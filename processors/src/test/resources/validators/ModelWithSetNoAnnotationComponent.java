package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Data;
import validators.models.ModelWithSetNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithSetNoAnnotationComponent implements IsVueComponent {

  @Data
  ModelWithSetNoAnnotation modelWithSetNoAnnotation;
}