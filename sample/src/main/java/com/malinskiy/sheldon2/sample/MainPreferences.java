package com.malinskiy.sheldon2.sample;

import com.malinskiy.sheldon2.annotation.Contains;
import com.malinskiy.sheldon2.annotation.Default;
import com.malinskiy.sheldon2.annotation.Delete;
import com.malinskiy.sheldon2.annotation.Get;
import com.malinskiy.sheldon2.annotation.Preferences;
import com.malinskiy.sheldon2.annotation.Set;

import io.reactivex.Observable;

@Preferences(
        name = "temp"
)
public interface MainPreferences {

    @Default(name = "name") String DEFAULT_POLICY = "DEFAULT_NAME";

    @Get(name = "name") Observable<String> getPolicyName();

    @Set(name = "name") void setPolicyName(String name);


    @Default(name = "enum") Type DEFAULT_ENUM = Type.THREE;

    @Get(name = "enum") Observable<Type> getEnum();

    @Set(name = "enum") void setEnum(Type value);

    @Contains Observable<Boolean> contains(String key);

    @Delete void remove(String key);
}
