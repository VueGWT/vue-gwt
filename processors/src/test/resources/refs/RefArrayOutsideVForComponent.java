package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.core.JsArray;
import elemental2.dom.HTMLDivElement;

@Component
public class RefArrayOutsideVForComponent implements IsVueComponent {

  @Ref
  JsArray<HTMLDivElement> refArray;
}
