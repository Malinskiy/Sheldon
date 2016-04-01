package com.malinskiy.sheldon.sample;

import com.malinskiy.sheldon.IGateway;
import com.malinskiy.sheldon.adapter.IPreferenceAdapter;
import com.malinskiy.sheldon.annotation.Adapter;

import javax.annotation.Nonnull;

import rx.Observable;
import rx.functions.Func1;

@Adapter
public class TypeAdapter implements IPreferenceAdapter<Type> {
    @Nonnull @Override
    public Observable<Type> observe(@Nonnull String key, @Nonnull Type defaultValue, @Nonnull IGateway gateway) {
        return gateway.observeString(key, defaultValue.name())
                      .map(new Func1<String, Type>() {
                          @Override public Type call(String s) {
                              return Type.valueOf(s);
                          }
                      });
    }

    @Override public void put(@Nonnull String key, @Nonnull Type value, IGateway gateway) {
        gateway.putString(key, value.name());
    }
}
