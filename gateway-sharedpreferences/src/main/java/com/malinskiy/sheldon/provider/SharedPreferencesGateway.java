package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.IGateway;

import android.content.SharedPreferences;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import rx.Completable;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

public class SharedPreferencesGateway implements IGateway {

    @Nonnull private Observable<String> keyChangesObservable;
    @Nonnull private SharedPreferences preferences;
    @Nullable private SharedPreferences.OnSharedPreferenceChangeListener listener;

    public SharedPreferencesGateway(@Nonnull final SharedPreferences preferences) {
        this.preferences = preferences;

        keyChangesObservable =
                Observable.using(
                        new Func0<SharedPreferences>() {
                            @Override public SharedPreferences call() {
                                return preferences;
                            }
                        },

                        new Func1<SharedPreferences, Observable<? extends String>>() {
                            @Override
                            public Observable<? extends String> call(final SharedPreferences sharedPreferences) {
                                return Observable.create(new Observable.OnSubscribe<String>() {
                                    @Override public void call(final Subscriber<? super String> subscriber) {
                                        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                                            @Override
                                            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                                                subscriber.onNext(key);
                                            }
                                        };
                                        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
                                    }
                                });
                            }
                        },

                        new Action1<SharedPreferences>() {
                            @Override public void call(SharedPreferences sharedPreferences) {
                                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
                                listener = null;
                            }
                        })
                        .publish()
                        .refCount();
    }

    @Nonnull @Override
    public Observable<Boolean> observeBoolean(@Nonnull final String key,
                                              @Nonnull final Boolean defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Func1<String, Boolean>() {
                    @Override public Boolean call(String s) {
                        return preferences.getBoolean(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Float> observeFloat(@Nonnull final String key,
                                                             @Nonnull final Float defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Func1<String, Float>() {
                    @Override public Float call(String s) {
                        return preferences.getFloat(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Integer> observeInteger(@Nonnull final String key,
                                                                 @Nonnull final Integer defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Func1<String, Integer>() {
                    @Override public Integer call(String s) {
                        return preferences.getInt(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<Long> observeLong(@Nonnull final String key,
                                                           @Nonnull final Long defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Func1<String, Long>() {
                    @Override public Long call(String s) {
                        return preferences.getLong(s, defaultValue);
                    }
                });
    }

    @Nonnull @Override public Observable<String> observeString(@Nonnull final String key,
                                                               @Nonnull final String defaultValue) {

        return keyChangesObservable
                .startWith(key)
                .filter(new Func1<String, Boolean>() {
                    @Override public Boolean call(String changedKey) {
                        return changedKey.equals(key);
                    }
                })
                .map(new Func1<String, String>() {
                    @Override public String call(String s) {
                        return preferences.getString(s, defaultValue);
                    }
                });
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

    @Override public Completable putBooleanSync(@Nonnull final String key, @Nonnull final Boolean value) {
        return Completable.fromAction(new Action0() {
            @Override public void call() {
                preferences.edit()
                        .putBoolean(key, value)
                        .commit();
            }
        });
    }

    @Override public Completable putFloatSync(@Nonnull final String key, @Nonnull final Float value) {
        return Completable.fromAction(new Action0() {
            @Override public void call() {
                preferences.edit()
                        .putFloat(key, value)
                        .commit();
            }
        });
    }

    @Override public Completable putIntegerSync(@Nonnull final String key, @Nonnull final Integer value) {
        return Completable.fromAction(new Action0() {
            @Override public void call() {
                preferences.edit()
                        .putInt(key, value)
                        .commit();
            }
        });
    }

    @Override public Completable putLongSync(@Nonnull final String key, @Nonnull final Long value) {
        return Completable.fromAction(new Action0() {
            @Override public void call() {
                preferences.edit()
                        .putLong(key, value)
                        .commit();
            }
        });

    }

    @Override public Completable putStringSync(@Nonnull final String key, @Nonnull final String value) {
        return Completable.fromAction(new Action0() {
            @Override public void call() {
                preferences.edit()
                        .putString(key, value)
                        .commit();
            }
        });
    }

    @Nonnull @Override public Observable<Boolean> contains(@Nonnull final String key) {
        return Observable.fromCallable(new Func0<Boolean>() {
            @Override public Boolean call() {
                return preferences.contains(key);
            }
        });
    }

    @Override public void remove(@Nonnull String key) {
        preferences.edit()
                .remove(key)
                .apply();
    }
}
