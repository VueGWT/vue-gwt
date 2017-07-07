# JsInterop

Vue GWT relies heavily on `JsInterop`.
This allows to expose Java code to the JavaScript world and vice-versa.

To make communication easier, Vue GWT comes with a few utility Classes.

* [JsObject](./js-object.md): A Java representation of a native JavaScript Object
* [JsArray<T>](./js-array.md): A Java representation of a native JavaScript Array
* [JsTools](./js-tools.md): Common utility methods to communicate/manipulate the JavaScript environment

All these classes **WON'T** work in your server side code.
They are just wrapper for existing Objects in the browser. 