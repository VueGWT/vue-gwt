package com.axellience.vuegwt.client.definitions;

import com.axellience.vuegwt.client.VueDirective;
import com.axellience.vuegwt.client.jsnative.JsTools;
import com.axellience.vuegwt.client.jsnative.VueGwtTools;
import com.axellience.vuegwt.client.jsnative.types.JsObject;
import jsinterop.annotations.JsType;

/**
 * Java representation of a Vue Directive definition
 * Class extending this one are generated using the Annotation processor for each VueDirective
 * <p>
 * An instance of this Class can be immediately passed to Vue.js instance where it's expecting a
 * directive definition object.
 * <p>
 * This is an internal Class, it shouldn't be extended in applications that use VueGWT.
 * @author Adrien Baron
 */
@JsType
public abstract class VueDirectiveDefinition extends JsObject
{
    /**
     * Will be set by class inheriting to an instance of the VueDirective class
     * Will allow us to get the hook functions from it
     */
    protected VueDirective vuegwt$javaDirectiveInstance;

    /**
     * Copy all the hook functions from our Java instance to the definition we pass Vue
     * https://vuejs.org/v2/guide/custom-directive.html#Hook-Functions
     * <p>
     * Will be called by the generated VueDirectiveDefinition
     */
    protected void copyHooks()
    {
        copyHook("bind");
        copyHook("inserted");
        copyHook("update");
        copyHook("componentUpdated");
        copyHook("unbind");
    }

    /**
     * Copy the given hook function from the Java instance to the definition we pass to Vue
     * @param hookFunctionName The name of the hook function to copy
     */
    private void copyHook(String hookFunctionName)
    {
        JsObject hookFunction = JsTools.get(vuegwt$javaDirectiveInstance, hookFunctionName);
        if (hookFunction == null)
            return;

        // Filter empty function inherited from VueDirective
        String body = VueGwtTools.getFunctionBody(hookFunction);
        if ("".equals(body))
            return;

        set(hookFunctionName, hookFunction);
    }
}
