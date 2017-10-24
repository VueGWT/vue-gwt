package com.axellience.vuegwtexamples.client.examples.simplerender;

import com.axellience.vuegwt.client.component.HasRender;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vnode.VNode;
import com.axellience.vuegwt.client.vnode.VNodeData;
import com.axellience.vuegwt.client.vnode.builder.VNodeBuilder;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.Prop;
import com.google.gwt.regexp.shared.RegExp;
import elemental2.core.Array;
import jsinterop.annotations.JsProperty;
import jsinterop.base.Js;

/**
 * @author Adrien Baron
 */
@Component
public class AnchoredHeadingComponent extends VueComponent implements HasRender
{
    private static RegExp camelCasePattern = RegExp.compile("([a-z])([A-Z]+)", "g");

    @JsProperty
    @Prop(required = true)
    Integer level;

    @Override
    public VNode render(VNodeBuilder builder)
    {
        String text =
            getChildrenTextContent(this.$slots().get("default")).trim().replaceAll(" ", "-");

        String headingId = camelCasePattern.replace(text, "$1-$2").toLowerCase();

        return builder.el("h" + this.level,
            builder.el("a",
                VNodeData.get().attr("name", headingId).attr("href", "#" + headingId),
                this.$slots().get("default")));
    }

    private String getChildrenTextContent(Array<VNode> children)
    {
        return ((Array<String>) Js.cast(children.map((child, index, array) -> {
            if (child.getChildren() != null && child.getChildren().length > 0)
                return getChildrenTextContent(child.getChildren());
            return child.getText();
        }))).join("");
    }
}
