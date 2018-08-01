package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import elemental2.core.JsArray;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(components = TreeFolderComponent.class)
public class TreeFolderContentComponent implements IsVueComponent {

  @Prop
  public JsArray<Folder> content;
}
