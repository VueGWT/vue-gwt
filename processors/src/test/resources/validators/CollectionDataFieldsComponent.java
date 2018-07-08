package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import java.util.LinkedList;
import java.util.List;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithIgnoredTypes;

@Component(hasTemplate = false)
public class CollectionDataFieldsComponent implements IsVueComponent {

  @Data
  List<String> collectionWithMissingJsProperty;

  @Data
  @JsProperty
  LinkedList<String> collectionWithSpecificType;
}