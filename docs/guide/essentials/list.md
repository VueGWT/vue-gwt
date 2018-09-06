# List Rendering

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/list.html) and has been adapted for Vue GWT.*

## The `v-for` directive

We can use the `v-for` directive to render a list of items based on an array.
The `v-for` directive requires a special syntax in the form of `Type item in items`, where `items` is the source data array, `item` is an **alias** for the array element being iterated on and `Type` is `item` type:

### Basic Usage

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<ol>
    <li v-for="Todo todo in todos">
        {{ todo.getText() }}
    </li>
</ol>
```

```java
@Component
public class SimpleTodoListComponent implements IsVueComponent, HasCreated {
    @Data
    @JsProperty
    List<Todo> todos = new LinkedList<>();
    
    @Override
    public void created() {
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}
```

Result:

<div class="example-container" data-name="simpleTodoListComponent">
    <span id="simpleTodoListComponent"></span>
</div>

Inside `v-for` blocks we have full access to parent scope properties.
`v-for` also supports an optional second argument for the index of the current item.

```html
<vue-gwt:import class="com.axellience.vuegwtexamples.client.examples.common.Todo"/>
<ul>
    <li v-for="(Todo todo, index)  in  todos">
        {{ parentMessage }} - {{ index }} -> {{ todo.getText() }}
    </li>
</ul>
```

```java
@Component
public class VForWithIndexComponent implements IsVueComponent, HasCreated {
    @Data String parentMessage = "Message from parent";
    
    @Data
    @JsProperty
    List<Todo> todos = new LinkedList<>();

    @Override
    public void created() {
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}
```

Result:

<div class="example-container" data-name="vForWithIndexComponent">
    <span id="vForWithIndexComponent"></span>
</div>

You can also use `of` as the delimiter instead of `in`, so that it is closer to JavaScript's syntax for iterators:

```html
<li v-for="Todo todo of todos">
```

::: warning
To avoid gotchas with Java Collection observation, you should read about [Java Collection Observation](reactivity-system.md#java-collections-observation).
:::

### Template `v-for`

Similar to template `v-if`, you can also use a `<template>` tag with `v-for` to render a block of multiple elements. For example:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<ul>
  <template v-for="Todo todo in todos">
    <li>{{ todo.getText() }}</li>
    <li class="divider"></li>
  </template>
</ul>
```

### Object `v-for`

You can also use `v-for` to iterate through the properties of an `Object`.

For this you have to cast to `Object` in your `v-for`.
This tells Vue GWT that you are iterating on an `Object` and not an regular Collection.

```html
<ul>
    <li v-for="Object value in (Object) myObject">
        {{ value }}
    </li>
</ul>
```

```java
@Component
public class VForOnObjectComponent implements IsVueComponent, HasCreated {
    @Data JsObject<Object> myObject = new JsObject<>();

    @Override
    public void created() {
        this.myObject.set("myString", "Hello World");
        this.myObject.set("myInt", 12);
        this.myObject.set("myTodo", new Todo("I'm a Todo"));
    }
}
```

Result:

<div class="example-container" data-name="vForOnObjectComponent">
    <span id="vForOnObjectComponent"></span>
</div>

You can also provide a second argument for the key:

```html
<ul>
    <li v-for="(Object value, key) in (Object) myObject">
        {{ key }}: {{ value }}
    </li>
</ul>
```

<div class="example-container" data-name="vForOnObjectWithKeyComponent">
    <span id="vForOnObjectWithKeyComponent"></span>
</div>

And another for the index:

```html
<ul>
    <li v-for="(Object value, key, index) in (Object) myObject">
        {{ index }}. {{ key }}: {{ value }}
    </li>
</ul>
```

<div class="example-container" data-name="vForOnObjectWithKeyAndIndexComponent">
    <span id="vForOnObjectWithKeyAndIndexComponent"></span>
</div>

::: tip
When iterating over an object, the order is based on the key enumeration order of `Object.keys()`, which is **not** guaranteed to be consistent across JavaScript engine implementations.
:::

### Range `v-for`

`v-for` can also take an integer.
In this case it will repeat the template that many times.

```html
<div>
    <span v-for="int n in 5">{{ n }} </span>
</div>
```

<div class="example-container" data-name="vForWithRangeComponent">
    <span id="vForWithRangeComponent"></span>
</div>

### Components and `v-for`

You can directly use `v-for` on a custom component, like any normal element:

```html
<vue-gwt:import class="com.mypackage.Item"/>
<my-component v-for="Item item in items" :key="item.getId()"></my-component>
```

> In Vue.js 2.2.0+, when using `v-for` with a component, a [`key`](list.html#key) is now required.

However, this won't automatically pass any data to the component, because components have isolated scopes of their own.
In order to pass the iterated data into the component, we should also use props:

```html
<vue-gwt:import class="com.mypackage.Item"/>
<my-component
  v-for="Item item in items"
  v-bind:item="item"
  v-bind:key="item.getId()">
</my-component>
```

The reason for not automatically injecting `item` into the component is because that makes the component tightly coupled to how `v-for` works.
Being explicit about where its data comes from makes the component reusable in other situations.

You can see our [TodoList example](https://vuegwt.github.io/vue-gwt-demo/).

### Using v-for with v-if

When they exist on the same node, `v-for` has a higher priority than `v-if`.
That means the `v-if` will be run on each iteration of the loop separately.
This is very useful when you want to render nodes for only *some* items, like below:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<li v-for="Todo todo in todos" v-if="!todo.isComplete()">
  {{ todo }}
</li>
```

The above only renders the todos that are not complete.

If instead, your intent is to conditionally skip execution of the loop, you can place the `v-if` on a wrapper element (or [ `<template>` ](https://vuejs.org/v2/guide/conditional.html#Conditional-Groups-with-v-if-on-lt-template-gt)).
For example:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<ul v-if="shouldRenderTodos">
  <li v-for="Todo todo in todos">
    {{ todo }}
  </li>
</ul>
```

## Using `key`

When Vue is updating a list of elements rendered with `v-for`, it by default uses an "in-place patch" strategy.
If the order of the data items has changed, instead of moving the DOM elements to match the order of the items, Vue will simply patch each element in-place and make sure it reflects what should be rendered at that particular index.
This is similar to the behavior of `track-by="$index"` in Vue 1.x.

This default mode is efficient, but only suitable **when your list render output does not rely on child component state or temporary DOM state (e.g. form input values)**.

To give Vue a hint so that it can track each node's identity, and thus reuse and reorder existing elements, you need to provide a unique `key` attribute for each item.
An ideal value for `key` would be the unique id of each item.
This special attribute is a rough equivalent to `track-by` in 1.x, but it works like an attribute, so you need to use `v-bind` to bind it to dynamic values (using shorthand here):

```html
<vue-gwt:import class="com.mypackage.Item"/>
<div v-for="Item item in items" :key="item.getId()">
  <!-- content -->
</div>
```

It is recommended to provide a `key` with `v-for` whenever possible, unless the iterated DOM content is simple, or you are intentionally relying on the default behavior for performance gains.

Since it's a generic mechanism for Vue to identify nodes, the `key` also has other uses that are not specifically tied to `v-for`, as we will see later in the guide.

## Displaying Filtered/Sorted Results

Sometimes we want to display a filtered or sorted version of an array without actually mutating or resetting the original data.
In this case, you can create a computed property that returns the filtered or sorted array.

For example:

```html
<div>
    <span v-for="Integer n in evenNumbers">{{ n }} </span>
</div>
```

```java
@Component
public class EvenNumbersComponent implements IsVueComponent {
    @Data
    @JsProperty
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    @Computed
    public List<Integer> getEvenNumbers() {
        return this.numbers.stream().filter(number -> number % 2 == 0).collect(Collectors.toList());
    }
}
```

<div class="example-container" data-name="evenNumbersComponent">
    <span id="evenNumbersComponent"></span>
</div>

In situations where computed properties are not feasible (e.g. inside nested `v-for` loops), you can just use a method:

```html
...
<!-- The variable numbers comes from a v-for in the template -->
<span v-for="Integer n in getEven(numbers)">{{ n }} </span>
...
```

```java
@Component
public class EvenNumbersComponent implements IsVueComponent {
    // No @Computed annotation
    public List<Integer> getEven(List<Integer> numbers) {
        return this.numbers.stream().filter(number -> number % 2 == 0).collect(Collectors.toList());
    }
}
```