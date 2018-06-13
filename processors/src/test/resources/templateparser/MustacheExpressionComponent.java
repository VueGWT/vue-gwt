package templateparser;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import common.SimpleObject;
import jsinterop.annotations.JsProperty;

@Component
public class MustacheExpressionComponent implements IsVueComponent {

  @JsProperty
  SimpleObject simpleObject;
}