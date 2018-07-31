package com.axellience.vuegwt.processors.component.vmodel;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VModelTest {

  @Test
  @DisplayName("should throw an error when using a v-model on a non existing field")
  void vmodelWithInvalidField() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("vmodel/VModelInvalidFieldComponent.java"));

    assertThat(compilation).hadErrorContaining(
        "Couldn't find @Data or @Computed for v-model \"nonExistingField\"");
  }

  @Test
  @DisplayName("should throw an error when using a v-model with dot notation")
  void vmodelWithDotNotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("vmodel/VModelDotNotationComponent.java"));

    assertThat(compilation).hadErrorContaining(
        "v-model doesn't support dot notation in Vue GWT: \"dot.notation\"");
  }
}