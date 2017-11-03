package com.axellience.vuegwt.core.client.observer.vuegwtobservers;

import com.axellience.vuegwt.core.client.observer.VueGWTObserver;
import com.axellience.vuegwt.core.client.observer.VueGWTObserverManager;
import com.axellience.vuegwt.core.client.observer.VueObserver;
import com.axellience.vuegwt.core.client.tools.AfterMethodCall;
import com.axellience.vuegwt.core.client.tools.JsUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static com.axellience.vuegwt.core.client.tools.VueGWTTools.wrapMethod;

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
        VueObserver observer = VueGWTObserverManager.get().getVueObserver(list);
        observer.observeArray(JsUtils.from(list));

        AfterMethodCall<List> callObserver =
            ((object, methodName, result, arguments) -> observer.notifyDep());

        wrapMethod(list, "clear", callObserver);
        wrapMethod(list, "remove", callObserver);
        wrapMethod(list, "removeAll", callObserver);
        wrapMethod(list, "retainAll", callObserver);
        wrapMethod(list, "add", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(args);
        }));
        wrapMethod(list, "addAll", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(JsUtils.from((Collection<?>) args[0]));
        }));
        wrapMethod(list, "addAtIndex", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { args[1] });
        }));
        wrapMethod(list, "addAllAtIndex", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(JsUtils.from((Collection<?>) args[1]));
        }));
        wrapMethod(list, "setAtIndex", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(new Object[] { args[1] });
        }));
    }

    private void observeSet(Set set)
    {
        VueObserver observer = VueGWTObserverManager.get().getVueObserver(set);
        observer.observeArray(JsUtils.from(set));

        AfterMethodCall<Set> callObserver =
            ((object, methodName, result, arguments) -> observer.notifyDep());

        wrapMethod(set, "clear", callObserver);
        wrapMethod(set, "remove", callObserver);
        wrapMethod(set, "removeAll", callObserver);
        wrapMethod(set, "retainAll", callObserver);
        wrapMethod(set, "add", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(args);
        }));
        wrapMethod(set, "addAll", ((object, methodName, result, args) -> {
            observer.notifyDep();
            observer.observeArray(JsUtils.from((Collection<?>) args[0]));
        }));
    }
}
