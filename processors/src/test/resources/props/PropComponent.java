package props;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component(hasTemplate = false)
public class PropComponent implements IsVueComponent {

  @Prop
  int myProp;
}
