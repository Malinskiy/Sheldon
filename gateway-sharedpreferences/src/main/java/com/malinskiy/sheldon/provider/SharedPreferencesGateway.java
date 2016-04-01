package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.IGateway;

import android.content.SharedPreferences;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Observable;
import rx.Subscriber;

public class SharedPreferencesGateway implements IGateway {

    @Nonnull private Observable<String> keyChangesObservable;
    @Nonnull private SharedPreferences preferences;
    @Nullable private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public SharedPreferencesGateway(@Nonnull SharedPreferences preferences) {
        this.preferences = preferences;

        keyChangesObservable =
                Observable.using(
                        () -> preferences,

                        preferencesToRegisterTo -> Observable.create(new Observable.OnSubscribe<String>() {
                            @Override public void call(final Subscriber<? super String> subscriber) {
                                listener = (sharedPreferences1, key) -> {
                                    subscriber.onNext(key);
                                };
                                preferencesToRegisterTo.registerOnSharedPreferenceChangeListener(listener);
                            }
                        }),

                        preferencesToDisposeOf -> {
                            preferencesToDisposeOf.unregisterOnSharedPreferenceChangeListener(listener);
                            listener = null;
                        })
                          .publish()
                          .refCount();
    }

    @Nonnull @Override
    public Observable<Boolean> observeBoolean(@Nonnull final String key, @Nonnull final Boolean defaultValue) {
        return keyChangesObservable
                .startWith(key)
                .filter(changedKey -> changedKey.equals(key))
                .map(s -> preferences.getBoolean(s, defaultValue));
    }

    @Nonnull @Override public Observable<Float> observeFloat(@Nonnull String key, @Nonnull Float defaultValue) {
        return keyChangesObservable
                .startWith(key)
                .filter(changedKey -> changedKey.equals(key))
                .map(s -> preferences.getFloat(s, defaultValue));
    }

    @Nonnull @Override public Observable<Integer> observeInteger(@Nonnull String key, @Nonnull Integer defaultValue) {
        return keyChangesObservable
                .startWith(key)
                .filter(changedKey -> changedKey.equals(key))
                .map(s -> preferences.getInt(s, defaultValue));
    }

    @Nonnull @Override public Observable<Long> observeLong(@Nonnull String key, @Nonnull Long defaultValue) {
        return keyChangesObservable
                .startWith(key)
                .filter(changedKey -> changedKey.equals(key))
                .map(s -> preferences.getLong(s, defaultValue));
    }

    @Nonnull @Override public Observable<String> observeString(@Nonnull String key, @Nonnull String defaultValue) {
        return keyChangesObservable
                .startWith(key)
                .filter(changedKey -> changedKey.equals(key))
                .map(s -> preferences.getString(s, defaultValue));
    }

    @Override public void putBoolean(@Nonnull String key, @Nonnull Boolean value) {
        preferences.edit()
                   .putBoolean(key, value)
                   .apply();
    }

    @Override public void putFloat(@Nonnull String key, @Nonnull Float value) {
        preferences.edit()
                   .putFloat(key, value)
                   .apply();
    }

    @Override public void putInteger(@Nonnull String key, @Nonnull Integer value) {
        preferences.edit()
                   .putInt(key, value)
                   .apply();
    }

    @Override public void putLong(@Nonnull String key, @Nonnull Long value) {
        preferences.edit()
                   .putLong(key, value)
                   .apply();
    }

    @Override public void putString(@Nonnull String key, @Nonnull String value) {
        preferences.edit()
                   .putString(key, value)
                   .apply();
    }

    @Nonnull @Override public Observable<Boolean> contains(@Nonnull String key) {
        return Observable.fromCallable(() -> preferences.contains(key));
    }

    @Override public void remove(@Nonnull String key) {
        preferences.edit()
                   .remove(key)
                   .apply();
    }
}
