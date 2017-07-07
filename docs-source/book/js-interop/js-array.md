# `JsArray<T>`

Represent a JavaScript Native Array in the Java world.

Because we are in the Java world, it is parametrized and can only contain elements of the given type.

But an instance of an Array in JavaScript can actually contain objects of different types.
Just keep that in mind if you get an instance of `JsArray` from an external library.

## How to Create an Instance

You create `JsArray` exactly like any Java Collection:
```java
JsArray<Todo> myArray = new JsArray<>();
```

When your program run this will be executed:
```js
var myArray = [];
```

You can also create an Array inline:
```java
JsArray<Integer> myArray = JsArray.of(1, 2, 3, 4, 5);
```


## What Can I Do with My JsArray?

### Accessing/Changing

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

### Iterate
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

### Streaming

If you like Java 8 Streams, JavaScript Arrays supports some of the operators you know:
`map`, `filter`, `reduce`, `reduceRight`.

### And a Lot More

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