# `JsObject<T>`

Represent a JavaScript Native Object in the Java world.
It can be typed or not.
When typed, you can only set values of the given type on it.

If you want your method to accept or return a `JsObject<Object>` you should set the type to `JsObject` without `<Object>`.
Otherwise it won't be possible to pass for example `JsObject<Todo>` to your function.

## How to Create an Instance

You create `JsObject` exactly like any Java Object:
```java
JsObject<Object> myObject = new JsObject<>();
```

When your program run this will be executed:
```js
var myObject = {};
```

## Setting a Property
```java
myObject.set("myProperty", myValue);
```

## Getting a Property
```java
Object myValue = myObject.get("myProperty");
```

## Getting the list of Properties (keys)
```java
JsArray<String> myPropertyNames = JsObject.getOwnPropertyNames(myObject);
```