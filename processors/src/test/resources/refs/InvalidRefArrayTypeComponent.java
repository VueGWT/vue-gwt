package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.core.JsArray;
import java.util.Set;
import jsinterop.annotations.JsProperty;

@Component
public class InvalidRefArrayTypeComponent implements IsVueComponent {

  @Data
  @JsProperty
  Set<String> strings;

  @Ref
  JsArray<String> invalidRefArrayType;
}
