package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import common.SimpleChildComponent;
import elemental2.core.JsArray;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLInputElement;
import java.util.Set;
import jsinterop.annotations.JsProperty;

@Component(components = SimpleChildComponent.class)
public class ValidRefsComponent implements IsVueComponent {
  @Data
  @JsProperty
  Set<String> strings;

  @Ref
  HTMLDivElement divElement;

  @Ref
  JsArray<HTMLInputElement> inputs;

  @Ref
  SimpleChildComponent child;

  @Ref
  JsArray<SimpleChildComponent> children;
}
