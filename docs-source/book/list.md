# List Rendering

!INCLUDE "dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/list.html) and has been adapted for Vue GWT.*

## `v-for`

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
@JsType
@Component
public class SimpleTodoListComponent extends VueComponent {
    public JsArray<Todo> todos;
    
    @Override
    public void created() {
        this.todos = new JsArray<>();
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}
```

Result:

{% raw %}
<p class="example-container" data-name="simpleTodoListComponent">
    <span id="simpleTodoListComponent"></span>
</p>
{% endraw %}

Vue.js [support getting the index of the current element](https://vuejs.org/v2/guide/list.html#Basic-Usage) being looped on.
This is not yet supported in Vue GWT.

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

[Iterating on Object properties](https://vuejs.org/v2/guide/list.html#Object-v-for) is not supported yet by Vue GWT.

### Range `v-for`

[Repeating on a fix number](https://vuejs.org/v2/guide/list.html#Range-v-for) is not yet supported in Vue GWT.

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

You can see our [TodoList example](https://axellience.github.io/vue-gwt-demo/).

### `v-for` with `v-if`

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

If instead, your intent is to conditionally skip execution of the loop, you can place the `v-if` on a wrapper element (or [`<template>`](https://vuejs.org/v2/guide/conditional.html#Conditional-Groups-with-v-if-on-lt-template-gt)).
For example:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<ul v-if="shouldRenderTodos">
  <li v-for="Todo todo in todos">
    {{ todo }}
  </li>
</ul>
```

## `key`

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

## Array Change Detection

### Mutation Methods

Vue wraps an observed JS array's mutation methods so they will also trigger view updates.
The wrapped methods are:

- `push()`
- `pop()`
- `shift()`
- `unshift()`
- `splice()`
- `sort()`
- `reverse()`

You can open the console and play with the previous examples' `items` array by calling their mutation methods.
For example: `simpleTodoListComponent.todos.shift()`.

### Replacing an Array

Mutation methods, as the name suggests, mutate the original array they are called on.
In comparison, there are also non-mutating methods, e.g. `filter()`, `concat()` and `slice()`, which do not mutate the original array but **always return a new array**.
When working with non-mutating methods, you can just replace the old array with the new one:

```java
JsArray<Todo> notDoneTodos = new JsArray<>();
for (Todo todo : this.todos.iterate())
    if (!todo.isDone())
        notDoneTodos.push(todo);
this.todos = notDoneTodos;
```

You might think this will cause Vue to throw away the existing DOM and re-render the entire list - luckily, that is not the case.
Vue implements some smart heuristics to maximize DOM element reuse, so replacing an array with another array containing overlapping objects is a very efficient operation.

### Caveats

Due to limitations in JavaScript, Vue **cannot** detect the following changes to an array:

1. When you directly set an item with the index, e.g. `this.todos.set(indexOfItem, newValue)`
2. When you modify the length of the array, e.g. `this.todos.length = newLength`

To overcome caveat 1, both of the following will accomplish the same as `this.todos.set(indexOfItem, newValue)`, but will also trigger state updates in the reactivity system:

```java
this.todos.splice(indexOfItem, 1, newValue);
```

To deal with caveat 2, you can use `splice`:

```js
this.todos.splice(newLength)
```

## Displaying Filtered/Sorted Results

**TODO** You can check [Vue.js documentation on this](https://vuejs.org/v2/guide/list.html#Displaying-Filtered-Sorted-Results) in the meantime.