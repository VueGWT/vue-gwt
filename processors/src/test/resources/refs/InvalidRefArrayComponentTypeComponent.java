package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import common.SimpleChildComponent;
import common.SimpleOtherChildComponent;
import elemental2.core.JsArray;
import java.util.Set;
import jsinterop.annotations.JsProperty;

@Component(components = SimpleChildComponent.class)
public class InvalidRefArrayComponentTypeComponent implements IsVueComponent {

  @Data
  @JsProperty
  Set<String> strings;

  @Ref
  JsArray<SimpleOtherChildComponent> invalidRefArrayType;
}