package com.malinskiy.sheldon2.provider;


import io.reactivex.Observable;

public class IntSharedPreferenceTypeTest extends BaseSharedPreferenceTypeTest<Integer> {

    @Override public void initDefaults() {
        defaultValue = 0;
        newValue = 42;
    }

    @Override public void put(String key, Integer value) {
        gateway.putInteger(key, value);
    }

    @Override public Observable<Integer> observe(String key, Integer defaultValue) {
        return gateway.observeInteger(key, defaultValue);
    }
}
