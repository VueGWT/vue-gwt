package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import elemental2.core.Array;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(components = TreeFolderComponent.class)
public class TreeFolderContentComponent extends VueComponent
{
    @Prop
    @JsProperty
    public Array<Folder> content;
}
