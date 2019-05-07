package com.axellience.vuegwt.processors.component.template.parser;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.axellience.vuegwt.processors.VueGwtProcessor;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TemplateParserTest {

  @Test
  @DisplayName("should compile a template expression with linebreaks inside")
  void modelWithListNoAnnotation() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("templateparser/MustacheExpressionComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("templateparser.MustacheExpressionComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "templateparser/compileresult/MustacheExpressionComponentExposedType.java"));
  }

  @Test
  @DisplayName("should compile an external stylesheet")
  void componentWithExternalStylesheet() {
    Compilation compilation =
        javac()
            .withProcessors(new VueGwtProcessor())
            .compile(
                JavaFileObjects.forResource("templateparser/StyledComponent.java"));

    assertThat(compilation)
        .generatedSourceFile("templateparser.StyledComponentExposedType")
        .containsElementsIn(JavaFileObjects.forResource(
            "templateparser/compileresult/StyledComponentExposedType.java"));
  }
}