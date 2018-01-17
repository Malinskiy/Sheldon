package com.malinskiy.sheldon.sample;

import com.malinskiy.sheldon.provider.SharedPreferencesGatewayBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import rx.Completable;
import rx.functions.Action0;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Temp_Preferences preferences = new Temp_Preferences(new SharedPreferencesGatewayBuilder(this));

        Log.d(MainActivity.class.getSimpleName(), "String value: " + preferences.getPolicyName().toBlocking().first());
        Log.d(MainActivity.class.getSimpleName(), "Enum value: " + preferences.getEnum().toBlocking().first());

        Completable.timer(10, TimeUnit.SECONDS).andThen(preferences.setEnum(Type.TWO)).subscribe(new Action0() {
            @Override public void call() {
                Log.e("Complete", "Complete");
            }
        }, new Action1<Throwable>() {
            @Override public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }
}
