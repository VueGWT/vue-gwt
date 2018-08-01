package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import common.SimpleChildComponent;
import common.SimpleOtherChildComponent;
import elemental2.dom.HTMLDivElement;

@Component
public class InvalidRefDOMElementTypeComponent implements IsVueComponent {

  @Ref
  HTMLDivElement invalidRefType;
}
