package com.malinskiy.sheldon.codegen.validator;

import com.google.auto.common.MoreTypes;

import com.malinskiy.sheldon.codegen.ProcessingException;

import java.util.List;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import io.reactivex.Observable;

public class ContainsValidator {
    public static void checkValidContainsMethod(@Nonnull ExecutableElement method) throws ProcessingException {
        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() != 1) {
            throw new ProcessingException(method,
                    "Invalid number of parameters for contains method. Should be one String parameter");
        }

        if (!MoreTypes.isTypeOf(Observable.class, method.getReturnType())) {
            throw new ProcessingException(
                    method,
                    "Should return Observable"
            );
        }
    }
}
