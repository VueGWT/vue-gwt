# Class and Style Bindings

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/class-and-style.html) and has been adapted for Vue GWT.*

A common need for data binding is manipulating an element's class list and its inline styles.
Since they are both attributes, we can use `v-bind` to handle them: we just need to calculate a final string with our expressions.
However, meddling with string concatenation is annoying and error-prone.
For this reason, Vue provides special enhancements when `v-bind` is used with `class` and `style`.
In addition to strings, the expressions can also evaluate to objects or arrays.

## Binding HTML Classes

### JSON Syntax

We can pass JSON to `v-bind:class` to dynamically toggle classes:

```html
<div v-bind:class="{ 'active': 'isActive' }"></div>
```

The above syntax means the presence of the `active` class will be determined by the [truthiness](https://developer.mozilla.org/en-US/docs/Glossary/Truthy) of the data property `isActive`.

Beware the quotes around both the keys and the values.
This is a Vue GWT specificity.
We actually parse this expression as JSON, and valid JSON must have quotes on both keys and values. 

You can have multiple classes toggled by having more fields in the object.
In addition, the `v-bind:class` directive can also co-exist with the plain `class` attribute.
So given the following template:

```html
<div class="static"
     v-bind:class="{ 'active': 'isActive', 'text-danger': 'hasError' }">
</div>
```

And the following data:

```java
this.isActive = true;
this.hasError = false;
```

It will render:

```html
<div class="static active"></div>
```

When `isActive` or `hasError` changes, the class list will be updated accordingly.
For example, if `hasError` becomes `true`, the class list will become `"static active text-danger"`.

You can also pass a `JsObject` Object with keys/values:

```html
<div v-bind:class="classObject"></div>
```

```java
this.classObject = new JsObject();
this.classObject.set("active", true);
this.classObject.set("text-danger", true);
```

A `JsObject` is a special Java class that will be represented as a native JS Object in the browser.
You can see it as a Map (key/values).

This will render the same result. We can also bind to a [computed property](./computed-and-watchers.md) that returns a JsObject.
This is a common and powerful pattern:

```html
<div v-bind:class="classObject"></div>
```
```java
@JsType
@Component
public class StylishComponent extends VueComponent
{
    public boolean isActive;
    public Error error;

    @Override
    public void created() {
        this.isActive = true;
        this.error = null;
    }

    @Computed
    public JsObject classObject() {
        JsObject classObject = new JsObject();
        classObject.set("active", this.isActive && this.error == null);
        classObject.set("text-danger", this.error != null && this.error.getType() == ErrorType.FATAL);
        return classObject;
    }
}
```

### Array Syntax

We can pass an array to `v-bind:class` to apply a list of classes:

```html
<div v-bind:class="[activeClass, errorClass]">
```
```java
this.activeClass = "active";
this.errorClass = "text-danger";
```

Which will render:

```html
<div class="active text-danger"></div>
```

If you would like to also toggle a class in the list conditionally, you can do it with a ternary expression:

```html
<div v-bind:class='[isActive ? activeClass : "", errorClass]'>
```

This will always apply `errorClass`, but will only apply `activeClass` when `isActive` is `true`.

However, this can be a bit verbose if you have multiple conditional classes.
That's why it's also possible to use the object syntax inside array syntax:

```html
<div v-bind:class="[{ 'active': 'isActive' }, errorClass]">
```

### GWT Styles

You should also check how to use [GWT GSS Styles](./gwt-integration/styles.md) in your app.

### With Components

**TODO**

In the meantime you can check the [Vue.js documentation on this](https://vuejs.org/v2/guide/class-and-style.html#With-Components).

## Binding Inline Styles

**TODO**

In the meantime you can check the [Vue.js documentation on this](https://vuejs.org/v2/guide/class-and-style.html#Binding-Inline-Styles).