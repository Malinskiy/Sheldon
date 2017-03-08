package com.malinskiy.sheldon.codegen.model;

import com.google.common.base.Preconditions;

import com.malinskiy.sheldon.codegen.PrefType;
import com.malinskiy.sheldon.codegen.ProcessingException;
import com.malinskiy.sheldon.codegen.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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

    public Setter(@Nonnull String namespace,
                  @Nonnull ExecutableElement method,
                  @Nonnull String providerFieldName,
                  @Nonnull ClassName repositoryClassName) {

        this.namespace = Preconditions.checkNotNull(namespace);
        this.method = Preconditions.checkNotNull(method);
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
        this.repositoryClassName = Preconditions.checkNotNull(repositoryClassName);
    }

    @Override public void generateCode(TypeSpec.Builder builder) throws ProcessingException {
        VariableElement parameterElement = method.getParameters().get(0);
        TypeMirror parameterType = parameterElement.asType();

        String methodName = method.getSimpleName().toString();
        String parameterName = parameterElement.getSimpleName().toString();
        TypeName parameterTypeName = TypeName.get(parameterType);

        PrefType PREFType = Utils.getType(parameterElement);
        String prefName = Utils.getName(method);

        MethodSpec.Builder setterBuilder = MethodSpec.methodBuilder(methodName)
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .addAnnotation(Override.class)
                                                     .addParameter(parameterTypeName, parameterName)
                                                     .returns(void.class);

        switch (PREFType) {
            case BOOLEAN:
                setterBuilder.addStatement(simpleStatement("Boolean", prefName, parameterName));
                break;
            case FLOAT:
                setterBuilder.addStatement(simpleStatement("Float", prefName, parameterName));
                break;
            case INT:
                setterBuilder.addStatement(simpleStatement("Integer", prefName, parameterName));
                break;
            case LONG:
                setterBuilder.addStatement(simpleStatement("Long", prefName, parameterName));
                break;
            case STRING:
                setterBuilder.addStatement(simpleStatement("String", prefName, parameterName));
                break;
            case OBJECT:
                setterBuilder.addStatement("$T.getInstance().get($T.class).put(\"" + prefName + "\", " +
                                           parameterName + ", " + providerFieldName + ")", repositoryClassName,
                        parameterElement.asType());
                break;
            default:
                throw new ProcessingException(method, "Unsupported preference type %s", PREFType.name());
        }

        builder.addMethod(setterBuilder.build());
    }

    private String simpleStatement(String type, String prefName, String parameterName) {
        return providerFieldName + ".put" + type + "(\"" + prefName + "\", " + parameterName + ")";
    }
}
