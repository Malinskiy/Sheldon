package com.malinskiy.sheldon2.codegen.model;

import com.google.common.base.Preconditions;

import com.malinskiy.sheldon2.codegen.PrefType;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.malinskiy.sheldon2.codegen.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.reactivex.Completable;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class Setter implements Generatable {

    @Nonnull private final String namespace;
    @Nonnull private final ExecutableElement method;
    @Nonnull private final String providerFieldName;
    @Nonnull private final ClassName repositoryClassName;

    private final VariableElement parameterElement;
    private final TypeMirror parameterType;
    private final String methodName;
    private final String parameterName;
    private final TypeName parameterTypeName;
    private final PrefType PREFType;
    private final boolean isReactive;

    public Setter(@Nonnull String namespace,
                  @Nonnull ExecutableElement method,
                  @Nonnull String providerFieldName,
                  @Nonnull ClassName repositoryClassName) {

        this.namespace = Preconditions.checkNotNull(namespace);
        this.method = Preconditions.checkNotNull(method);
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
        this.repositoryClassName = Preconditions.checkNotNull(repositoryClassName);
        this.parameterElement = method.getParameters().get(0);
        this.parameterType = parameterElement.asType();
        this.methodName = method.getSimpleName().toString();
        this.parameterName = parameterElement.getSimpleName().toString();
        this.parameterTypeName = TypeName.get(parameterType);
        this.PREFType = Utils.getType(parameterElement);
        this.isReactive = method.getReturnType().toString().equals(Completable.class.getCanonicalName());
    }

    @Override public void generateCode(TypeSpec.Builder builder) throws ProcessingException {
        String prefName = Utils.getName(method);

        MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameterTypeName, parameterName)
                .returns(TypeName.get(method.getReturnType()));


        switch (PREFType) {
            case BOOLEAN:
                setterBuilder.addStatement(primitiveTypeStatement("Boolean", prefName));
                break;
            case FLOAT:
                setterBuilder.addStatement(primitiveTypeStatement("Float", prefName));
                break;
            case INT:
                setterBuilder.addStatement(primitiveTypeStatement("Integer", prefName));
                break;
            case LONG:
                setterBuilder.addStatement(primitiveTypeStatement("Long", prefName));
                break;
            case STRING:
                setterBuilder.addStatement(primitiveTypeStatement("String", prefName));
                break;
            case OBJECT:
                setterBuilder.addStatement(objectStatement(prefName), repositoryClassName, parameterType);
                break;
            default:
                throw new ProcessingException(method, "Unsupported preference type %s", PREFType.name());
        }

        builder.addMethod(setterBuilder.build());
    }

    private String objectStatement(String prefName) {
        return isReactive ? reactiveObjectStatement(prefName) : simpleObjectStatement(prefName);
    }

    private String reactiveObjectStatement(String prefName) {
        return "return $T.getInstance().get($T.class).putSync(\"" + prefName + "\", " +
                parameterName + ", " + providerFieldName + ")";
    }

    private String simpleObjectStatement(String prefName) {
        return "$T.getInstance().get($T.class).put(\"" + prefName + "\", " +
                parameterName + ", " + providerFieldName + ")";
    }

    private String primitiveTypeStatement(String type, String prefName) {
        return isReactive ? reactiveSimpleStatement(type, prefName) : simpleStatement(type, prefName);
    }

    private String simpleStatement(String type, String prefName) {
        return providerFieldName + ".put" + type + "(\"" + prefName + "\", " + parameterName + ")";
    }

    private String reactiveSimpleStatement(String type, String prefName) {
        return "return " + providerFieldName + ".put" + type + "Sync" + "(\"" + prefName + "\", " + parameterName + ")";
    }
}
