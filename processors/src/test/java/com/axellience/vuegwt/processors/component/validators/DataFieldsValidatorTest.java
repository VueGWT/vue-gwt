package com.axellience.vuegwt.processors.component.validators;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DataFieldsValidatorTest {

  @Test
  @DisplayName("should throw a warning when List missing @JsProperty in the data model")
  void modelWithListNoAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithListNoAnnotationComponent.java"));

    assertThat(compilation).hadWarningContaining("Collection with missing @JsProperty");
  }

  @Test
  @DisplayName("should throw a warning when Set missing @JsProperty in the data model")
  void modelWithSetNoAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithSetNoAnnotationComponent.java"));

    assertThat(compilation).hadWarningContaining("Collection with missing @JsProperty");
  }

  @Test
  @DisplayName("should throw a warning when Map missing @JsProperty in the data model")
  void modelWithMapNoAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithMapNoAnnotationComponent.java"));

    assertThat(compilation).hadWarningContaining("Collection with missing @JsProperty");
  }

  @Test
  @DisplayName("should throw a warning when using specific List implementation in the data model, even with @JsProperty Annotation")
  void modelWithLinkedListWithAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithLinkedListWithAnnotationComponent.java"));

    assertThat(compilation).hadWarningContaining("Specific Collection type used as Property type, you should use either Map, Set or List");
  }

  @Test
  @DisplayName("should not throw a warning on Java Lang types")
  void modelWithIgnoredTypes() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithIgnoredTypesComponent.java"));

    assertThat(compilation).hadWarningCount(1);
  }

  @Test
  @DisplayName("should validate type parameters fields")
  void modelWithTypeParameters() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/ModelWithTypeParametersComponent.java"));

    assertThat(compilation).hadWarningContaining("Collection with missing @JsProperty");
    System.out.println(compilation.errors());
  }
  @Test
  @DisplayName("should not throw a warning when using @SuppressWarnings")
  void ignoreListWithNoAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(JavaFileObjects.forResource("validators/IgnoreModelWithListNoAnnotationComponent.java"));

    assertThat(compilation).hadWarningCount(1);
  }
}