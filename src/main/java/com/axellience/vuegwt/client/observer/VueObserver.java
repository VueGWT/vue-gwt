package com.axellience.vuegwt.client.observer;

import com.axellience.vuegwt.client.jsnative.jstypes.JsArray;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Adrien Baron
 */
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class VueObserver
{
    @JsProperty
    private Dep dep;

    public native void observeArray(JsArray array);
    public native void observeArray(Object[] array);

    @JsOverlay
    public final void observe(Object toObserve) {
        observeArray(JsArray.array(toObserve));
    }

    @JsOverlay
    public final void notifyDep() {
        this.dep.notifySelf();
    }

    private class Dep {
        @JsMethod(name = "notify")
        public native void notifySelf();
    }
}
