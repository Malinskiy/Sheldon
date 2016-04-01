package com.malinskiy.sheldon;

public interface IAdapterProvider {

    <T> com.malinskiy.sheldon.adapter.IPreferenceAdapter<T> get(Class<T> clazz);

}
