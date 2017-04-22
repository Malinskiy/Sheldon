package com.malinskiy.sheldon2.codegen.validator;

import com.google.auto.common.MoreTypes;

import com.malinskiy.sheldon2.codegen.ProcessingException;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;

public class DeleteValidator {
    public static void checkValidDelete(@Nonnull ExecutableElement method) throws ProcessingException {
        if (method.getReturnType().getKind() != TypeKind.VOID) {
            throw new ProcessingException(
                    method,
                    "Should return void"
            );
        }

        if (method.getParameters().size() != 1 ||
            !MoreTypes.isTypeOf(String.class, method.getParameters().get(0).asType())) {
            throw new ProcessingException(
                    method,
                    "Should have only one String parameter"
            );
        }
    }
}
