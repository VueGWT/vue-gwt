package com.axellience.vuegwtexamples.client.examples.errorboundary;

import com.axellience.vuegwt.core.client.component.hooks.HasRender;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasErrorCaptured;
import com.axellience.vuegwt.core.client.vnode.VNode;
import com.axellience.vuegwt.core.client.vnode.VNodeData;
import com.axellience.vuegwt.core.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.core.annotations.component.Component;
import jsinterop.annotations.JsProperty;
import jsinterop.base.JsPropertyMap;

import static com.axellience.vuegwt.core.client.tools.JsUtils.map;

@Component
public class ErrorBoundaryComponent implements IsVueComponent, HasErrorCaptured, HasRender
{
    @JsProperty JsPropertyMap error;

    @Override
    public boolean errorCaptured(JsPropertyMap err, IsVueComponent vue, String info)
    {
        this.error = err;
        return false;
    }

    @Override
    public VNode render(VNodeBuilder builder)
    {
        if (error != null)
        {
            VNodeData style = new VNodeData().setStyle(map("color", "red"));
            return builder.el("pre", style, "An error occurred: " + error);
        }

        return builder.el(ErrorMakerComponent.class);
    }
}
