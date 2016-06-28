package com.malinskiy.sheldon.sample;

import com.malinskiy.sheldon.AdapterRepository;
import com.malinskiy.sheldon.provider.SharedPreferencesGatewayBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Temp_Preferences preferences = new Temp_Preferences(new SharedPreferencesGatewayBuilder(this));

        Log.d(MainActivity.class.getSimpleName(), "String value: " + preferences.getPolicyName().toBlocking().first());
        Log.d(MainActivity.class.getSimpleName(), "Enum value: " + preferences.getEnum().toBlocking().first());
    }
}
