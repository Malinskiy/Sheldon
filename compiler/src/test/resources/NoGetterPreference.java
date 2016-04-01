package test;

import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Preferences;
import com.malinskiy.sheldon.annotation.Set;

@Preferences(
        name = "test"
)
public interface NoGetterPreference {

    @Default(name = "string") String DEFAULT_POLICY = "DEFAULT_STRING";

    @Set(name = "string") void setString(String value);

}
