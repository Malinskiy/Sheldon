package com.malinskiy.sheldon2;

import com.malinskiy.sheldon2.adapter.IPreferenceAdapter;

public interface IAdapterProvider {
    <T> IPreferenceAdapter<T> get(Class<T> clazz);
}