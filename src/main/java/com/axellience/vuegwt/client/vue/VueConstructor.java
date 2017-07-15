package com.axellience.vuegwt.client.vue;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.VueGwtCache;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsFunction;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.tools.VueGwtTools;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import java.util.Set;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class VueConstructor<T extends Vue> extends JsFunction
{
    private Set<Class<? extends Vue>> vuegwt$localComponents;
    private Set<Class<? extends VueDirective>> vuegwt$localDirectives;
    private boolean areDependenciesInjected;

    @JsOverlay
    public final T instantiate()
    {
        return VueGwtTools.createInstanceForVueClass(this);
    }

    @JsOverlay
    public final <K extends T> VueConstructor<K> extend(VueComponentOptions<K> vueComponentOptions)
    {
        return VueGwtTools.extendVueClass(this, vueComponentOptions);
    }

    @JsOverlay
    public final void ensureDependenciesInjected()
    {
        if (areDependenciesInjected)
            return;

        areDependenciesInjected = true;
        ensureLocalComponentsInjected();
        ensureLocalDirectivesInjected();
    }

    @JsOverlay
    private void ensureLocalComponentsInjected()
    {
        if (this.vuegwt$localComponents != null)
        {
            JsObject options = JsTools.get(this, "options");
            JsObject<VueConstructor> components =
                (JsObject<VueConstructor>) options.get("components");
            if (components == null)
            {
                components = new JsObject<>();
                options.set("components", components);
            }

            for (Class<? extends Vue> childComponentClass : vuegwt$localComponents)
            {
                VueConstructor childComponentOptions =
                    VueGwtCache.getVueConstructor(childComponentClass);
                components.set(VueGwtTools.componentToTagName(childComponentClass),
                    childComponentOptions);
            }
            this.vuegwt$localComponents = null;
        }
    }

    @JsOverlay
    private void ensureLocalDirectivesInjected()
    {
        if (this.vuegwt$localDirectives != null)
        {
            JsObject options = JsTools.get(this, "options");
            JsObject<VueDirectiveOptions> directives =
                (JsObject<VueDirectiveOptions>) options.get("directives");
            if (directives == null)
            {
                directives = new JsObject<>();
                options.set("directives", directives);
            }

            for (Class<? extends VueDirective> childDirectiveClass : vuegwt$localDirectives)
            {
                VueDirectiveOptions childDirectiveDefinition =
                    VueGwtCache.getDirectiveOptions(childDirectiveClass);
                directives.set(VueGwtTools.directiveToTagName(childDirectiveClass),
                    childDirectiveDefinition);
            }

            this.vuegwt$localDirectives = null;
        }
    }

    @JsOverlay
    public final void setLocalComponents(Set<Class<? extends Vue>> localComponents)
    {
        this.vuegwt$localComponents = localComponents;
    }

    @JsOverlay
    public final void setLocalDirectives(Set<Class<? extends VueDirective>> localDirectives)
    {
        this.vuegwt$localDirectives = localDirectives;
    }
}
