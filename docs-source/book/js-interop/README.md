# JsInterop

Vue GWT relies heavily on `JsInterop`.
This allows to expose Java code to the JavaScript world and vice-versa.

To make communication easier, Vue GWT comes with a few utility Classes.

All these classes **WON'T** work in your server side code.
They are just wrapper for existing Objects in the browser.
 
## `JsObject<T>` {#js-object}

Represent a JavaScript Native Object in the Java world.
It can be typed or not.
When typed, you can only set values of the given type on it.

If you want your method to accept or return a `JsObject<Object>` you should set the type to `JsObject` without `<Object>`.
Otherwise it won't be possible to pass for example `JsObject<Todo>` to your function.

### How to Create an Instance

You create `JsObject` exactly like any Java Object:
```java
JsObject<Object> myObject = new JsObject<>();
```

When your program run this will be executed:
```js
var myObject = {};
```

### Setting a Property
```java
myObject.set("myProperty", myValue);
```

### Getting a Property
```java
Object myValue = myObject.get("myProperty");
```

### Getting the list of Properties (keys)
```java
JsArray<String> myPropertyNames = JsObject.getOwnPropertyNames(myObject);
```

### `map` static builder method {#map}

It's possible to create a `JsObject` inline very easily using the static `map` method.

```java
// Equivalent to {key1: value1, key2: value2, key3: value3}
JsObject myObject = JsObject.map( e("key1", value1), e("key2", value2), e("key3", value3))
```

You also nest the calls to get `JsObject` in `JsObject`:

```java
// Equivalent to {parentKey1: parentValue1, parentKey2: {childKey1: childValue1, childKey2: childValue2}}
JsObject myObject = map(
    e("parentKey1", parentValue1),
    e("parentKey2", map( e("childKey1", childValue1), e("childKey2", childValue2) ))
);
```

If you only one key/value pair in your `JsObject` you don't need to wrap it in `e()`:
```java
JsObject myObject = map("key", value);
```

You can use `map` in templates as it is statically imported for you:
```html
<a :class='map( e("red": hasError), e("blue": isOk) )'/>
```

You can also mix `map` with [`array`](#array):

```java
// Equivalent to {arrayValue: ["hello", "world"]}
JsObject myObject = map("arrayValue", array("hello", "world"));
```

## `JsArray<T>` {#js-array}

Represent a JavaScript Native Array in the Java world.

Because we are in the Java world, it is parametrized and can only contain elements of the given type.

But an instance of an Array in JavaScript can actually contain objects of different types.
Just keep that in mind if you get an instance of `JsArray` from an external library.

### How to Create an Instance

You create `JsArray` exactly like any Java Collection:
```java
JsArray<Todo> myArray = new JsArray<>();
```

When your program run this will be executed:
```js
var myArray = [];
```

### `array` static builder method {#array}

It's possible to create a `JsArray` inline very easily using the static `array` method.

```java
// Equivalent to ["hello", "world"]
JsArray<String> myArray = JsArray.array("hello", "world");
```

You also nest the calls to get `JsArray` in `JsArray`:

```java
// Equivalent to ["parentValue1", ["childArrayValue1", "childArrayValue2]]
JsArray myArray = array("parentValue1", array("childArrayValue1", "childArrayValue2"));
```

You can use `array` in templates as it is statically imported for you:
```html
<a :class='array(firstClass, secondClass)'/>
```

You can also mix `array` with [`map`](#map):

```java
// Equivalent to ["hello", {world: true}]
JsArray myArray = array("hello", map("world", true));
```

### What Can I Do with My JsArray?

#### Accessing/Changing

```java
// Setting at index
myArray.set(0, myTodo);

// Getting at index
Todo myTodo = myArray.get(0);

// Getting the length of the Array
int myLength = myArray.length;

// Add an item at the end
myArray.push(myTodo);

// Remove last item and returns it
Todo myTodo = myArray.pop();
```

#### Iterate
```java
// The Java way
for(Todo todo in myArray.iterate()) {
    // Change the world
}

// The JS way in Java ;)
myArray.forEach(todo -> {
   // Change the world some more! 
});
```

#### Streaming

If you like Java 8 Streams, JavaScript Arrays supports some of the operators you know:
`map`, `filter`, `reduce`, `reduceRight`.

#### And a Lot More

`JsArray` supports all the ES5 (IE 9+) methods on Array:

* `sort`
* `join`
* `indexOf`
* `concat`
* `unshift`
* `splice`
* And more!

You can get the [full list here](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Array).
(Some of the methods in that list are not supported by `JsArray` because they are not supported by IE 9+).


## JsTools {#js-tools}

Provides a set of methods to manipulate the JavaScript world from our Java app.

Unlike [JsObject](#js-object) and [JsArray](#js-array) this is not a wrapper of an existing JavaScript Object.
It's only exposes static methods to do various operations.

### Getter/Setter

#### &lt;T&gt; T get(`Object` o, `String` propertyName)

Same as [JsObject](#js-object) getter method.
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

#### `void` set(`Object` o, `String` propertyName, `Object` value)

Same as [JsObject](#js-object) setter method.
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

### `JsObject` getWindow()

Return the global `window` object from the browser as a `JsObject`.
It can be useful to get or set a property in the global JavaScript context.

### Log

If you want to log something quickly in the console you can use:
```java
JsTools.log("Hello Log");
```

It's better to use the regular GWT logging mechanism though.
But it can come handy when debugging.