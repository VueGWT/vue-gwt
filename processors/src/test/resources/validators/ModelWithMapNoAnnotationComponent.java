package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithMapNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithMapNoAnnotationComponent implements IsVueComponent {

  @JsProperty
  ModelWithMapNoAnnotation modelWithMapNoAnnotation;
}