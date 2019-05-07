package com.axellience.vuegwt.processors.component.template.parser;

import java.net.URI;
import java.util.Optional;

import javax.annotation.processing.Messager;

import com.axellience.vuegwt.processors.component.template.parser.TemplateScopedCssParser.ScopedCssResult;
import com.axellience.vuegwt.processors.component.template.parser.context.StyleParserContext;
import com.axellience.vuegwt.processors.component.template.parser.result.StyleParserResult;
import com.axellience.vuegwt.processors.component.template.parser.result.TemplateParserResult;

import io.bit3.jsass.CompilationException;
import io.bit3.jsass.Compiler;
import io.bit3.jsass.Options;
import io.bit3.jsass.Output;
import io.bit3.jsass.context.StringContext;

public class StyleParser {
  private StyleParserContext context;
  private Messager messager;
  private StyleParserLogger logger;
  private StyleParserResult result;
  private URI htmlTemplateUri;

  /**
   * Parse a given HTML template and return the a result object containing the
   * expressions and a transformed HTML.
   *
   * @param styleTemplate The SCSS or CSS template to process, as a String
   * @param content
   * @param context       Context of the Component we are currently processing
   * @param messager      Used to report errors in template during Annotation
   *                      Processing
   * @return A {@link TemplateParserResult} containing the processed template and
   *         expressions
   */
  public StyleParserResult parseStyleTemplate(String type, String content, StyleParserContext context,
      Messager messager, URI htmlTemplateUri) {
    this.context = context;
    this.messager = messager;
    this.logger = new StyleParserLogger(context, messager);
    this.htmlTemplateUri = htmlTemplateUri;

    result = new StyleParserResult();
    result.setScopedCss(processScopedCss(type, content));

    return result;
  }

  private String processScopedCss(String type, String styleScoped) {
    String[] scopedCss = new String[1];

    String css = styleScoped.trim();
    if (!css.isEmpty()) {
      if (type.equals("scss")) { // [file].scss
        css = scssToCss(css);
      }
      TemplateScopedCssParser scopedCssParser = new TemplateScopedCssParser(messager);
      Optional<ScopedCssResult> scopedCssResult = scopedCssParser.parse(context.getComponentTypeElement(), css);
      if (scopedCssResult.isPresent()) {
        context.getMandatoryAttributes().putAll(scopedCssResult.get().mandatoryAttributes);
        scopedCss[0] = scopedCssResult.get().scopedCss;
      }
    }

    return scopedCss[0];
  }

  private String scssToCss(String scss) {
    Compiler compiler = new Compiler();
    Options options = new Options();

    try {
      StringContext context = new StringContext(scss, this.htmlTemplateUri, null/* outputPath */, options);
      Output output = compiler.compile(context);
      return output.getCss();
    } catch (CompilationException e) {
      logger.error("SCSS compile failed: " + e.getErrorText());
      throw new RuntimeException(e);
    }
  }
}
