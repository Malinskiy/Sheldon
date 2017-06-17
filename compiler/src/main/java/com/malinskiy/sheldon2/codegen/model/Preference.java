package com.malinskiy.sheldon2.codegen.model;

import com.google.common.base.CaseFormat;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.malinskiy.sheldon2.GatewayBuilder;
import com.malinskiy.sheldon2.IGateway;
import com.malinskiy.sheldon2.annotation.Delete;
import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.annotation.Preferences;
import com.malinskiy.sheldon2.annotation.Set;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.malinskiy.sheldon2.codegen.Utils;
import com.malinskiy.sheldon2.codegen.validator.ConsistencyValidator;
import com.malinskiy.sheldon2.codegen.validator.ContainsValidator;
import com.malinskiy.sheldon2.codegen.validator.DeleteValidator;
import com.malinskiy.sheldon2.codegen.validator.GetValidator;
import com.malinskiy.sheldon2.codegen.validator.SetValidator;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class Preference {

    private static final String SUFFIX = "_Preferences";
    private static final String PROVIDER_FIELD_NAME = "provider";

    @Nonnull private final String namespace;
    @Nonnull private final TypeElement annotatedClassElement;
    @Nonnull private final ClassName repositoryClassName;
    @Nonnull private final Messager messager;

    @Nonnull private final Map<String, Getter> gettersMap = Maps.newHashMap();
    @Nonnull private final Map<String, Setter> settersMap = Maps.newHashMap();
    @Nonnull private final Map<String, DefaultValue> defaultsMap = Maps.newHashMap();
    @Nonnull private Optional<Contains> contains = Optional.absent();
    @Nonnull private Optional<Deleter> deleter = Optional.absent();

    public Preference(@Nonnull TypeElement interfaceElement,
                      @Nonnull ClassName repositoryClassName,
                      @Nonnull Messager messager) throws ProcessingException {

        annotatedClassElement = Preconditions.checkNotNull(interfaceElement);
        this.repositoryClassName = Preconditions.checkNotNull(repositoryClassName);
        this.messager = Preconditions.checkNotNull(messager);

        Preferences annotation = interfaceElement.getAnnotation(Preferences.class);
        namespace = annotation.name();

        if (Strings.isNullOrEmpty(namespace)) {
            throw new ProcessingException(interfaceElement,
                    "name() in @%s for interface %s is null or empty! that's not allowed",
                    Preferences.class.getSimpleName(),
                    interfaceElement.getQualifiedName().toString());
        }

        //1. Populate defaults first
        for (Element element : annotatedClassElement.getEnclosedElements()) {
            if (element.getKind().equals(ElementKind.FIELD)) {
                VariableElement variable = (VariableElement) element;
                addDefault(variable);
            }
        }

        //2. Populate getters and setters
        for (Element element : annotatedClassElement.getEnclosedElements()) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) element;
                if (method.getAnnotation(Get.class) != null) {
                    GetValidator.checkValidGetter(method, defaultsMap);
                    addGetter(method);
                } else if (method.getAnnotation(Set.class) != null) {
                    SetValidator.checkValidSetter(method, defaultsMap);
                    addSetter(method);
                } else if (method.getAnnotation(Delete.class) != null) {
                    DeleteValidator.checkValidDelete(method);
                    deleter = Optional.of(new Deleter(method, PROVIDER_FIELD_NAME));
                } else if (method.getAnnotation(com.malinskiy.sheldon2.annotation.Contains.class) != null) {
                    ContainsValidator.checkValidContainsMethod(method);
                    contains = Optional.of(new Contains(method, PROVIDER_FIELD_NAME));
                } else {
                    throw new ProcessingException(
                            method,
                            "Unrecognized method"
                    );
                }
            }
        }


        ConsistencyValidator.checkConsistency(defaultsMap.keySet(), gettersMap.keySet(), settersMap.keySet());
    }

    private void addDefault(VariableElement variable) throws ProcessingException {
        String name = Utils.getName(variable);

        defaultsMap.put(name, new DefaultValue(variable, name));
    }

    private void addSetter(ExecutableElement method) throws ProcessingException {
        String name = Utils.getName(method);

        settersMap.put(name, new Setter(namespace, method, PROVIDER_FIELD_NAME, repositoryClassName));
    }

    private void addGetter(ExecutableElement method) throws ProcessingException {
        String name = Utils.getName(method);
        gettersMap.put(name, new Getter(namespace, method, PROVIDER_FIELD_NAME, defaultsMap.get(name), repositoryClassName));
    }

    public String getNamespace() {
        return namespace;
    }

    /**
     * The original element that was annotated with @Preferences
     */
    public TypeElement getTypeElement() {
        return annotatedClassElement;
    }

    public void generateCode(Elements elementUtils, Filer filer) throws IOException, ProcessingException {
        String className = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, namespace.toLowerCase());
        String preferencesClassName = className + SUFFIX;

        PackageElement pkg = elementUtils.getPackageOf(annotatedClassElement);
        String packageName = pkg.isUnnamed() ? null : pkg.getQualifiedName().toString();

        FieldSpec.Builder providerFieldSpec =
                FieldSpec.builder(TypeName.get(IGateway.class), PROVIDER_FIELD_NAME, Modifier.PRIVATE, Modifier.FINAL);

        MethodSpec.Builder builderConstructor =
                MethodSpec.constructorBuilder()
                          .addModifiers(Modifier.PUBLIC)
                          .addParameter(GatewayBuilder.class, "builder")
                          .addStatement("this.$N = builder.namespace($S).build()", PROVIDER_FIELD_NAME, namespace);

        TypeSpec.Builder builder = TypeSpec.classBuilder(preferencesClassName)
                                           .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                           .addSuperinterface(TypeName.get(annotatedClassElement.asType()))
                                           .addField(providerFieldSpec.build())
                                           .addMethod(builderConstructor.build());


        for (Setter setter : settersMap.values()) {
            setter.generateCode(builder);
        }

        for (Getter getter : gettersMap.values()) {
            getter.generateCode(builder);
        }

        if (contains.isPresent()) {
            contains.get().generateCode(builder);
        }

        if (deleter.isPresent()) {
            deleter.get().generateCode(builder);
        }

        new Cleaner(PROVIDER_FIELD_NAME).generateCode(builder);

        JavaFile javaFile = JavaFile.builder(packageName, builder.build())
                                    .build();

        javaFile.writeTo(filer);
    }

    public void generateUpdateMethod(Elements elementUtils, Filer filer) throws IOException {

    }
}
