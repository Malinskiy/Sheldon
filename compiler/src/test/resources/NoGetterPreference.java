package test;

import com.malinskiy.sheldon2.annotation.Default;
import com.malinskiy.sheldon2.annotation.Preferences;
import com.malinskiy.sheldon2.annotation.Set;

@Preferences(
        name = "test"
)
public interface NoGetterPreference {

    @Default(name = "string") String DEFAULT_POLICY = "DEFAULT_STRING";

    @Set(name = "string") void setString(String value);

}
