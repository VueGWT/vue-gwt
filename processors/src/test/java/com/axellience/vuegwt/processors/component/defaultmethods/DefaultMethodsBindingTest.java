package com.axellience.vuegwt.processors.component.defaultmethods;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

class DefaultMethodsBindingTest {

  @Test
  @DisplayName("should bind component methods to default methods of interfaces of the option object")
  void bindComponentMethodsToOptions() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("defaultmethods/DefaultMethodsComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("defaultmethods.DefaultMethodsComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "defaultmethods/compileresult/DefaultMethodsComponentExposedType.java"));
  }
}