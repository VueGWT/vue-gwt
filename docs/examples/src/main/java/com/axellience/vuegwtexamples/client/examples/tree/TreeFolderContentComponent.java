package com.axellience.vuegwtexamples.client.examples.tree;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import elemental2.core.JsArray;
import jsinterop.annotations.JsProperty;

/**
 * @author Adrien Baron
 */
@Component(components = TreeFolderComponent.class)
public class TreeFolderContentComponent implements IsVueComponent
{
    @Prop
    @JsProperty
    public JsArray<Folder> content;
}
