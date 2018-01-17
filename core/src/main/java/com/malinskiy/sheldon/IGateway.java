package com.malinskiy.sheldon;

import javax.annotation.Nonnull;

import rx.Completable;
import rx.Observable;

public interface IGateway {

    @Nonnull Observable<Boolean> observeBoolean(@Nonnull String key, @Nonnull Boolean defaultValue);

    @Nonnull Observable<Float> observeFloat(@Nonnull String key, @Nonnull Float defaultValue);

    @Nonnull Observable<Integer> observeInteger(@Nonnull String key, @Nonnull Integer defaultValue);

    @Nonnull Observable<Long> observeLong(@Nonnull String key, @Nonnull Long defaultValue);

    @Nonnull Observable<String> observeString(@Nonnull String key, @Nonnull String defaultValue);

    void putBoolean(@Nonnull String key, @Nonnull Boolean value);

    void putFloat(@Nonnull String key, @Nonnull Float value);

    void putInteger(@Nonnull String key, @Nonnull Integer value);

    void putLong(@Nonnull String key, @Nonnull Long value);

    void putString(@Nonnull String key, @Nonnull String value);

    Completable putBooleanSync(@Nonnull String key, @Nonnull Boolean value);

    Completable putFloatSync(@Nonnull String key, @Nonnull Float value);

    Completable putIntegerSync(@Nonnull String key, @Nonnull Integer value);

    Completable putLongSync(@Nonnull String key, @Nonnull Long value);

    Completable putStringSync(@Nonnull String key, @Nonnull String value);


    @Nonnull Observable<Boolean> contains(@Nonnull String key);

    void remove(@Nonnull String key);
}
