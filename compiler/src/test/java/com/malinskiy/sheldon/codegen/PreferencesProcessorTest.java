package com.malinskiy.sheldon.codegen;


import com.google.common.io.Resources;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class PreferencesProcessorTest {

    @Test
    public void testBasicTypesWithDeleterAndContains() {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("BasicTypesPreferences.java")))
                 .processedWith(new PreferencesProcessor())
                 .compilesWithoutError();
    }

    @Test
    public void testWithCustomAdapters() throws Exception {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("EnumAdapterPreference.java")))
                 .processedWith(new PreferencesProcessor())
                 .compilesWithoutError();
    }

    @Test
    public void testWithoutDeleterOrContains() throws Exception {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("PreferencesWithoutContainsOrDeleter.java")))
                 .processedWith(new PreferencesProcessor())
                 .compilesWithoutError();
    }

    @Test
    public void testNoGetter() throws Exception {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("NoGetterPreference.java")))
                 .processedWith(new PreferencesProcessor())
                 .failsToCompile();
    }

    @Test
    public void testNoSetter() throws Exception {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("NoSetterPreference.java")))
                 .processedWith(new PreferencesProcessor())
                 .failsToCompile();
    }

    @Test
    public void testNoDefault() throws Exception {
        assert_().about(javaSource())
                 .that(JavaFileObjects.forResource(Resources.getResource("NoDefaultPreference.java")))
                 .processedWith(new PreferencesProcessor())
                 .failsToCompile();
    }
}