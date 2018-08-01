package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Data;
import validators.models.ModelWithTypeParameters;

@Component(hasTemplate = false)
public class ModelWithTypeParametersComponent implements IsVueComponent {

  @Data
  ModelWithTypeParameters modelWithTypeParameters;
}