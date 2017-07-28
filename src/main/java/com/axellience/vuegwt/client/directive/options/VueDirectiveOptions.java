package com.axellience.vuegwt.client.directive.options;

import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.JsTools;
import jsinterop.annotations.JsType;

/**
 * Java representation of VueDirectiveOptions
 * Class extending this one are generated using the Annotation processor for each VueDirective
 * <p>
 * An instance of this Class can be immediately passed to Vue.js instance where it's expecting a
 * directive options object.
 * <p>
 * This is an internal Class, it shouldn't be extended in applications that use VueGWT.
 * @author Adrien Baron
 */
@JsType
public abstract class VueDirectiveOptions extends JsObject
{
    /**
     * Will be set by class inheriting to an instance of the VueDirective class
     * Will allow us to get the hook functions from it
     */
    protected VueDirective vuegwt$javaDirectiveInstance;

    /**
     * Copy all the hook functions from our Java instance to the options we pass Vue
     * https://vuejs.org/v2/guide/custom-directive.html#Hook-Functions
     * <p>
     * Will be called by the generated VueDirectiveOptions
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
     * Copy the given hook function from the Java instance to the options we pass to Vue
     * @param hookFunctionName The name of the hook function to copy
     */
    private void copyHook(String hookFunctionName)
    {
        JsObject hookFunction = JsTools.get(vuegwt$javaDirectiveInstance, hookFunctionName);
        if (hookFunction == null)
            return;

        // Filter empty function inherited from VueDirective
        String body = JsTools.getFunctionBody(hookFunction);
        if ("".equals(body))
            return;

        set(hookFunctionName, hookFunction);
    }
}
