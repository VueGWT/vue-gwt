package com.axellience.vuegwtexamples.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface JsFilesResources extends ClientBundle {

  JsFilesResources INSTANCE = GWT.create(JsFilesResources.class);

  @Source("js-components.js")
  TextResource jsComponents();
}
