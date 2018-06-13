package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface MelisandreComponentClientBundle extends ClientBundle {

  MelisandreComponentClientBundle INSTANCE = GWT.create(MelisandreComponentClientBundle.class);

  @Source("MelisandreComponentStyle.gss")
  MelisandreComponentStyle melisandreComponentStyle();
}