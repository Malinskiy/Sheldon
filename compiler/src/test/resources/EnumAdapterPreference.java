package test;

import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Preferences;
import com.malinskiy.sheldon.annotation.Set;

import io.reactivex.Observable;

@Preferences(
        name = "test"
)
public interface EnumAdapterPreference {

    @Default(name = "enum") Type DEFAULT_ENUM = Type.THREE;

    @Get(name = "enum") Observable<Type> getEnum();

    @Set(name = "enum") void setEnum(Type value);

    enum Type {

        ONE,
        TWO,
        THREE
    }
}
