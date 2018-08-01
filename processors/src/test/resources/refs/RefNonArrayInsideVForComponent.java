package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.dom.HTMLDivElement;
import java.util.Set;
import jsinterop.annotations.JsProperty;

@Component
public class RefNonArrayInsideVForComponent implements IsVueComponent {

  @Data
  @JsProperty
  Set<String> strings;

  @Ref
  HTMLDivElement ref;
}
