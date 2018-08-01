package com.axellience.vuegwt.processors.component.methods;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MethodsBindingTest {

  @Test
  @DisplayName("should bind component methods to the option object")
  void bindComponentMethodsToOptions() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("methods/MethodsBindingComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("methods.MethodsBindingComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "methods/compileresult/MethodsBindingComponentExposedType.java"));
  }
}