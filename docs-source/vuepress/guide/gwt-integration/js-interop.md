# JsInterop

Vue GWT relies heavily on `JsInterop` and [`Elemental2`](https://github.com/google/elemental2).
This allows to expose Java code to the JavaScript world and vice-versa.

To make communication easier, Vue GWT comes with an utility Class, `JsUtils`.

All these methods and Objects **WON'T** work in your server side code.
They are just wrapper for existing Objects in the browser.
 
## `JsPropertyMap<T>`

This interface comes from Elemental2 core.
It represents a JavaScript object as a map.
It can be typed or not.
When typed, you can only set values of the given type on it.

If you want your method to accept or return a `JsPropertyMap<Object>` you should set the type to `JsPropertyMap` without `<Object>`.
Otherwise it won't be possible to pass for example `JsPropertyMap<Todo>` to your function.

### How to Create an Instance

You create `JsPropertyMap` using the `of` static method:
```java
JsPropertyMap<String> myObject = JsPropertyMap.of();
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

To do this you must cast your `JsPropertyMap` to `JsObject`, also from Elemental2 core.

```java
Array<String> myPropertyNames = ((JsObject) myObject).getOwnPropertiesName();
```

### `map` static builder method

It's possible to create a `JsPropertyMap` inline very easily using the static `map` method from `JsUtils` that comes with Vue GWT.

```java
// Equivalent to {key1: value1, key2: value2, key3: value3}
JsPropertyMap myObject = JsUtils.map( e("key1", value1), e("key2", value2), e("key3", value3))
```

You also nest the calls to get `JsPropertyMap` in `JsPropertyMap`:

```java
// Equivalent to {parentKey1: parentValue1, parentKey2: {childKey1: childValue1, childKey2: childValue2}}
JsPropertyMap myObject = map(
    e("parentKey1", parentValue1),
    e("parentKey2", map( e("childKey1", childValue1), e("childKey2", childValue2) ))
);
```

If you only one key/value pair in your `JsPropertyMap` you don't need to wrap it in `e()`:
```java
JsPropertyMap myObject = map("key", value);
```

You can use `map` in templates as it is statically imported for you:
```html
<a :class='map( e("red", hasError), e("blue", isOk) )'/>
```

You can also mix `map` with [`array`](#array):

```java
// Equivalent to {arrayValue: ["hello", "world"]}
JsPropertyMap myObject = map("arrayValue", array("hello", "world"));
```

## `JsArray<T>`

This object comes from Elemental2 core.
It represents a JavaScript Native Array in the Java world.

Because we are in the Java world, it is parametrized and can only contain elements of the given type.

But an instance of an Array in JavaScript can actually contain objects of different types.
Just keep that in mind if you get an instance of `JsArray` from an external JS library.

### How to Create an Instance

You create `JsArray` exactly like any Java Collection:
```java
JsArray<Todo> myArray = new JsArray<>();
```

When your program run this will be executed:
```js
var myArray = [];
```

### `array` static builder method

It's possible to create a `JsArray` inline very easily using the static `array` method from `JsUtils` that comes with Vue GWT.

```java
// Equivalent to ["hello", "world"]
JsArray<String> myArray = JsUtils.array("hello", "world");
```

You also nest the calls to get `JsArray` in `JsArray`:

```java
// Equivalent to ["parentValue1", ["childArrayValue1", "childArrayValue2]]
JsArray<Object> myArray = array("parentValue1", array("childArrayValue1", "childArrayValue2"));
```

You can use `array` in templates as it is statically imported for you:
```html
<a :class='array(firstClass, secondClass)'/>
```

You can also mix `array` with [`map`](#map):

```java
// Equivalent to ["hello", {world: true}]
JsArray<Object> myArray = array("hello", map("world", true));
```

### What Can I Do with My JavaScript Array?

#### Accessing/Changing

You can get and set values at index on your `JsArray`.
The `setAt` and `getAt` methods corresponds to the `[]` accessing syntax in JavaScript.
Be aware [that `setAt` is not observable by Vue.js](https://vuejs.org/v2/guide/list.html#Array-Change-Detection) (due to a technical limitation of ES5 getter/setters).

```java
// Setting at index
// Equivalent to myArray[0] = myTodo
// Not observed by Vue
myArray.setAt(0, myTodo);

// Setting at index (observed by Vue)
myArray.splice(index, 1, newValue)

// Getting at index
Todo myTodo = myArray.getAt(0);
```

```java
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
for(Todo todo in myArray.asArray()) {
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