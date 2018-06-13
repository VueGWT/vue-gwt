package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(components = TreeFolderContentComponent.class)
public class TreeFolderComponent implements IsVueComponent {

  @Prop
  @JsProperty
  public Folder folder;
}
