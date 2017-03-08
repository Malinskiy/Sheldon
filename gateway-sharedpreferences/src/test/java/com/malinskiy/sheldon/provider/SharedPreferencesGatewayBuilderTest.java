package com.malinskiy.sheldon.provider;

import com.malinskiy.sheldon.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;

import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricTestRunner.class)
public class SharedPreferencesGatewayBuilderTest {

    @Mock Context context;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithMode() throws Exception {
        SharedPreferencesGatewayBuilder builder = new SharedPreferencesGatewayBuilder(context);

        builder.mode(Context.MODE_WORLD_READABLE)
               .namespace("test")
               .build();

        verify(context).getSharedPreferences("test", Context.MODE_WORLD_READABLE);
    }
}