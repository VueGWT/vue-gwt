package com.axellience.vuegwt.client.vnode;

import com.axellience.vuegwt.client.jsnative.jsfunctions.JsSimpleFunction;
import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class InlineTemplate
{
    @JsProperty protected JsSimpleFunction render;
    @JsProperty protected JsArray<JsSimpleFunction> staticRenderFns;

    @JsOverlay
    public final JsSimpleFunction getRender()
    {
        return render;
    }

    @JsOverlay
    public final InlineTemplate setRender(JsSimpleFunction render)
    {
        this.render = render;
        return this;
    }

    @JsOverlay
    public final JsArray<JsSimpleFunction> getStaticRenderFns()
    {
        return staticRenderFns;
    }

    @JsOverlay
    public final InlineTemplate setStaticRenderFns(JsArray<JsSimpleFunction> staticRenderFns)
    {
        this.staticRenderFns = staticRenderFns;
        return this;
    }
    @JsOverlay
    public final InlineTemplate addStaticRenderFn(JsSimpleFunction staticRenderFn)
    {
        if (this.staticRenderFns == null)
            this.staticRenderFns = new JsArray<>();

        this.staticRenderFns.push(staticRenderFn);
        return this;
    }
}
