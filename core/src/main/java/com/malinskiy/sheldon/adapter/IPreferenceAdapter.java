package com.malinskiy.sheldon.adapter;

import javax.annotation.Nonnull;

import rx.Observable;

public interface IPreferenceAdapter<T> {

    @Nonnull
    Observable<T> observe(@Nonnull String key, @Nonnull T defaultValue, @Nonnull com.malinskiy.sheldon.IGateway gateway);

    void put(@Nonnull String key, @Nonnull T value, com.malinskiy.sheldon.IGateway gateway);
}
