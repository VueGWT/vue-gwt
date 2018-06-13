package com.axellience.vuegwtexamples.client.examples.kitten;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Adrien Baron
 */
public interface KittenClientBundle extends ClientBundle {

  KittenClientBundle INSTANCE = GWT.create(KittenClientBundle.class);

  @Source("kitten.jpg")
  ImageResource myKitten();
}