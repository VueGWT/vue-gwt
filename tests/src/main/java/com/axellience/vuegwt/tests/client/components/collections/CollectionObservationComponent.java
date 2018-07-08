package com.axellience.vuegwt.tests.client.components.collections;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsProperty;

@Component
public class CollectionObservationComponent implements IsVueComponent {

  @Data
  @JsProperty
  List<String> arrayList = new ArrayList<>();

  @Data
  @JsProperty
  List<String> linkedList = new LinkedList<>();

  @Data
  @JsProperty
  Set<String> hashSet = new HashSet<>();

  @Data
  @JsProperty
  Map<String, String> hashMap = new HashMap<>();

  @JsMethod
  public void addToArrayList(String value) {
    arrayList.add(value);
  }

  @JsMethod
  public void addToLinkedList(String value) {
    linkedList.add(value);
  }

  @JsMethod
  public void addToHashSet(String value) {
    hashSet.add(value);
  }

  @JsMethod
  public void addToHashMap(String key, String value) {
    hashMap.put(key, value);
  }

  @JsMethod
  public void removeFromArrayList(String value) {
    arrayList.remove(value);
  }

  @JsMethod
  public void removeFromLinkedList(String value) {
    linkedList.remove(value);
  }

  @JsMethod
  public void removeFromHashSet(String value) {
    hashSet.remove(value);
  }

  @JsMethod
  public void removeFromHashMap(String key) {
    hashMap.remove(key);
  }
}
