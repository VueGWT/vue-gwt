package methods;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import common.SimpleObject;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class MethodsBindingComponent implements IsVueComponent, HasCreated {

  @JsProperty
  SimpleObject simpleObject;

  @JsMethod
  void onClickMethod() {

  }

  @Override
  public void created() {
    // Simple Hook
  }
}