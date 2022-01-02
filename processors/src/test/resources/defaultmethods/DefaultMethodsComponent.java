package defaultmethods;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import common.SimpleObjectInterface;
import jsinterop.annotations.JsMethod;

@Component
public class DefaultMethodsComponent implements IsVueComponent, HasCreated {

  @Data
  SimpleObjectInterface simpleObject;

  @JsMethod
  void onClickMethod() {

  }

  @Override
  public void created() {
    // Simple Hook
  }
}