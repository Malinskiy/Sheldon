package com.malinskiy.sheldon.codegen.validator;

import com.google.auto.common.MoreTypes;

import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.codegen.ProcessingException;
import com.malinskiy.sheldon.codegen.Utils;
import com.malinskiy.sheldon.codegen.model.DefaultValue;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import io.reactivex.Observable;


public class GetValidator {
    public static void checkValidGetter(@Nonnull ExecutableElement method, Map<String, DefaultValue> defaultsMap) throws ProcessingException {
        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() != 0) {
            throw new ProcessingException(method,
                    "Invalid number of parameters for getter. Should be zero parameters");
        }

        if (method.getAnnotation(Get.class) == null) {
            throw new ProcessingException(
                    method,
                    "No annotation @Get present. Did you annotate your method?"
            );
        }

        if (!MoreTypes.isTypeOf(Observable.class, method.getReturnType())) {
            throw new ProcessingException(
                    method,
                    "Should return Observable"
            );
        }

        if (!defaultsMap.containsKey(Utils.getName(method))) {
            throw new ProcessingException(
                    method,
                    "No default value found"
            );
        }
    }
}
