package com.malinskiy.sheldon.adapter.impl;

import com.malinskiy.sheldon.IGateway;
import com.malinskiy.sheldon.adapter.IPreferenceAdapter;

import javax.annotation.Nonnull;

import rx.Completable;
import rx.Observable;
import rx.functions.Func1;

public class EnumAdapter<T extends Enum<T>> implements IPreferenceAdapter<T> {

    @Nonnull private final Class<T> clazz;

    public EnumAdapter(@Nonnull Class<T> clazz) {
        this.clazz = clazz;
    }

    @Nonnull @Override
    public Observable<T> observe(@Nonnull String key, @Nonnull T defaultValue, @Nonnull IGateway gateway) {
        return gateway.observeString(key, toString(defaultValue))
                      .map(new Func1<String, T>() {
                          @Override public T call(String s) {
                              return fromString(s);
                          }
                      });
    }

    @Override public void put(@Nonnull String key, @Nonnull T value, IGateway gateway) {
        gateway.putString(key, toString(value));
    }

    @Override public Completable putSync(@Nonnull String key, @Nonnull T value, IGateway gateway) {
        return gateway.putStringSync(key,toString(value));
    }

    @Nonnull private T fromString(@Nonnull String string) {
        return Enum.valueOf(clazz, string);
    }

    @Nonnull private String toString(@Nonnull T value) {
        return value != null ? value.name() : "";
    }
}
