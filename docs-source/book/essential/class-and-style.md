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
<div v-bind:class="{ active: isActive }"></div>
```

The above syntax means the presence of the `active` class will be determined by the [truthiness](https://developer.mozilla.org/en-US/docs/Glossary/Truthy) of the data property `isActive`.

If your expression is not just a literal (it contains a space) you **MUST** put it between quotes.
For example:

```html
<div v-bind:class="{ active: 'count > 5' }"></div>
```

This is a specificity from Vue GWT.
It's because those expressions are parsed as very loose JSON.
We are using the [jodd JSON parser](http://jodd.org/doc/json/json-parser.html) in it's loose mode.
It's pretty forgiving, but still won't accept values with spaces without quotes.

You can have multiple classes toggled by having more fields in the object.
In addition, the `v-bind:class` directive can also co-exist with the plain `class` attribute.
So given the following template:

```html
<div class="static"
     v-bind:class="{ active: isActive, 'text-danger': hasError }">
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

This will render the same result. We can also bind to a [computed property](computed-and-watchers.md) that returns a JsObject.
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
    public JsObject getClassObject() {
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
<div v-bind:class='["isActive ? activeClass : \"\"", errorClass]'>
```

This will always apply `errorClass`, but will only apply `activeClass` when `isActive` is `true`.

Notice that in this case we have to put the expression between quotes.
This is for the same reason as with expression in JSON.
We use [jodd JSON parser](http://jodd.org/doc/json/json-parser.html) to parse Array expressions too.

However, this can be a bit verbose if you have multiple conditional classes.
That's why it's also possible to use the object syntax inside array syntax:

```html
<div v-bind:class="[{ active: isActive }, errorClass]">
```

### GWT Styles

You should also check how to use [GWT GSS Styles](../gwt-integration/styles.md) in your app.

### With Components

When you use the `class` attribute on a custom component, those classes will be added to the component's root element.
Existing classes on this element will not be overwritten.

For example, if you declare a Component `my-component` with this template:

```html
<p class="foo bar">Hi</p>
```

Then add some classes when using it:

```html
<my-component class="baz boo"></my-component>
```

The rendered HTML will be:

```html
<p class="foo bar baz boo">Hi</p>
```

The same is true for class bindings:

```html
<my-component v-bind:class="{ active: isActive }"></my-component>
```

When `isActive` is truthy, the rendered HTML will be:

```html
<p class="foo bar active">Hi</p>
```

## Binding Inline Styles

### Object Syntax

The object syntax for `v-bind:style` is pretty straightforward - it looks almost like CSS, except it's a JavaScript object. You can use either camelCase or kebab-case (use quotes with kebab-case) for the CSS property names:

```html
<div v-bind:style='{color: activeColor, fontSize: "fontSize + \"px\"" }'>
    Here is some red big text.
</div>
```
```java
this.activeColor = "red";
this.fontSize = 20;
```

{% raw %}
<div class="example-container" data-name="bindInlineStyleComponent">
    <span id="bindInlineStyleComponent"></span>
</div>
{% endraw %}

It is often a good idea to bind to a style object directly so that the template is cleaner:

```html
<div v-bind:style="styleObject"></div>
```
```java
@JsType
@Component
public class StylishComponent extends VueComponent
{
    public boolean styleObject;

    @Override
    public void created() {
        this.styleObject = new JsObject();
        this.styleObject.set("color", "red");
        this.styleObject.set("fontSize", "20px");
    }
}
```

Again, the object syntax is often used in conjunction with computed properties that return objects.

### Array Syntax

The array syntax for `v-bind:style` allows you to apply multiple style objects to the same element:

```html
<div v-bind:style="[baseStyles, overridingStyles]">
```

### Auto-prefixing

When you use a CSS property that requires [vendor prefixes](https://developer.mozilla.org/en-US/docs/Glossary/Vendor_Prefix) in `v-bind:style`, for example `transform`, Vue will automatically detect and add appropriate prefixes to the applied styles.

### Multiple Values

> 2.3.0+

Starting in 2.3 you can provide an array of multiple (prefixed) values to a style property, for example:

```html
<div v-bind:style='{ display: ["\"-webkit-box\"", "\"-ms-flexbox\"", "\"flex\""] }'>
```

This will only render the last value in the array which the browser supports.
In this example, it will render `display: flex` for browsers that support the unprefixed version of flexbox.

Vue GWT consider the expressions in JSON Object Values/Arrays to be Java expression.
If you actually want to pass a String you have to add escaped quotes around your expression.