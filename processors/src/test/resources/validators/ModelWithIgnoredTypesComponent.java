package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import validators.models.ModelWithIgnoredTypes;

@Component(hasTemplate = false)
public class ModelWithIgnoredTypesComponent implements IsVueComponent {

  @Data
  ModelWithIgnoredTypes modelWithIgnoredTypes;
}