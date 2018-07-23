package validators;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import java.util.LinkedList;
import java.util.List;
import jsinterop.annotations.JsProperty;
import validators.models.ModelWithIgnoredTypes;

@Component(hasTemplate = false)
public class CollectionPropFieldsComponent implements IsVueComponent {

  @Prop
  List<String> collectionWithMissingJsProperty;

  @Prop
  @JsProperty
  LinkedList<String> collectionWithSpecificType;
}