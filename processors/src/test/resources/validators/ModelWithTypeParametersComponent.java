package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithTypeParameters;

@Component(hasTemplate = false)
public class ModelWithTypeParametersComponent implements IsVueComponent {

  @JsProperty
  ModelWithTypeParameters modelWithTypeParameters;
}