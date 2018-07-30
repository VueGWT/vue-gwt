package com.axellience.vuegwt.processors.component.refs;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;
import static com.google.testing.compile.JavaFileObjects.forResource;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RefsTest {

  @Test
  @DisplayName("should check that @Ref exist in template")
  void shouldCheckRefExistence() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/NonExistingRefComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Couldn't find ref attribute in template for @Ref field \"nonExistingRef\"");
  }

  @Test
  @DisplayName("should error when using a JsArray @Ref outside a v-for")
  void jsArrayOutsideVFor() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/RefArrayOutsideVForComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "@Ref field \"refArray\", must not be a JsArray as the ref is not used inside a v-for.");
  }

  @Test
  @DisplayName("should error when using a non JsArray @Ref inside a v-for")
  void nonJsArrayInsideVFor() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/RefNonArrayInsideVForComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "@Ref field \"ref\", must be a JsArray as the ref is used inside a v-for.");
  }

  @Test
  @DisplayName("should check @Ref is an Element or a IsVueComponent")
  void shouldCheckRefType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref \"invalidRefType\", type must extend elemental2.Element or IsVueComponent.");
  }

  @Test
  @DisplayName("should check @Ref is a JsArray<? extends Element> or a JsArray<? extends IsVueComponent>")
  void shouldCheckRefArrayType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefArrayTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref array \"invalidRefArrayType\", must be a JsArray of a type extending elemental2.Element or IsVueComponent.");
  }

  @Test
  @DisplayName("should check ref on local component is assignable to the @Ref field")
  void shouldCheckLocalComponentType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefComponentTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref \"invalidRefType\", must be able to assign \"common.SimpleChildComponent\".");
  }

  @Test
  @DisplayName("should check ref on local component is assignable to the @Ref JsArray field")
  void shouldCheckJsArrayLocalComponentType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefArrayComponentTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref \"invalidRefArrayType\", must be able to assign \"common.SimpleChildComponent\".");
  }

  @Test
  @DisplayName("should check ref on DOM Element is assignable to the @Ref field")
  void shouldCheckDOMElementType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefDOMElementTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref \"invalidRefType\", must be able to assign \"elemental2.dom.HTMLInputElement\".");
  }

  @Test
  @DisplayName("should check ref on DOM Element is assignable to the @Ref JsArray field")
  void shouldCheckJsArrayDOMElementType() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("refs/InvalidRefArrayDOMElementTypeComponent.java"));

    assertThat(compilation)
        .hadErrorContaining(
            "Invalid type for @Ref \"invalidRefArrayType\", must be able to assign \"elemental2.dom.HTMLDivElement\".");
  }

  @Test
  @DisplayName("should not throw exceptions on valid refs")
  void shouldNotThrowOnValidRefs() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(forResource("common/SimpleChildComponent.java"),
                forResource("refs/ValidRefsComponent.java"));

    assertThat(compilation).hadErrorCount(0);
  }
}