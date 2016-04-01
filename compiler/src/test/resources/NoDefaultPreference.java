package test;

import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Preferences;
import com.malinskiy.sheldon.annotation.Set;

import rx.Observable;

@Preferences(
        name = "test"
)
public interface NoDefaultPreference {

    @Get(name = "string") Observable<String> getString();

    @Set(name = "string") void setString(String value);

}
