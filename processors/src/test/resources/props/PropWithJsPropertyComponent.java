package props;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

@Component(hasTemplate = false)
public class PropWithJsPropertyComponent implements IsVueComponent {

  @JsProperty
  @Prop
  int myProp;
}
