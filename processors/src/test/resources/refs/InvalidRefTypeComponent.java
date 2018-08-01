package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class InvalidRefTypeComponent implements IsVueComponent {

  @Ref
  String invalidRefType;
}
