package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithListNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithListNoAnnotationComponent implements IsVueComponent {
  @JsProperty
  ModelWithListNoAnnotation modelWithListNoAnnotation;
}