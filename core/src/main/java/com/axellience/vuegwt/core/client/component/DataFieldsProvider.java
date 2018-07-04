package com.axellience.vuegwt.core.client.component;

import static jsinterop.base.Js.asAny;
import static jsinterop.base.Js.cast;
import static jsinterop.base.Js.isTripleEqual;

import elemental2.core.JsObject;
import java.util.HashSet;
import java.util.Set;
import jsinterop.base.Any;
import jsinterop.base.JsPropertyMap;

public interface DataFieldsProvider {

  void vuegwt$markDataFields();

  /**
   * Determine all the data fields of the object by using vuegwt$markDataFields.
   *
   * @return The list of names of the fields
   */
  default Set<String> vuegwt$getDataFieldsName() {
    JsPropertyMap<Object> map = cast(this);
    JsObject jsObject = cast(this);
    map.forEach(key -> {
      if (!jsObject.hasOwnProperty(key))
        return;

      Any val = asAny(map.get(key));
      if (isTripleEqual(val, null) ||
          isTripleEqual(val, asAny(0)) ||
          isTripleEqual(val, asAny(false))) {
        map.delete(key);
      }
    });

    vuegwt$markDataFields();

    Set<String> dataFields = new HashSet<>();
    map.forEach(key -> {
      if (!jsObject.hasOwnProperty(key))
        return;

      Any val = asAny(map.get(key));
      if (isTripleEqual(val, null) ||
          isTripleEqual(val, asAny(0)) ||
          isTripleEqual(val, asAny(false))) {
        dataFields.add(key);
      }
    });

    return dataFields;
  }
}
