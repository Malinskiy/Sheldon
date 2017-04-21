package test;

import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.annotation.Preferences;
import com.malinskiy.sheldon2.annotation.Set;

import io.reactivex.Observable;

@Preferences(
        name = "test"
)
public interface NoDefaultPreference {

    @Get(name = "string") Observable<String> getString();

    @Set(name = "string") void setString(String value);

}
