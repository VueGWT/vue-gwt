# `JsObject`

Represent a JavaScript Native Object in the Java world.

You can see a `JsObject` as an instance of `Map<String, Object>`.

## How to Create an Instance

You create `JsObject` exactly like any Java Object:
```java
JsObject myObject = new JsObject();
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
JsArray<String> myPropertyNames = myObject.getOwnPropertyNames();
```