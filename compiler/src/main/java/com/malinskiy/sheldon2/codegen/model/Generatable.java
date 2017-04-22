package com.malinskiy.sheldon2.codegen.model;

import com.malinskiy.sheldon2.codegen.ProcessingException;
import com.squareup.javapoet.TypeSpec;

public interface Generatable {

    void generateCode(TypeSpec.Builder builder) throws ProcessingException;

}
