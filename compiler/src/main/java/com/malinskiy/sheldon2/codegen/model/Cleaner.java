package com.malinskiy.sheldon2.codegen.model;

import com.google.common.base.Preconditions;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.Nonnull;
import javax.lang.model.element.Modifier;

/**
 * Created by konstantinaksenov on 25.05.17.
 */

public class Cleaner implements Generatable {

    @Nonnull private final String providerFieldName;

    public Cleaner(@Nonnull String providerFieldName) {
        this.providerFieldName = Preconditions.checkNotNull(providerFieldName);
    }

    @Override
    public void generateCode(TypeSpec.Builder builder) throws ProcessingException {
        builder.addMethod(
                MethodSpec.methodBuilder("clear")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement(providerFieldName + ".clear()")
                        .build());
    }
}