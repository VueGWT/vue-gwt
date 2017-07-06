# Computed Properties and Watchers

!INCLUDE "dependencies.md"

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
@JsType
@Component
public class ReverseComponent extends VueComponent
{
    public String message;

    @Override
    public void created() {
        this.message = "Hello";
    }

    @Computed // Note the annotation that tells Vue GWT that this is a Computed Properties
    public String reversedMessage() {
        return new StringBuilder(message).reverse().toString();
    }
}
```

Result:

{% raw %}
<p class="example-container" data-name="reverseComponent">
    <span id="reverseComponent"></span>
</p>
{% endraw %}

Here we have declared a computed property `reversedMessage`.
The function we provided will be used as the getter function for the property `reversedMessage` of your template.

To avoid name collision in JsInterop Vue GWT rename the computed property in your template at compile time.
The property in your Vue instance in JavaScript is actually named `reversedMessage$computed`.

You can open the console and play with the example vm yourself.
The value of `reverseComponent.reversedMessage$computed` is always dependent on the value of `reverseComponent.message`.

```js
console.log(reverseComponent.reversedMessage$computed); // -> 'olleH'
reverseComponent.message = 'Goodbye';
console.log(reverseComponent.reversedMessage$computed); // -> 'eybdooG'
```

You can data-bind to computed properties in templates just like a normal property.
Vue is aware that `reversedMessage` depends on `message`, so it will update any bindings that depend on `reversedMessage` when `message` changes.
And the best part is that we've created this dependency relationship declaratively: the computed getter function has no side effects, which makes it easy to test and reason about.

### Computed Caching vs Methods

You may have noticed we can achieve the same result by invoking a method in the expression:

```html
<p>Reversed message: "{{ reverseMessage() }}"</p>
```

```java
@JsType
@Component
public class ReverseComponent extends VueComponent
{
    public String message;

    @Override
    public void created() {
        this.message = "Hello";
    }

    // Note that there a no more @Computed annotation
    public String reversedMessage() {
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
public String now() {
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
@JsType
@Component
public class JohnSnowComponent extends VueComponent
{
    public String firstName;
    public String lastName;
    public String fullName;

    @Override
    public void created() {
        this.firstName = "John";
        this.lastName = "Snow";
        this.fullName = "John Snow";
    }
    
    @Watch(propertyName = "firstName")
    public void watchFirstName(String newValue) {
        this.fullName = newValue + " " + this.lastName;
    }
    
    @Watch(propertyName = "lastName")
    public void watchLastName(String newValue) {
        this.fullName = this.firstName + " " + this.newValue;
    }
}
```

The above code is imperative and repetitive. Compare it with a computed property version:

```java
@JsType
@Component
public class JohnSnowComponent extends VueComponent
{
    public String firstName;
    public String lastName;

    @Override
    public void created() {
        this.firstName = "John";
        this.lastName = "Snow";
    }
    
    @Computed
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
```

Much better, isn't it?

### Computed Setter

Computed properties are by default getter-only, but you can also provide a setter when you need it:

```java
@JsType
@Component
public class JohnSnowComponent extends VueComponent
{
    public String firstName;
    public String lastName;

    @Override
    public void created() {
        this.firstName = "John";
        this.lastName = "Snow";
    }

    @Computed
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }

    @Computed(propertyName = "fullName", kind = ComputedKind.SETTER)
    public void setFullName(String fullName) {
        String[] split = fullName.split(" ");
        this.firstName = split[0];
        this.lastName = split[1];
    }
}
```

Now when you run `this.fullName = 'John Doe'`, the setter will be invoked and `this.firstName` and `this.lastName` will be updated accordingly.

## Watchers

While computed properties are more appropriate in most cases, there are times when a custom watcher is necessary.
That's why Vue provides a more generic way to react to data changes through the `watch` option.
This is most useful when you want to perform asynchronous or expensive operations in response to changing data.

In Vue GWT we use the `@Watch` annotation on a method to indicate it as a watcher.
The mandatory `propertyName` attribute of the annotation indicate the property to watch.

Here is an example:
```java
@JsType
@Component
public class JohnSnowComponent extends VueComponent
{
    public String message;

    @Override
    public void created() {
        this.message = "Hello World!";
    }

    @Watch(propertyName = "message")
    public void watchMessage(String newValue, String oldValue) {
        // Do something asynchroneous
    }
}
```

⚠️ Like for `v-model`, only @JsInterop expression can be used as `propertyName`.
This means any attribute from your Component and any of their attributes as long as they have the `@JsProperty` annotation.
This `@Computed(propertyName = "todo.text")` won't work if the attribute `text` of the class `Todo` doesn't have the `@JsProperty` annotation. 

In addition to the `watch` option, you can also use the imperative [vm.$watch API](https://vuejs.org/v2/api/#vm-watch).
This allow you to watch non `@JsInterop` properties:
```java
this.$watch(
    () -> this.todo.text,
    (newValue, oldValue) -> {
        // Do something
    }
);
```