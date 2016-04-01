package com.malinskiy.sheldon;

import javax.annotation.Nonnull;

public abstract class GatewayBuilder {

    @Nonnull protected String namespace;

    public AllSet namespace(@Nonnull String namespace) {
        assert namespace != null;

        this.namespace = namespace;
        return prepare();
    }

    protected abstract AllSet prepare();

    public abstract class AllSet {
        public abstract IGateway build();
    }
}