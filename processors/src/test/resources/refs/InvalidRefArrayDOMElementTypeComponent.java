package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import elemental2.core.JsArray;
import elemental2.dom.HTMLInputElement;
import java.util.Set;
import jsinterop.annotations.JsProperty;

@Component
public class InvalidRefArrayDOMElementTypeComponent implements IsVueComponent {

  @Data
  @JsProperty
  Set<String> strings;

  @Ref
  JsArray<HTMLInputElement> invalidRefArrayType;
}