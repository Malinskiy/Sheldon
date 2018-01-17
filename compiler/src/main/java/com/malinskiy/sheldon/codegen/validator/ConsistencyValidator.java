package com.malinskiy.sheldon.codegen.validator;

import com.google.common.collect.HashMultiset;

import com.malinskiy.sheldon.codegen.ProcessingException;

import java.util.Set;

import javax.annotation.Nonnull;

public class ConsistencyValidator {

    public static void checkConsistency(@Nonnull Set<String> defaults,
                                        @Nonnull Set<String> getters,
                                        @Nonnull Set<String> setters) throws ProcessingException {

        if (!HashMultiset.create(setters).equals(HashMultiset.create(getters)) ||
                !HashMultiset.create(setters).equals(HashMultiset.create(defaults))) {

            throw new ProcessingException(null, "Incomplete interface definition (missing either getter, setter or " +
                    "default)");
        }
    }
}
