package com.axellience.vuegwt.processors.component.template.parser;

import com.axellience.vuegwt.core.client.component.VueComponent;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSSelectorAttribute;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSSelectorMember;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.DefaultCSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import com.squareup.javapoet.ClassName;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.processing.Messager;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;

/**
 * Process scoped styles in HTML template for a given {@link VueComponent}.
 * <br> See <a href="https://vue-loader.vuejs.org/en/features/scoped-css.html">Vue Scoped CSS</a>
 * <br> Also see <a href="https://github.com/vuejs/vue-loader/issues/957">Scoped style leak</a>
 *
 * @author SlavaP
 */
public class TemplateScopedCssParser {

  private final Messager messager;

  public TemplateScopedCssParser(Messager messager) {
    this.messager = messager;
  }

  private static String md5(String s) {
    return Integer.toHexString(s.toString().hashCode());
  }

  private String getCssAsString(CascadingStyleSheet css) {
    CSSWriterSettings aSettings = new CSSWriterSettings(ECSSVersion.CSS30, true/*optimizedOutput*/);
    try {
      CSSWriter aWriter = new CSSWriter(aSettings);
      String rslt = aWriter.getCSSAsString(css);
      return rslt;
    } catch (Exception ex) {
      messager.printMessage(Kind.ERROR, ex.getMessage());
      return null;
    }
  }

  public static class ScopedCssResult {

    public final String scopedCss;
    public final Map<String, String> mandatoryAttributes;

    public ScopedCssResult(String scopedCss, Map<String, String> mandatoryAttributes) {
      this.scopedCss = scopedCss;
      this.mandatoryAttributes = mandatoryAttributes;
    }
  }

  public Optional<ScopedCssResult> parse(TypeElement componentTypeElement, String css) {
    if (css == null || css.isEmpty()) {
      return Optional.empty();
    }
    final ClassName componentTypeName = ClassName.get(componentTypeElement);
    final String md5 = md5(componentTypeName.toString());
    final String datav = "data-v-" + md5;
    // UTF-8 is the fallback if neither a BOM nor @charset rule is present
    final CascadingStyleSheet cssSheet = CSSReader
        .readFromString(css, StandardCharsets.UTF_8, ECSSVersion.CSS30);
    if (cssSheet == null) {
      return Optional.empty();
    }
    CSSVisitor.visitCSS(cssSheet, new DefaultCSSVisitor() {
      @Override
      public void onBeginStyleRule(@Nonnull final CSSStyleRule aStyleRule) {
        ICommonsList<CSSSelector> selectors = aStyleRule.getAllSelectors();
        if (selectors != null) {
          for (CSSSelector sel : selectors) {
            int vDataPosition = getVDataPosition(sel);
            sel.addMember(vDataPosition, new CSSSelectorAttribute("", datav));
          }
        }
      }
    });
    Map<String, String> mandatoryAttributes = new LinkedHashMap<>();
    mandatoryAttributes.put(datav, null);
    return Optional.of(new ScopedCssResult(getCssAsString(cssSheet), mandatoryAttributes));
  }

  private int getVDataPosition(CSSSelector selector) {
    if (selector.hasMembers()) {
      for (int i = selector.getMemberCount() - 1; i > 0; i--) {
        ICSSSelectorMember member = selector.getMemberAtIndex(i);
        if (!isPseudoElementOrPseudoClass(member)) {
          return i + 1;
        }
      }
    }

    return 1;
  }

  private boolean isPseudoElementOrPseudoClass(ICSSSelectorMember member) {
    return member != null && member.getAsCSSString().startsWith(":");
  }
}
