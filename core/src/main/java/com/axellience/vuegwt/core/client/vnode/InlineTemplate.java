package com.axellience.vuegwt.core.client.vnode;

import com.axellience.vuegwt.core.client.jsnative.jsfunctions.JsRunnable;
import elemental2.core.JsArray;
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
    @JsProperty protected JsRunnable render;
    @JsProperty protected JsArray<JsRunnable> staticRenderFns;

    @JsOverlay
    public final JsRunnable getRender()
    {
        return render;
    }

    @JsOverlay
    public final InlineTemplate setRender(JsRunnable render)
    {
        this.render = render;
        return this;
    }

    @JsOverlay
    public final JsArray<JsRunnable> getStaticRenderFns()
    {
        return staticRenderFns;
    }

    @JsOverlay
    public final InlineTemplate setStaticRenderFns(JsArray<JsRunnable> staticRenderFns)
    {
        this.staticRenderFns = staticRenderFns;
        return this;
    }
    @JsOverlay
    public final InlineTemplate addStaticRenderFn(JsRunnable staticRenderFn)
    {
        if (this.staticRenderFns == null)
            this.staticRenderFns = new JsArray<>();

        this.staticRenderFns.push(staticRenderFn);
        return this;
    }
}
