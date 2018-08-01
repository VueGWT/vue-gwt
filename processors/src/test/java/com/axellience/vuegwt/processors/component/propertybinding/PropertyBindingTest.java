package com.axellience.vuegwt.processors.component.propertybinding;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PropertyBindingTest {

  @Test
  @DisplayName("should enforce types for DOM element property binding")
  void propsAreRegisteredInOptionsCorrectly() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("propertybinding/PropertyBindingComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("propertybinding.PropertyBindingComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "propertybinding/compileresult/PropertyBindingComponentExposedType.java"));
  }
}