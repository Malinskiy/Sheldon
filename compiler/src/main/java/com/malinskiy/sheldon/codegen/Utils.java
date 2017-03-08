package com.malinskiy.sheldon.codegen;

import com.google.auto.common.MoreTypes;

import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public class Utils {

    /**
     * Get preference type from variable element (field, method parameter)
     */
    public static PrefType getType(VariableElement parameterElement) {
        TypeMirror type = parameterElement.asType();

        return getType(type);
    }

    /**
     * Get preference type from method element
     */
    public static PrefType getType(ExecutableElement method) throws ProcessingException {
        boolean isSetter = method.getReturnType().getKind().equals(TypeKind.VOID);

        return isSetter ?
               getType(method.getParameters().get(0).asType()) :
               getType(MoreTypes.asDeclared(method.getReturnType()).getTypeArguments().get(0));
    }

    private static PrefType getType(TypeMirror type) {
        if (MoreTypes.isTypeOf(Boolean.class, type)) {
            return PrefType.BOOLEAN;
        } else if (MoreTypes.isTypeOf(Float.class, type)) {
            return PrefType.FLOAT;
        } else if (MoreTypes.isTypeOf(Integer.class, type)) {
            return PrefType.INT;
        } else if (MoreTypes.isTypeOf(Long.class, type)) {
            return PrefType.LONG;
        } else if (MoreTypes.isTypeOf(String.class, type)) {
            return PrefType.STRING;
        } else {
            return PrefType.OBJECT;
        }
    }

    /**
     * Get name of preference from getter or setter method
     */
    public static String getName(ExecutableElement method) throws ProcessingException {
        Get getAnnotation = method.getAnnotation(Get.class);
        Set setAnnotation = method.getAnnotation(Set.class);

        if (getAnnotation == null && setAnnotation == null) {
            throw new ProcessingException(method, "No annotations present. Should be either @Get or @Set");
        } else if (getAnnotation == null && setAnnotation != null) {
            return setAnnotation.name();
        } else if (getAnnotation != null && setAnnotation == null) {
            return getAnnotation.name();
        } else {
            throw new ProcessingException(method, "Method annotated with @Get and @Set");
        }
    }

    /**
     * Get preference name from field annotated with
     */
    public static String getName(VariableElement variable) throws ProcessingException {
        Default annotation = variable.getAnnotation(Default.class);

        if (annotation == null) {
            throw new ProcessingException(variable, "No annotation @Default present");
        }

        return annotation.name();
    }
}
