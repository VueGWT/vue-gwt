package com.axellience.vuegwt.client;

import com.axellience.vuegwt.client.jsnative.VueGwtToolsInjector;

/**
 * The Java representation of a VueComponent
 * Whenever you want to add a component to your application you should extends this class.
 *
 * @author Adrien Baron
 */
public abstract class VueComponent extends VueComponentInstance
{
    static
    {
        // Inject the JS tools in the page
        VueGwtToolsInjector.inject();
    }

    /**
     * Lifecycle hooks
     * By default they are not copied, they are here to facilitate development
     */
    public void beforeCreate() {

    }
    public void created() {

    }
    public void beforeMount() {

    }
    public void mounted() {

    }
    public void beforeUpdate() {

    }
    public void updated() {

    }
    public void activated() {

    }
    public void deactivated() {

    }
    public void beforeDestroy() {

    }
    public void destroyed() {

    }
}
