package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.BuildConfig;
import com.malinskiy.sheldon.IGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.app.Application;

import rx.Observable;
import rx.observers.TestSubscriber;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public abstract class BaseSharedPreferenceTypeTest<T> {

    public static final String PREFERENCE_KEY = "preference";
    private Application context;
    protected IGateway gateway;

    protected T defaultValue;
    protected T newValue;

    public abstract void initDefaults();

    public abstract void put(String key, T value);

    public abstract Observable<T> observe(String key, T defaultValue);

    @Before
    public void setUp() throws Exception {
        context = RuntimeEnvironment.application;
        gateway = new SharedPreferencesGatewayBuilder(context)
                .namespace("test")
                .build();
        initDefaults();
    }

    @Test
    public void testSubscribeTriggersInitialValue() {
        TestSubscriber<T> observer = TestSubscriber.create();
        put(PREFERENCE_KEY, newValue);


        observe(PREFERENCE_KEY, defaultValue)
                .subscribe(observer);

        observer.assertValue(newValue);
        observer.assertNoTerminalEvent();
    }

    @Test
    public void testRemovalEmitsDefaultValue() throws Exception {
        TestSubscriber<T> observer = TestSubscriber.create();

        observe(PREFERENCE_KEY, defaultValue)
                .subscribe(observer);

        gateway.remove(PREFERENCE_KEY);
        observer.assertValues(defaultValue, defaultValue);
        observer.assertNoTerminalEvent();
    }

    @Test
    public void testReturnsDefaultIfNotPresent() throws Exception {
        TestSubscriber<T> observer = TestSubscriber.create();

        observe(PREFERENCE_KEY, defaultValue)
                .subscribe(observer);

        observer.assertValue(defaultValue);
        observer.assertNoTerminalEvent();
    }

    @Test
    public void testPutProducesUpdate() throws Exception {
        TestSubscriber<T> observer = TestSubscriber.create();

        observe(PREFERENCE_KEY, defaultValue)
                .subscribe(observer);

        put(PREFERENCE_KEY, newValue);
        observer.assertValues(defaultValue, newValue);
        observer.assertNoTerminalEvent();
    }

    @Test
    public void testContains() throws Exception {
        TestSubscriber<Boolean> observer = TestSubscriber.create();

        gateway.contains(PREFERENCE_KEY)
               .subscribe(observer);

        observer.assertValue(false);
        observer.assertCompleted();
        observer.assertNoErrors();
    }

    @Test
    public void testObserve() throws Exception {
        TestSubscriber<T> observer = TestSubscriber.create();

        observe(PREFERENCE_KEY, defaultValue)
                .subscribe(observer);

        observer.assertValue(defaultValue);
        observer.assertNoTerminalEvent();
    }
}