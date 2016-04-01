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
    public static PREF_TYPE getType(VariableElement parameterElement) {
        TypeMirror type = parameterElement.asType();

        return getType(type);
    }

    /**
     * Get preference type from method element
     */
    public static PREF_TYPE getType(ExecutableElement method) throws com.malinskiy.sheldon.codegen.ProcessingException {
        boolean isSetter = method.getReturnType().getKind().equals(TypeKind.VOID);

        return isSetter ?
               getType(method.getParameters().get(0).asType()) :
               getType(MoreTypes.asDeclared(method.getReturnType()).getTypeArguments().get(0));
    }

    private static PREF_TYPE getType(TypeMirror type) {
        if (MoreTypes.isTypeOf(Boolean.class, type)) {
            return PREF_TYPE.BOOLEAN;
        } else if (MoreTypes.isTypeOf(Float.class, type)) {
            return PREF_TYPE.FLOAT;
        } else if (MoreTypes.isTypeOf(Integer.class, type)) {
            return PREF_TYPE.INT;
        } else if (MoreTypes.isTypeOf(Long.class, type)) {
            return PREF_TYPE.LONG;
        } else if (MoreTypes.isTypeOf(String.class, type)) {
            return PREF_TYPE.STRING;
        } else {
            return PREF_TYPE.OBJECT;
        }
    }

    /**
     * Get name of preference from getter or setter method
     */
    public static String getName(ExecutableElement method) throws com.malinskiy.sheldon.codegen.ProcessingException {
        Get getAnnotation = method.getAnnotation(Get.class);
        Set setAnnotation = method.getAnnotation(Set.class);

        if (getAnnotation == null && setAnnotation == null) {
            throw new com.malinskiy.sheldon.codegen.ProcessingException(method, "No annotations present. Should be either @Get or @Set");
        } else if (getAnnotation == null && setAnnotation != null) {
            return setAnnotation.name();
        } else if (getAnnotation != null && setAnnotation == null) {
            return getAnnotation.name();
        } else {
            throw new com.malinskiy.sheldon.codegen.ProcessingException(method, "Method annotated with @Get and @Set");
        }
    }

    /**
     * Get preference name from field annotated with
     */
    public static String getName(VariableElement variable) throws com.malinskiy.sheldon.codegen.ProcessingException {
        Default annotation = variable.getAnnotation(Default.class);

        if (annotation == null) {
            throw new com.malinskiy.sheldon.codegen.ProcessingException(variable, "No annotation @Default present");
        }

        return annotation.name();
    }
}
