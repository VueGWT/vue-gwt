package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

/**
 * @author Adrien Baron
 */
@Component(components = TreeFolderContentComponent.class)
public class TreeFolderComponent implements IsVueComponent {

  @Prop
  public Folder folder;
}
