package validators.models;

import java.util.List;
import jsinterop.annotations.JsProperty;

public class ModelWithTypeParameters {

  @JsProperty
  List<ModelWithListNoAnnotation> modelWithListNoAnnotationList;
}