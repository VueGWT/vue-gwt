package com.axellience.vuegwtexamples.client.examples.example10;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * @author Adrien Baron
 */
public interface Example10ClientBundle extends ClientBundle
{
    Example10ClientBundle INSTANCE = GWT.create(Example10ClientBundle.class);

    @Source("example10.jpg")
    ImageResource myImage();
}