package test;

import com.malinskiy.sheldon2.annotation.Default;
import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.annotation.Preferences;

import io.reactivex.Observable;

@Preferences(
        name = "test"
)
public interface NoSetterPreference {

    @Default(name = "string") String DEFAULT_POLICY = "DEFAULT_STRING";

    @Get(name = "string") Observable<String> getString();

}
