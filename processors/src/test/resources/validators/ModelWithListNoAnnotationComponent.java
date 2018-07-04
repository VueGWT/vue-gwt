package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Data;
import validators.models.ModelWithListNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithListNoAnnotationComponent implements IsVueComponent {

  @Data
  ModelWithListNoAnnotation modelWithListNoAnnotation;
}