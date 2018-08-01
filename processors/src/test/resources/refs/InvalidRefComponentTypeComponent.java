package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import common.SimpleChildComponent;
import common.SimpleOtherChildComponent;

@Component(components = SimpleChildComponent.class)
public class InvalidRefComponentTypeComponent implements IsVueComponent {

  @Ref
  SimpleOtherChildComponent invalidRefType;
}
