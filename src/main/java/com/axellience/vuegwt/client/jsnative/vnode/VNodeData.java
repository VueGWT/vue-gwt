package com.axellience.vuegwt.client.jsnative.vnode;

import com.axellience.vuegwt.client.jsnative.types.JsArray;
import com.axellience.vuegwt.client.jsnative.types.JsObject;

/**
 * @author Adrien Baron
 */
public class VNodeData
{
    Object key;
    String slot;
    Dictionary<ScopedSlot> scopedSlots;
    String ref;
    String tag;
    String staticClass;
    Object classProp;
    Object staticStyle;
    JsObject style;
    JsObject props;
    JsObject attrs;
    JsObject domProps;
    Object hook;
    Object on;
    Object nativeOn;
    Object transition;
    boolean show;
    InlineTemplate inlineTemplate;
    JsArray<VNodeDirective> directives;
    boolean keepAlive;
}
