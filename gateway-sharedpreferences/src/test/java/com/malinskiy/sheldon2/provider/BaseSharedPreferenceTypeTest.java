package com.malinskiy.sheldon2.provider;

import com.malinskiy.sheldon2.BuildConfig;
import com.malinskiy.sheldon2.IGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.app.Application;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;


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

        put(PREFERENCE_KEY, newValue);
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        observer.assertValue(newValue);
        observer.assertNotTerminated();
    }

    @Test
    public void testRemovalEmitsDefaultValue() throws Exception {
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        gateway.remove(PREFERENCE_KEY);
        observer.assertValues(defaultValue, defaultValue);
        observer.assertNotTerminated();
    }

    @Test
    public void testReturnsDefaultIfNotPresent() throws Exception {
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        observer.assertValue(defaultValue);
        observer.assertNotTerminated();
    }

    @Test
    public void testPutProducesUpdate() throws Exception {
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        put(PREFERENCE_KEY, newValue);
        observer.assertValues(defaultValue, newValue);
        observer.assertNotTerminated();
    }

    @Test
    public void testContains() throws Exception {
        TestObserver<Boolean> observer = gateway.contains(PREFERENCE_KEY).test();

        observer.assertValue(false);
        observer.assertComplete();
        observer.assertNoErrors();
    }

    @Test
    public void testObserve() throws Exception {
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        observer.assertValue(defaultValue);
        observer.assertNotTerminated();
    }

    @Test
    public void testClear()  throws Exception{
        put(PREFERENCE_KEY, newValue);
        TestObserver<T> observer = observe(PREFERENCE_KEY, defaultValue).test();

        observer.assertValue(newValue);
        observer.assertNotTerminated();

        gateway.clear();

        observer = observe(PREFERENCE_KEY, defaultValue).test();

        observer.assertValue(defaultValue);
        observer.assertNotTerminated();
    }
}