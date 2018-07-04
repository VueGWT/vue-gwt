package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Data;
import validators.models.ModelWithMapNoAnnotation;

@Component(hasTemplate = false)
public class ModelWithMapNoAnnotationComponent implements IsVueComponent {

  @Data
  ModelWithMapNoAnnotation modelWithMapNoAnnotation;
}