# Computed Properties and Watchers

!INCLUDE "../dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/computed.html) and has been adapted for Vue GWT.*

## Computed Properties

In-template expressions are very convenient, but they are really only meant for simple operations.
Putting too much logic into your templates can make them bloated and hard to maintain. For example:

```html
<div id="example">
  {{ new StringBuilder(message).reverse().toString() }}
</div>
```

At this point, the template is no longer simple and declarative.
You have to look at it for a second before realizing that it displays `message` in reverse.
The problem is made worse when you want to include the reversed message in your template more than once.

That's why for any complex logic, you should use a **computed property**.

### Basic Example

```html
<div id="example">
  <p>Original message: "{{ message }}"</p>
  <p>Computed reversed message: "{{ reversedMessage }}"</p>
</div>
```

```java
@Component
public class ReverseComponent implements IsVueComponent {
    @JsProperty String message = "Hello";

    @Computed // Note the annotation that tells Vue GWT that this is a Computed Properties
    public String getReversedMessage() {
        return new StringBuilder(message).reverse().toString();
    }
}
```

Result:

{% raw %}
<div class="example-container" data-name="reverseComponent">
    <span id="reverseComponent"></span>
</div>
{% endraw %}

Here we have declared a computed property `reversedMessage`.
For this we declared a getter method `getReversedMessage()` following the Java bean naming convention.
The method we provided will be used as the getter function for the property `reversedMessage` of your template.

You can open the console and play with the example vm yourself.
The value of `reverseComponent.reversedMessage` is always dependent on the value of `reverseComponent.message`.

```js
console.log(reverseComponent.reversedMessage); // -> 'olleH'
reverseComponent.message = 'Goodbye';
console.log(reverseComponent.reversedMessage); // -> 'eybdooG'
```

You can data-bind to computed properties in templates just like a normal property.
Vue is aware that `reversedMessage` depends on `message`, so it will update any bindings that depend on `reversedMessage` when `message` changes.
And the best part is that we've created this dependency relationship declaratively: the computed getter function has no side effects, which makes it easy to test and reason about.

### Computed Caching vs Methods

You may have noticed we can achieve the same result by invoking a method in the expression:

```html
<p>Reversed message: "{{ getReversedMessage() }}"</p>
```

```java
@Component
public class ReverseComponent implements IsVueComponent {
    @JsProperty String message = "Hello";

    // Note that there a no more @Computed annotation
    @JsMethod
    public String getReversedMessage() {
        return new StringBuilder(message).reverse().toString();
    }
}
```

Instead of a computed property, we can define the same function as a method instead.
For the end result, the two approaches are indeed exactly the same.
However, the difference is that **computed properties are cached based on their dependencies.**
A computed property will only re-evaluate when some of its dependencies have changed.
This means as long as `message` has not changed, multiple access to the `reversedMessage` computed property will immediately return the previously computed result without having to run the function again.

This also means the following computed property will never update, because `new Date()` is not a reactive dependency:

```java
@Computed
public String getNow() {
    return new Date().toString();
}
```

In comparison, a method invocation will **always** run the function whenever a re-render happens.

Why do we need caching?
Imagine we have an expensive computed property **A**, which requires looping through a huge Array and doing a lot of computations.
Then we may have other computed properties that in turn depend on **A**.
Without caching, we would be executing **A**’s getter many more times than necessary!
In cases where you do not want caching, use a method instead.

### Computed vs Watched Property

Vue does provide a more generic way to observe and react to data changes on a Vue instance: **watch properties**.
When you have some data that needs to change based on some other data, it is tempting to overuse `watch` - especially if you are coming from an AngularJS background.
However, it is often a better idea to use a computed property rather than an imperative `watch` callback. Consider this example:

```html
<div id="demo">{{ fullName }}</div>
```

```java
@Component
public class JohnSnowComponent implements IsVueComponent, HasCreated {
    @JsProperty String firstName;
    @JsProperty String lastName;
    @JsProperty String fullName;

    @Override
    public void created() {
        this.firstName = "John";
        this.lastName = "Snow";
        this.fullName = "John Snow";
    }
    
    @Watch("firstName")
    public void watchFirstName(String newValue) {
        this.fullName = newValue + " " + this.lastName;
    }
    
    @Watch("lastName")
    public void watchLastName(String newValue) {
        this.fullName = this.firstName + " " + this.newValue;
    }
}
```

The above code is imperative and repetitive. Compare it with a computed property version:

```java
@Component
public class JohnSnowComponent implements IsVueComponent {
    @JsProperty String firstName = "John";
    @JsProperty String lastName = "Snow";

    @Computed
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
```

Much better, isn't it?

### Computed Setter

Computed properties are by default getter-only, but you can also provide a setter when you need it:

```java
@Component
public class JohnSnowComponent implements IsVueComponent {
    @JsProperty String firstName = "John";
    @JsProperty String lastName = "Snow";

    @Computed
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Computed
    public void setFullName(String fullName) {
        String[] split = fullName.split(" ");
        this.firstName = split[0];
        this.lastName = split[1];
    }
}
```

Now when you run `this.fullName = 'John Doe'`, the setter will be invoked and `this.firstName` and `this.lastName` will be updated accordingly.

We also follow the Java bean naming convention, so `setFullName` is a setter for the `fullName` property.
If you need/want to, you can override the name of the property by passing it to the `@Computed` annotation:
```java
@Computed("myProperty")
public String someMethodName() {
    // Return something
}
```

## Watchers

While computed properties are more appropriate in most cases, there are times when a custom watcher is necessary.
That's why Vue provides a more generic way to react to data changes through the `watch` option.
This is most useful when you want to perform asynchronous or expensive operations in response to changing data.

In Vue GWT we use the `@Watch` annotation on a method to indicate it as a watcher.
The mandatory `value` attribute of the annotation indicate the name of the property to watch.

Here is an example:
```java
@Component
public class JohnSnowComponent implements IsVueComponent {
    @JsProperty String message = "Hello World!";

    @Watch("message")
    public void watchMessage(String newValue, String oldValue) {
        // Do something asynchronous
    }
}
```

<p class="warning-panel">
    Like for <code>v-model</code>, only <code>JsInterop</code> expression can be used as <code>value</code> for the <code>@Watch</code> annotation.
    This means any attribute from your Component and any of their attributes as long as they have the <code>@JsProperty</code> annotation.<br/>
    The following: <code>@Watch("todo.text")</code> won't work if the attribute <code>text</code> of the class <code>Todo</code> doesn't have the <code>@JsProperty</code> annotation.
</p>

In addition to the `watch` option, you can also use the imperative [vm.$watch API](https://vuejs.org/v2/api/#vm-watch).
This allow you to watch non `JsInterop` properties:
```java
asVue().$watch(
    () -> this.todo.getText(),
    (newValue, oldValue) -> {
        // Do something
    }
);
```