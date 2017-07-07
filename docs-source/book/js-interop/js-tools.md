# JsTools

Provides a set of methods to manipulate the JavaScript world from our Java app.

Unlike [JsObject](./js-object.md) and [JsArray](./js-array.md) this is not a wrapper of an existing JavaScript Object.
It's only exposes static methods to do various operations.

## Getter/Setter

### &lt;T&gt; T get(`Object` o, `String` propertyName)

Same as [JsObject](./js-object.md) getter method.
But this one also works on any Java object instance.

So you are able to do this for example:

```java
Todo myTodo = new Todo("My Todo Text");
String text = JsTools.get(myTodo, "text");
```

Nice right?
But sadly this will only work at runtime if the property `text` of `Todo` is a `JsInterop` property.

This is because GWT renames all the non `JsInterop` properties, so your `Todo` will actually look like this in JS:

```js
var myTodo = {
	text_$g: "My Todo Text" // "text_$g" is random, depends on optimizations
}
```

And so your get will return `null` ☹️  .

### `void` set(`Object` o, `String` propertyName, `Object` value)

Same as [JsObject](./js-object.md) setter method.
But this one also works on any Java object instance.

Sadly, same limitation as the getter.
You won't be able to set non `JsInterop` properties values at runtime.
For example:

```java
Todo myTodo = new Todo("My Todo Text");
JsTools.set(myTodo, "text", "My New Text");
```

Will result in:

```js
var myTodo = {
	text_$g: "My Todo Text", // "text_$g" is random, depends on optimizations,
	text: "My New Text"
}
```

## `JsObject` getWindow()

Return the global `window` object from the browser as a `JsObject`.
It can be useful to get or set a property in the global JavaScript context.

## Log

If you want to log something quickly in the console you can use:
```java
JsTools.log("Hello Log");
```

It's better to use the regular GWT logging mechanism though.
But it can come handy when debugging.