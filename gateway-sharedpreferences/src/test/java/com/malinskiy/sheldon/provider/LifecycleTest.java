package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.BuildConfig;
import com.malinskiy.sheldon.IGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.SharedPreferences;

import io.reactivex.disposables.Disposable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LifecycleTest {

    @Mock SharedPreferences sharedPreferences;

    private IGateway gateway;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        gateway = new SharedPreferencesGateway(sharedPreferences);
    }

    @Test
    public void testCreationTriggersRegister() throws Exception {
        gateway.observeBoolean("preference", false)
               .subscribe();

        verify(sharedPreferences).registerOnSharedPreferenceChangeListener(
                any(SharedPreferences.OnSharedPreferenceChangeListener.class));
    }

    @Test
    public void testUnsubscribeTriggersUnregister() throws Exception {
        Disposable disposable = gateway.observeBoolean("prefence", false)
                                       .subscribe();
        disposable.dispose();

        verify(sharedPreferences).unregisterOnSharedPreferenceChangeListener(
                any(SharedPreferences.OnSharedPreferenceChangeListener.class));
    }
}
