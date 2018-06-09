package validators.models;

import jsinterop.annotations.JsProperty;
import java.util.List;

public class ModelWithTypeParameters {
  @JsProperty
  List<ModelWithListNoAnnotation> modelWithListNoAnnotationList;
}