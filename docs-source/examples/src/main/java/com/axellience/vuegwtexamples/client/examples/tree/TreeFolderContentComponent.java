package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType
@Component(components = TreeFolderComponent.class)
public class TreeFolderContentComponent extends VueComponent
{
    @Prop
    public JsArray<Folder> content;
}
