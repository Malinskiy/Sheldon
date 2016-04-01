package test;

import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Preferences;

import rx.Observable;

@Preferences(
        name = "test"
)
public interface NoSetterPreference {

    @Default(name = "string") String DEFAULT_POLICY = "DEFAULT_STRING";

    @Get(name = "string") Observable<String> getString();

}
