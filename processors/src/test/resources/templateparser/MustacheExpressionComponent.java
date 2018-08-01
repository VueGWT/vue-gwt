package templateparser;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import common.SimpleObject;

@Component
public class MustacheExpressionComponent implements IsVueComponent {

  @Data
  SimpleObject simpleObject;
}