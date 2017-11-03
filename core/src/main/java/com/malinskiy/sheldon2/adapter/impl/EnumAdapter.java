package com.malinskiy.sheldon2.adapter.impl;

import com.malinskiy.sheldon2.IGateway;
import com.malinskiy.sheldon2.adapter.IPreferenceAdapter;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


public class EnumAdapter<T extends Enum<T>> implements IPreferenceAdapter<T> {

    @Nonnull private final Class<T> clazz;

    public EnumAdapter(@Nonnull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Nonnull @Override
    public Observable<T> observe(@Nonnull String key, @Nonnull T defaultValue, @Nonnull IGateway gateway) {
        return gateway.observeString(key, toString(defaultValue)).map(new Function<String, T>() {
            @Override public T apply(@NonNull String s) throws Exception {
                return fromString(s);
            }
        });
    }

    @Override public void put(@Nonnull String key, @Nonnull T value, IGateway gateway) {
        gateway.putString(key, toString(value));
    }

    @Override
    public Completable putSync(@Nonnull String key, @Nonnull T value, IGateway gateway) {
        return gateway.putStringSync(key, toString(value));
    }

    @Nonnull private T fromString(@Nonnull String string) {
        return Enum.valueOf(clazz, string);
    }

    @Nonnull private String toString(@Nonnull T value) {
        return value != null ? value.name() : "";
    }
}
