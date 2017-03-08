package com.malinskiy.sheldon.adapter;

import com.malinskiy.sheldon.IGateway;

import javax.annotation.Nonnull;

import io.reactivex.Observable;

public interface IPreferenceAdapter<T> {

    @Nonnull
    Observable<T> observe(@Nonnull String key, @Nonnull T defaultValue, @Nonnull IGateway gateway);

    void put(@Nonnull String key, @Nonnull T value, IGateway gateway);
}
