package com.malinskiy.sheldon2.codegen.model;

import javax.annotation.Nonnull;
import javax.lang.model.element.VariableElement;

public class DefaultValue {

    @Nonnull private final VariableElement element;
    @Nonnull private final String prefName;

    public DefaultValue(@Nonnull VariableElement element,
                        @Nonnull String prefName) {

        this.element = element;
        this.prefName = prefName;
    }

    @Nonnull public VariableElement getElement() {
        return element;
    }

    @Nonnull public String getPrefName() {
        return prefName;
    }
}
