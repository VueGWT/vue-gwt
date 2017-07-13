package com.axellience.vuegwt.client.jsnative.vnode;

import com.axellience.vuegwt.client.VueComponent;
import com.axellience.vuegwt.client.jsnative.types.JsArray;
import jsinterop.annotations.JsProperty;

import javax.lang.model.element.Element;

/**
 * @author Adrien Baron
 */
public class VNode
{
    @JsProperty public String tag;
    @JsProperty public VNodeData data;
    @JsProperty public JsArray<VNode> children;
    @JsProperty public String text;
    @JsProperty public Element elm;
    @JsProperty public String ns;
    @JsProperty public VueComponent context;
    @JsProperty public Object key;
    @JsProperty public VNodeComponentOptions componentOptions;
    @JsProperty public VueComponent componentInstance;
    @JsProperty public VNode parent;
    @JsProperty public boolean raw;
    @JsProperty public boolean isStatic;
    @JsProperty public boolean isRootInsert;
    @JsProperty public boolean isComment;
}
