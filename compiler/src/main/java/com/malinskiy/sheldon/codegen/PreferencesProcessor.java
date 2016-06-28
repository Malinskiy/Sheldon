package com.malinskiy.sheldon.codegen;

import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;

import com.malinskiy.sheldon.IAdapterProvider;
import com.malinskiy.sheldon.adapter.IPreferenceAdapter;
import com.malinskiy.sheldon.annotation.Adapter;
import com.malinskiy.sheldon.annotation.Preferences;
import com.malinskiy.sheldon.codegen.model.Preference;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class PreferencesProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private Map<String, Preference> preferenceInterfaces = new LinkedHashMap<>();
    private Map<TypeMirror, Element> adapters = new LinkedHashMap<>();
    private boolean isAdapterRepositoryGenerated;
    private ClassName repositoryClassName;

    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Adapter.class)) {
                if (!annotatedElement.getKind().equals(ElementKind.CLASS)) {
                    error(annotatedElement, "Only classes can be annotated with @%s", Adapter.class.getSimpleName());
                    return true;
                }


                TypeMirror superType = MoreTypes.asTypeElement(typeUtils, annotatedElement.asType()).getInterfaces().get(0);
                if (!MoreTypes.isTypeOf(IPreferenceAdapter.class, superType)) {
                    error(annotatedElement, "Should extend @%s, @%s", IPreferenceAdapter.class.getSimpleName());
                    return true;
                }

                TypeMirror parameterType = MoreTypes.asDeclared(superType).getTypeArguments().get(0);
                adapters.put(parameterType, annotatedElement);
            }

            generateAdapterRepository(elementUtils, filer);

            for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Preferences.class)) {
                if (!annotatedElement.getKind().equals(ElementKind.INTERFACE)) {
                    error(annotatedElement, "Only interfaces can be annotated with @%s", Preferences.class.getSimpleName());
                    return true;
                }

                TypeElement typeElement = (TypeElement) annotatedElement;
                Preference annotatedInterface = new Preference(typeElement, repositoryClassName, messager);

                checkValidInterface(annotatedInterface);

                add(annotatedInterface);
            }


            for (Preference preferenceInterface : preferenceInterfaces.values()) {
                preferenceInterface.generateCode(elementUtils, filer);
            }

            preferenceInterfaces.clear();
        } catch (ProcessingException e) {
            error(e.getElement(), e.getMessage());
        } catch (IOException e) {
            error(null, e.getMessage());
        }

        return true;
    }

    private void generateAdapterRepository(Elements elementUtils, Filer filer) throws IOException {
        String className = "AdapterRepository";
        String packageName = IAdapterProvider.class.getPackage().getName();
        repositoryClassName = ClassName.get(packageName, className);

        if (isAdapterRepositoryGenerated) return;

        MethodSpec.Builder providerInitializer = MethodSpec.constructorBuilder()
                                                           .addModifiers(Modifier.PRIVATE);

        for (Map.Entry<TypeMirror, Element> entry : adapters.entrySet()) {
            providerInitializer.addStatement("register($T.class, new $T())", entry.getKey(), entry.getValue().asType());
        }

        FieldSpec.Builder instanceFieldSpec =
                FieldSpec.builder(repositoryClassName, "INSTANCE", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                         .initializer("new $T()", repositoryClassName);

        ParameterizedTypeName adaptersMapClassName = ParameterizedTypeName.get(Map.class, Class.class, IPreferenceAdapter.class);
        FieldSpec.Builder adaptersFieldSpec =
                FieldSpec.builder(adaptersMapClassName, "adapters", Modifier.PRIVATE)
                         .initializer("new $T<>()", ConcurrentHashMap.class);

        TypeVariableName t = TypeVariableName.get("T");
        MethodSpec.Builder registerMethodBuidler = MethodSpec.methodBuilder("register")
                                                             .addModifiers(Modifier.PUBLIC)
                                                             .addTypeVariable(t)
                                                             .returns(void.class)
                                                             .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), t), "clazz")
                                                             .addParameter(ParameterizedTypeName.get(ClassName.get(IPreferenceAdapter.class), t), "adapter")
                                                             .addStatement("adapters.put($N, $N)", "clazz", "adapter");

        MethodSpec.Builder getMethodBuidler = MethodSpec.methodBuilder("get")
                                                        .addModifiers(Modifier.PUBLIC)
                                                        .addTypeVariable(t)
                                                        .addAnnotation(Override.class)
                                                        .returns(ParameterizedTypeName.get(ClassName.get(IPreferenceAdapter.class), t))
                                                        .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), t), "clazz")
                                                        .addStatement("return adapters.get($N)", "clazz");


        MethodSpec.Builder getInstanceMethodBuidler = MethodSpec.methodBuilder("getInstance")
                                                                .addModifiers(Modifier.PUBLIC)
                                                                .addModifiers(Modifier.STATIC)
                                                                .returns(repositoryClassName)
                                                                .addStatement("return $N", "INSTANCE");

        TypeSpec.Builder builder = TypeSpec.classBuilder(className)
                                           .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                           .addSuperinterface(TypeName.get(IAdapterProvider.class))
                                           .addField(instanceFieldSpec.build())
                                           .addField(adaptersFieldSpec.build())
                                           .addMethod(providerInitializer.build())
                                           .addMethod(registerMethodBuidler.build())
                                           .addMethod(getMethodBuidler.build())
                                           .addMethod(getInstanceMethodBuidler.build());

        JavaFile javaFile = JavaFile.builder(packageName, builder.build())
                                    .build();

        javaFile.writeTo(filer);
        isAdapterRepositoryGenerated = true;
    }

    @Override public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<String>();
        annotataions.add(Preferences.class.getCanonicalName());
        return annotataions;
    }

    @Override public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void add(Preference toInsert) throws ProcessingException {
        Preference existing = preferenceInterfaces.get(toInsert.getNamespace());
        if (existing != null) {
            throw new ProcessingException(toInsert.getTypeElement(),
                    "Conflict: The interface %s is annotated with @%s with name ='%s' but %s already uses the same " +
                    "name",
                    toInsert.getTypeElement().getQualifiedName().toString(), Preferences.class.getSimpleName(),
                    toInsert.getNamespace(), existing.getTypeElement().getQualifiedName().toString());
        }

        preferenceInterfaces.put(toInsert.getNamespace(), toInsert);
    }

    private void checkValidInterface(Preference item) throws ProcessingException {
        TypeElement classElement = item.getTypeElement();

        if (!classElement.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException(classElement, "The interface %s is not public.",
                    classElement.getQualifiedName().toString());
        }
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }
}
