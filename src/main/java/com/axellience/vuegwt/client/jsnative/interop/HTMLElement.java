package com.axellience.vuegwt.client.jsnative.interop;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType( isNative = true, namespace = JsPackage.GLOBAL, name = "HTMLElement" )
public class HTMLElement
{
	public String innerHTML;
	public String innerText;

	public native void appendChild( HTMLElement child );
}
