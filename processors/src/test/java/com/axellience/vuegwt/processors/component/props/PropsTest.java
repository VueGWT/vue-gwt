package com.axellience.vuegwt.processors.component.props;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PropsTest {

  @Test
  @DisplayName("should throw when using @JsProperty with @Prop")
  void propsWithJsPropertyAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("props/PropWithJsPropertyComponent.java"));

    assertThat(compilation).hadErrorContaining("@Prop should not be annotated with @JsProperty anymore starting Beta 9 on field: \"myProp\".");
  }

  @Test
  @DisplayName("should register @Prop in options correctly")
  void propsAreRegisteredInOptionsCorrectly() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("props/PropComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("props.PropComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "props/compileresult/PropComponentExposedType.java"));
  }
}