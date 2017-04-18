package com.malinskiy.sheldon.codegen.model;

import com.google.common.base.Preconditions;

import com.malinskiy.sheldon.codegen.ProcessingException;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;

import io.reactivex.Observable;

public class Contains implements Generatable {

    @Nonnull private final ExecutableElement method;
    @Nonnull private final String providerFieldName;

    public Contains(@Nonnull ExecutableElement method,
                    @Nonnull String providerFieldName) {

        this.method = Preconditions.checkNotNull(method);
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
    }

    @Override
    public void generateCode(TypeSpec.Builder builder) throws ProcessingException {
        builder.addMethod(
                MethodSpec.methodBuilder(method.getSimpleName().toString())
                          .addModifiers(Modifier.PUBLIC)
                          .addParameter(String.class, "key")
                          .returns(ParameterizedTypeName.get(Observable.class, Boolean.class))
                          .addStatement("return " + providerFieldName + ".contains($N)", "key")
                          .build());
    }

}
