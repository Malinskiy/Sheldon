package test;

import com.malinskiy.sheldon2.annotation.Default;
import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.annotation.Preferences;
import com.malinskiy.sheldon2.annotation.Set;

import io.reactivex.Observable;

@Preferences(
        name = "test"
)
public interface PreferencesWithoutContainsOrDeleter {

    @Default(name = "string") String DEFAULT_POLICY = "DEFAULT_STRING";

    @Get(name = "string") Observable<String> getString();

    @Set(name = "string") void setString(String value);


    @Default(name = "boolean") Boolean DEFAULT_BOOLEAN = false;

    @Get(name = "boolean") Observable<Boolean> getBoolean();

    @Set(name = "boolean") void setBoolean(Boolean value);


    @Default(name = "int") Integer DEFAULT_INT = 42;

    @Get(name = "int") Observable<Integer> getInteger();

    @Set(name = "int") void setInteger(Integer value);


    @Default(name = "long") Long DEFAULT_LONG = 42L;

    @Get(name = "long") Observable<Long> getLong();

    @Set(name = "long") void setLong(Long value);


    @Default(name = "float") Float DEFAULT_FLOAT = 0.42f;

    @Get(name = "float") Observable<Float> getFloat();

    @Set(name = "float") void setFloat(Float value);
}
