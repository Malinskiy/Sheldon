package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.BuildConfig;
import com.malinskiy.sheldon.IGateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.SharedPreferences;

import rx.Subscription;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
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
        Subscription subscription = gateway.observeBoolean("prefence", false)
                                           .subscribe();
        subscription.unsubscribe();

        verify(sharedPreferences).unregisterOnSharedPreferenceChangeListener(
                any(SharedPreferences.OnSharedPreferenceChangeListener.class));
    }
}
