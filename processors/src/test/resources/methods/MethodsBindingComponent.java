package methods;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import common.SimpleObject;
import jsinterop.annotations.JsMethod;

@Component
public class MethodsBindingComponent implements IsVueComponent, HasCreated {

  @Data
  SimpleObject simpleObject;

  @JsMethod
  void onClickMethod() {

  }

  @Override
  public void created() {
    // Simple Hook
  }
}