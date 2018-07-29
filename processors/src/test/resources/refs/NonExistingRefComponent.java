package refs;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Ref;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class NonExistingRefComponent implements IsVueComponent {

  @Ref
  String nonExistingRef;
}
