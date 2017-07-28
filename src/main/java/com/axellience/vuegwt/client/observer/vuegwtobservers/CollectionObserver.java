package com.axellience.vuegwt.client.observer.vuegwtobservers;

import com.axellience.vuegwt.client.observer.VueGWTObserver;
import com.axellience.vuegwt.client.observer.VueGWTObserverManager;
import com.axellience.vuegwt.client.observer.VueObserver;
import com.axellience.vuegwt.client.tools.AfterMethodCall;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.axellience.vuegwt.client.tools.VueGWTTools.wrapMethod;

/**
 * This observer is able to observe Java Collections.
 * For now it only support List and Set.
 * <br>
 * To observe the collection, it wraps the Java mutable methods and call Vue observer
 * when they are called.
 * @author Adrien Baron
 */
public class CollectionObserver extends VueGWTObserver
{
    @Override
    public boolean observe(Object object)
    {
        if (object instanceof List)
        {
            observeList((List) object);
            return true;
        }

        if (object instanceof Set)
        {
            observeSet((Set) object);
            return true;
        }

        return false;
    }

    private void observeList(List list)
    {
        VueObserver observer = VueGWTObserverManager.getVueObserver(list);
        AfterMethodCall<List> callObserver =
            ((object, methodName, result, arguments) -> observer.notifyDep());

        wrapMethod(list, "clear", callObserver);
        wrapMethod(list, "remove", callObserver);
        wrapMethod(list, "removeAll", callObserver);
        wrapMethod(list, "retainAll", callObserver);
        wrapMethod(list, "add", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(arguments);
        }));
        wrapMethod(list, "addAll", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(((Collection) arguments[0]).toArray());
        }));
        wrapMethod(list, "add", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { arguments[1] });
        }));
    }

    private void observeSet(Set set)
    {
        VueObserver observer = VueGWTObserverManager.getVueObserver(set);
        AfterMethodCall<Set> callObserver =
            ((object, methodName, result, arguments) -> observer.notifyDep());

        wrapMethod(set, "clear", callObserver);
        wrapMethod(set, "remove", callObserver);
        wrapMethod(set, "removeAll", callObserver);
        wrapMethod(set, "retainAll", callObserver);
        wrapMethod(set, "add", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(arguments);
        }));
        wrapMethod(set, "addAll", ((object, methodName, result, arguments) -> {
            observer.notifyDep();
            observer.observeArray(((Collection) arguments[0]).toArray());
        }));
    }
}
