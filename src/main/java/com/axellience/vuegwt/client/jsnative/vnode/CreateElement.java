package com.axellience.vuegwt.client.jsnative.vnode;

/**
 * @author Adrien Baron
 */
public class CreateElement
{
    private final CreateElementFunction function;

    public CreateElement(CreateElementFunction function)
    {
        this.function = function;
    }

    public VNode c() {
        return this.function.create(null, null, null);
    }

    public VNode c(String tag, VNode... children) {
        return this.function.create(tag, children, null);
    }


}
