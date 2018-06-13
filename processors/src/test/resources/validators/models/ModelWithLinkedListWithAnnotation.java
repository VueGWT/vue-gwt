package validators.models;

import common.SimpleObject;
import java.util.LinkedList;
import jsinterop.annotations.JsProperty;

public class ModelWithLinkedListWithAnnotation {

  @JsProperty
  LinkedList<SimpleObject> list;
}