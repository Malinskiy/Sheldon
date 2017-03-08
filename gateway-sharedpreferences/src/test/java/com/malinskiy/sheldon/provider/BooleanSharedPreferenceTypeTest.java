package com.malinskiy.sheldon.provider;


import io.reactivex.Observable;

public class BooleanSharedPreferenceTypeTest extends BaseSharedPreferenceTypeTest<Boolean> {

    @Override public void initDefaults() {
        defaultValue = false;
        newValue = true;
    }

    @Override public void put(String key, Boolean value) {
        gateway.putBoolean(key, value);
    }

    @Override public Observable<Boolean> observe(String key, Boolean defaultValue) {
        return gateway.observeBoolean(key, defaultValue);
    }
}
