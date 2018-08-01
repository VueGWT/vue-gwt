package com.axellience.vuegwt.processors.dom;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.propNameToAttributeName;

import elemental2.dom.Element;
import elemental2.dom.HTMLAnchorElement;
import elemental2.dom.HTMLAreaElement;
import elemental2.dom.HTMLAudioElement;
import elemental2.dom.HTMLBRElement;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.HTMLDataListElement;
import elemental2.dom.HTMLDetailsElement;
import elemental2.dom.HTMLDialogElement;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLEmbedElement;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.HTMLFormElement;
import elemental2.dom.HTMLHRElement;
import elemental2.dom.HTMLHeadingElement;
import elemental2.dom.HTMLImageElement;
import elemental2.dom.HTMLInputElement;
import elemental2.dom.HTMLLIElement;
import elemental2.dom.HTMLLabelElement;
import elemental2.dom.HTMLLegendElement;
import elemental2.dom.HTMLMapElement;
import elemental2.dom.HTMLMenuElement;
import elemental2.dom.HTMLMenuItemElement;
import elemental2.dom.HTMLMeterElement;
import elemental2.dom.HTMLOListElement;
import elemental2.dom.HTMLObjectElement;
import elemental2.dom.HTMLOptGroupElement;
import elemental2.dom.HTMLOptionElement;
import elemental2.dom.HTMLOutputElement;
import elemental2.dom.HTMLParagraphElement;
import elemental2.dom.HTMLParamElement;
import elemental2.dom.HTMLPreElement;
import elemental2.dom.HTMLProgressElement;
import elemental2.dom.HTMLQuoteElement;
import elemental2.dom.HTMLScriptElement;
import elemental2.dom.HTMLSelectElement;
import elemental2.dom.HTMLSourceElement;
import elemental2.dom.HTMLTableCaptionElement;
import elemental2.dom.HTMLTableCellElement;
import elemental2.dom.HTMLTableColElement;
import elemental2.dom.HTMLTableElement;
import elemental2.dom.HTMLTableRowElement;
import elemental2.dom.HTMLTextAreaElement;
import elemental2.dom.HTMLTrackElement;
import elemental2.dom.HTMLUListElement;
import elemental2.dom.HTMLVideoElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DOMElementsUtil {

  // Taken from Elemento: https://github.com/hal/elemento/blob/develop/template-processor/src/main/java/org/jboss/gwt/elemento/processor/TemplatedProcessor.java#L149
  // List of elements from https://developer.mozilla.org/en-US/docs/Web/HTML/Element
  // which have a class in elemental2.dom, are standardized and not obsolete or deprecated
  private static final Map<String, Class<? extends Element>> HTML_ELEMENTS = new HashMap<>();

  private static final Map<Class<? extends Element>, Map<String, Class<?>>> HTML_ELEMENTS_PROPERTIES_CACHE = new HashMap<>();

  static {
    HTML_ELEMENTS.put("a", HTMLAnchorElement.class);
    HTML_ELEMENTS.put("area", HTMLAreaElement.class);
    HTML_ELEMENTS.put("audio", HTMLAudioElement.class);
    HTML_ELEMENTS.put("blockquote", HTMLQuoteElement.class);
    HTML_ELEMENTS.put("br", HTMLBRElement.class);
    HTML_ELEMENTS.put("button", HTMLButtonElement.class);
    HTML_ELEMENTS.put("canvas", HTMLCanvasElement.class);
    HTML_ELEMENTS.put("caption", HTMLTableCaptionElement.class);
    HTML_ELEMENTS.put("col", HTMLTableColElement.class);
    HTML_ELEMENTS.put("datalist", HTMLDataListElement.class);
    HTML_ELEMENTS.put("details", HTMLDetailsElement.class);
    HTML_ELEMENTS.put("dialog", HTMLDialogElement.class);
    HTML_ELEMENTS.put("div", HTMLDivElement.class);
    HTML_ELEMENTS.put("embed", HTMLEmbedElement.class);
    HTML_ELEMENTS.put("fieldset", HTMLFieldSetElement.class);
    HTML_ELEMENTS.put("form", HTMLFormElement.class);
    HTML_ELEMENTS.put("h1", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("h2", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("h3", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("h4", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("h5", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("h6", HTMLHeadingElement.class);
    HTML_ELEMENTS.put("hr", HTMLHRElement.class);
    HTML_ELEMENTS.put("img", HTMLImageElement.class);
    HTML_ELEMENTS.put("input", HTMLInputElement.class);
    HTML_ELEMENTS.put("label", HTMLLabelElement.class);
    HTML_ELEMENTS.put("legend", HTMLLegendElement.class);
    HTML_ELEMENTS.put("li", HTMLLIElement.class);
    HTML_ELEMENTS.put("map", HTMLMapElement.class);
    HTML_ELEMENTS.put("menu", HTMLMenuElement.class);
    HTML_ELEMENTS.put("menuitem", HTMLMenuItemElement.class);
    HTML_ELEMENTS.put("meter", HTMLMeterElement.class);
    HTML_ELEMENTS.put("object", HTMLObjectElement.class);
    HTML_ELEMENTS.put("ol", HTMLOListElement.class);
    HTML_ELEMENTS.put("optgroup", HTMLOptGroupElement.class);
    HTML_ELEMENTS.put("option", HTMLOptionElement.class);
    HTML_ELEMENTS.put("output", HTMLOutputElement.class);
    HTML_ELEMENTS.put("p", HTMLParagraphElement.class);
    HTML_ELEMENTS.put("param", HTMLParamElement.class);
    HTML_ELEMENTS.put("pre", HTMLPreElement.class);
    HTML_ELEMENTS.put("progress", HTMLProgressElement.class);
    HTML_ELEMENTS.put("q", HTMLQuoteElement.class);
    HTML_ELEMENTS.put("script", HTMLScriptElement.class);
    HTML_ELEMENTS.put("select", HTMLSelectElement.class);
    HTML_ELEMENTS.put("source", HTMLSourceElement.class);
    HTML_ELEMENTS.put("table", HTMLTableElement.class);
    HTML_ELEMENTS.put("td", HTMLTableCellElement.class);
    HTML_ELEMENTS.put("textarea", HTMLTextAreaElement.class);
    HTML_ELEMENTS.put("tr", HTMLTableRowElement.class);
    HTML_ELEMENTS.put("track", HTMLTrackElement.class);
    HTML_ELEMENTS.put("ul", HTMLUListElement.class);
    HTML_ELEMENTS.put("video", HTMLVideoElement.class);
  }

  public static Optional<Class<? extends Element>> getTypeForElementTag(String elementTag) {
    return Optional.ofNullable(HTML_ELEMENTS.get(elementTag));
  }

  public static <T extends Element> Map<String, Class<?>> getElementProperties(
      Class<T> elementClass) {
    if (HTML_ELEMENTS_PROPERTIES_CACHE.containsKey(elementClass)) {
      return HTML_ELEMENTS_PROPERTIES_CACHE.get(elementClass);
    }

    Map<String, Class<?>> elementProperties = new HashMap<>();
    for (Field field : elementClass.getFields()) {
      if (!Modifier.isPublic(field.getModifiers())) {
        continue;
      }

      elementProperties.put(propNameToAttributeName(field.getName()), field.getType());
    }

    HTML_ELEMENTS_PROPERTIES_CACHE.put(elementClass, elementProperties);

    return elementProperties;
  }
}
