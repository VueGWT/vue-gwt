package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component(components = TreeFolderContentComponent.class)
public class TreeFolderComponent extends VueComponent
{
    @Prop
    public Folder folder;
}
