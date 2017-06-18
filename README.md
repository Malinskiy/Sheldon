[![Build Status](https://travis-ci.org/Malinskiy/Sheldon.svg?branch=master)](https://travis-ci.org/Malinskiy/Sheldon) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Sheldon-green.svg?style=true)](https://android-arsenal.com/details/1/3378) [![Join the chat at https://gitter.im/Malinskiy/Sheldon](https://badges.gitter.im/Malinskiy/Sheldon.svg)](https://gitter.im/Malinskiy/Sheldon?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
![Maven Central](https://img.shields.io/maven-central/v/com.malinskiy.sheldon2/compiler.svg)

# Sheldon
Key concepts:

* Preferences **should not be tied to concrete implementation**, they should be an interface 
 that you can easily mock in your tests
* You should be able to provide **implementation gateway** in your platform layer
* Get your values through **RxJava's Observable**
* Get rid of always specifying a string key when you get or set preference, this should be **strongly typed**
 so that your IDE can help with autocompletion and your key<->value pair is always in sync
* Leverage annotation processing to **automatically generate the wiring code** between your preferences and platform gateway 
 
# Define your interface:

```java
@Preferences(name = "temp")
public interface SomePreferences {
    @Default(name = "name") String DEFAULT_VALUE = "DEFAULT_VALUE";
    @Get(name = "name") Observable<String> getValueName();
    @Set(name = "name") void setValue(String newValue);
    ...
    @Contains Observable<Boolean> contains(String key);
    @Delete void remove(String key);
}
```

# Adapters
Natively supported types are *Boolean*, *Float*, *Integer*, *Long* and *String*.
 
For everything else you have to extend **IPreferenceAdapter** and implement storing your
entity via supported types. You can look at example of generic **EnumAdapter**.
To include your adapter into processing just annotate it with **@Adapter**.

**Each adapter should have a no-args constructor**

# Defaults
For each parameter you have to **always supply a default value** as a field in an interface annotated with **@Default**.
Type of parameter should be the same as in getter and setter.

# Instantiate the generated implementation
```java
Temp_Preferences preferences = new Temp_Preferences(new SharedPreferencesGatewayBuilder(contextRef));
```

Probably you'd store this in your dependency graph and inject it by interface
For additional info look at the sample

## Get it

Gradle:
```groovy
repositories {
    ...
    mavenCentral()
    ...
}
...
dependencies {
    ...
    apt "com.malinskiy.sheldon2:compiler:$version"
    compile "com.malinskiy.sheldon2:core:$version"
    compile "com.malinskiy.sheldon2:gateway-sharedpreferences:$version"
    ...
}
```

## License

```
Copyright 2017 Anton Malinskiy

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
    
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and   limitations under the License.
```
