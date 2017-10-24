package com.axellience.vuegwt.client.observer.vuegwtobservers;

import com.axellience.vuegwt.client.observer.VueGWTObserver;
import com.axellience.vuegwt.client.observer.VueGWTObserverManager;
import com.axellience.vuegwt.client.observer.VueObserver;
import com.axellience.vuegwt.client.tools.AfterMethodCall;
import elemental2.core.Array;

import java.util.Map;

import static com.axellience.vuegwt.client.tools.VueGWTTools.wrapMethod;

/**
 * This observer is able to observe Java Collections.
 * For now it only support List and Set.
 * <br>
 * To observe the collection, it wraps the Java mutable methods and call Vue observer
 * when they are called.
 * @author Adrien Baron
 */
public class MapObserver extends VueGWTObserver
{
    @Override
    public boolean observe(Object object)
    {
        if (object instanceof Map)
        {
            observeMap((Map) object);
            return true;
        }

        return false;
    }

    private void observeMap(Map map)
    {
        VueObserver observer = VueGWTObserverManager.getVueObserver(map);
        observer.observeArray(new Array(map));

        AfterMethodCall<Map> callObserver =
            ((object, methodName, result, arguments) -> observer.notifyDep());

        wrapMethod(map, "clear", callObserver);
        wrapMethod(map, "remove", callObserver);

        wrapMethod(map, "put", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { arguments[1] });
        }));
        wrapMethod(map, "putIfAbsent", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { arguments[1] });
        }));
        wrapMethod(map, "putAll", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(new Array(((Map) arguments[0])));
        }));

        wrapMethod(map, "replace", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { arguments[1], arguments[2] });
        }));
    }
}
