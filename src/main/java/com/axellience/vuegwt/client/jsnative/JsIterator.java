package com.axellience.vuegwt.client.jsnative;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType( isNative = true, namespace = JsPackage.GLOBAL, name = "Object" )
public interface JsIterator<T>
{
    T next();
}