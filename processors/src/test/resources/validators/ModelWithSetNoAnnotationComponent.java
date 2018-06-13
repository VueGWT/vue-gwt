package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithSetNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithSetNoAnnotationComponent implements IsVueComponent {

  @JsProperty
  ModelWithSetNoAnnotation modelWithSetNoAnnotation;
}