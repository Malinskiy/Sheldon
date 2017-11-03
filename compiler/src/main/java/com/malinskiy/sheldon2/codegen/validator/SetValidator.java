package com.malinskiy.sheldon2.codegen.validator;

import com.malinskiy.sheldon2.annotation.Set;
import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.malinskiy.sheldon2.codegen.Utils;
import com.malinskiy.sheldon2.codegen.model.DefaultValue;
import io.reactivex.Completable;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class SetValidator {
    public static void checkValidSetter(@Nonnull ExecutableElement method, Map<String, DefaultValue> defaultsMap) throws ProcessingException {
        if (method.getAnnotation(Set.class) == null) {
            throw new ProcessingException(
                    method,
                    "No annotation @Set present. Did you annotate your method?"
            );
        }

        TypeMirror returnType = method.getReturnType();
        if (returnType.getKind() != TypeKind.VOID && !returnType.toString().equals(Completable.class.getCanonicalName())) {
            throw new ProcessingException(method,
                    "Invalid return type @%s for setter. Should be void or Completable",
                    returnType.toString());
        }

        List<? extends VariableElement> parameters = method.getParameters();
        if (parameters.size() != 1) {
            throw new ProcessingException(method,
                    "Invalid number of parameters for setter. Should be only one parameter");
        }

        if (!defaultsMap.containsKey(Utils.getName(method))) {
            throw new ProcessingException(
                    method,
                    "No default value found"
            );
        }
    }
}
