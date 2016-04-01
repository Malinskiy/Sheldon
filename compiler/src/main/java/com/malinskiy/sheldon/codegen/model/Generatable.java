package com.malinskiy.sheldon.codegen.model;

import com.malinskiy.sheldon.codegen.ProcessingException;
import com.squareup.javapoet.TypeSpec;

public interface Generatable {

    void generateCode(TypeSpec.Builder builder) throws ProcessingException;

}
