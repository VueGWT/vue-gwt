package com.axellience.vuegwt.client.component.options.data;

import jsinterop.annotations.JsFunction;

/**
 * Functional interface used to generate new instance of a Component data model
 * This will be used as the data attribute of a VueComponent if the useFactory flag is not set to
 * false.
 * @author Adrien Baron
 */
@JsFunction
@FunctionalInterface
public interface DataFactory
{
    Object getData();
}
