package com.malinskiy.sheldon.provider;

import rx.Observable;

public class FloatSharedPreferenceTypeTest extends BaseSharedPreferenceTypeTest<Float> {

    @Override public void initDefaults() {
        defaultValue = 0.0f;
        newValue = 5.0f;
    }

    @Override public void put(String key, Float value) {
        gateway.putFloat(key, value);
    }

    @Override public Observable<Float> observe(String key, Float defaultValue) {
        return gateway.observeFloat(key, defaultValue);
    }
}
