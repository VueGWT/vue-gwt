package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Data;
import validators.models.ModelWithLinkedListWithAnnotation;

@Component(hasTemplate = false)
public class ModelWithLinkedListWithAnnotationComponent implements IsVueComponent {

  @Data
  ModelWithLinkedListWithAnnotation modelWithLinkedListWithAnnotation;
}