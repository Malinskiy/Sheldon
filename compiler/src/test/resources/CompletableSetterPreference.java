package test;

import com.malinskiy.sheldon.annotation.Default;
import com.malinskiy.sheldon.annotation.Get;
import com.malinskiy.sheldon.annotation.Preferences;

import com.malinskiy.sheldon.annotation.Set;
import rx.Completable;
import rx.Observable;

@Preferences(
		name = "test"
)
public interface CompletableSetterPreference {

	@Default(name = "string")
	String DEFAULT_POLICY = "DEFAULT_STRING";

	@Get(name = "string")
	Observable<String> getString();

	@Set(name = "string")
	Completable putString(String str);

}
