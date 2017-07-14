package com.axellience.vuegwt.client.jsnative.html;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
@JsType( isNative = true, namespace = JsPackage.GLOBAL, name = "HTMLDocument" )
public class HTMLDocument
{
	public HTMLElement body;

	public native <T extends HTMLElement> T createElement( String name );

	@JsProperty( namespace = JsPackage.GLOBAL, name = "document" )
	public static native HTMLDocument get();
}
