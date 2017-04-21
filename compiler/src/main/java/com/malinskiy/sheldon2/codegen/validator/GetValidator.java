package com.malinskiy.sheldon2.codegen.validator;

import com.google.auto.common.MoreTypes;

import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.malinskiy.sheldon2.codegen.Utils;
import com.malinskiy.sheldon2.codegen.model.DefaultValue;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

import io.reactivex.Observable;


public class GetValidator {
    public static void checkValidGetter(@Nonnull ExecutableElement method, Map<String, DefaultValue> defaultsMap) throws ProcessingException {
        List<? extends VariableElement> parameters = method.getParameters();
        if (!parameters.isEmpty()) {
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
