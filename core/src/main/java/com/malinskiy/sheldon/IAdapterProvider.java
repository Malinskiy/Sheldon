package com.malinskiy.sheldon;

import com.malinskiy.sheldon.adapter.IPreferenceAdapter;

public interface IAdapterProvider {
    <T> IPreferenceAdapter<T> get(Class<T> clazz);
}