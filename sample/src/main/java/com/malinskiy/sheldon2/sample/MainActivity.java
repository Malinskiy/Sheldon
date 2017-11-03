package com.malinskiy.sheldon2.sample;

import com.malinskiy.sheldon2.provider.SharedPreferencesGatewayBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Temp_Preferences preferences = new Temp_Preferences(new SharedPreferencesGatewayBuilder(this));

        Log.d(MainActivity.class.getSimpleName(), "String value: " + preferences.getPolicyName().blockingFirst());
        Log.d(MainActivity.class.getSimpleName(), "Enum value: " + preferences.getEnum().blockingFirst());

        Completable.timer(5, TimeUnit.SECONDS).andThen(preferences.setEnum(Type.TWO))
                .subscribe(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d(MainActivity.class.getSimpleName(), "Complete");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(MainActivity.class.getSimpleName(), "Error", throwable);
                    }
                });
    }
}
