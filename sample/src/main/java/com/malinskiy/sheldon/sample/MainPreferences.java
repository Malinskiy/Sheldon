package com.malinskiy.sheldon.sample;

import com.malinskiy.sheldon.annotation.Contains;
import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Delete;
import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Preferences;
import com.malinskiy.sheldon.annotation.Set;

import rx.Observable;

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
