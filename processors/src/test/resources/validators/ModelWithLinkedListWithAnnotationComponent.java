package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithLinkedListWithAnnotation;

@Component(hasTemplate = false)
public class ModelWithLinkedListWithAnnotationComponent implements IsVueComponent {

  @JsProperty
  ModelWithLinkedListWithAnnotation modelWithLinkedListWithAnnotation;
}