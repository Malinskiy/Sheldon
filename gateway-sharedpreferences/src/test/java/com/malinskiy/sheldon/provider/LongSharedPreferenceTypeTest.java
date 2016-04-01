package com.malinskiy.sheldon.provider;

import rx.Observable;

public class LongSharedPreferenceTypeTest extends BaseSharedPreferenceTypeTest<Long> {

    @Override public void initDefaults() {
        defaultValue = 0L;
        newValue = 42L;
    }

    @Override public void put(String key, Long value) {
        gateway.putLong(key, value);
    }

    @Override public Observable<Long> observe(String key, Long defaultValue) {
        return gateway.observeLong(key, defaultValue);
    }
}
