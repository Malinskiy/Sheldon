package com.malinskiy.sheldon.provider;

import rx.Observable;

public class StringSharedPreferenceTypeTest extends BaseSharedPreferenceTypeTest<String> {

    @Override public void initDefaults() {
        defaultValue = "DEFAULT";
        newValue = "NEW";
    }

    @Override public void put(String key, String value) {
        gateway.putString(key, value);
    }

    @Override public Observable<String> observe(String key, String defaultValue) {
        return gateway.observeString(key, defaultValue);
    }
}
