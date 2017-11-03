package com.malinskiy.sheldon2.codegen;

import com.google.common.io.Resources;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;


import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class PreferencesProcessorTest {

    @Test
    public void testBasicTypesWithDeleterAndContains() {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("valid/BasicTypesPreferences.java")))
                .processedWith(new PreferencesProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testWithCustomAdapters() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("valid/EnumAdapterPreference.java")))
                .processedWith(new PreferencesProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testWithoutDeleterOrContains() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("valid/PreferencesWithoutContainsOrDeleter.java")))
                .processedWith(new PreferencesProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testNoGetter() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("invalid/NoGetterPreference.java")))
                .processedWith(new PreferencesProcessor())
                .failsToCompile();
    }

    @Test
    public void testNoSetter() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("invalid/NoSetterPreference.java")))
                .processedWith(new PreferencesProcessor())
                .failsToCompile();
    }

    @Test
    public void testNoDefault() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("invalid/NoDefaultPreference.java")))
                .processedWith(new PreferencesProcessor())
                .failsToCompile();
    }

    @Test
    public void testClear() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource("valid/PreferencesWithClear.java"))
                .processedWith(new PreferencesProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(JavaFileObjects.forResource("expected/PreferencesWithClear.java"));
    }

    @Test
    public void testEnumWithCompletable() throws Exception {
        assert_().about(javaSource())
                .that(JavaFileObjects.forResource(Resources.getResource("valid/EnumWithCompletableAdapterPreference.java")))
                .processedWith(new PreferencesProcessor())
                .compilesWithoutError();

    }
}