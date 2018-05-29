# Class and Style Bindings

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/class-and-style.html) and has been adapted for Vue GWT.*

A common need for data binding is manipulating an element's class list and its inline styles.
Since they are both attributes, we can use `v-bind` to handle them: we just need to calculate a final string with our expressions.
However, meddling with string concatenation is annoying and error-prone.
For this reason, Vue provides special enhancements when `v-bind` is used with `class` and `style`.
In addition to strings, the expressions can also evaluate to objects or arrays.

## Binding HTML Classes

### `map` Syntax

Vue.js supports passing JS object literals in the template.
As Vue GWT expressions must be Java, you can use the [`map` builder](../gwt-integration/js-interop.md#map).

We can pass the result from `map` to `v-bind:class` to dynamically toggle classes:

```html
<div v-bind:class='map("active", isActive)'></div>
```

The above syntax means the presence of the `active` class will be determined by the [truthiness](https://developer.mozilla.org/en-US/docs/Glossary/Truthy) of the data property `isActive`.

You can have multiple classes toggled by having more fields in the map.
For this you must use `e` to surround each key/value entry.

```html
<div v-bind:class='map( e("active", isActive), e("text-danger", hasError) )'></div>
```

In addition, the `v-bind:class` directive can also co-exist with the plain `class` attribute.
So given the following template:

```html
<div class="static"
     v-bind:class='map( e("active", isActive), e("text-danger", hasError) )'>
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

You can also pass a [`JsObject`](../gwt-integration/js-interop.md#js-object) Object with keys/values:

```html
<div v-bind:class="classObject"></div>
```

```java
this.classObject = JsObject.map( e("active", true), e("text-danger", true) );
```

A `JsObject` is a special Java class that will be represented as a native JS Object in the browser.
You can see it as a Map (key/values).

This will render the same result. We can also bind to a [computed property](computed-and-watchers.md) that returns a `JsObject`.
This is a common and powerful pattern:

```html
<div v-bind:class="classObject"></div>
```
```java
@Component
public class StylishComponent implements IsVueComponent {
    @JsProperty boolean isActive = true;
    @JsProperty Error error = null;

    @Computed
    public JsObject getClassObject() {
        return JsObject.map(
            e("active", this.isActive && this.error == null),
            e("text-danger", this.error != null && this.error.getType() == ErrorType.FATAL)
        );
    }
}
```

### Array Syntax

We can pass an array to `v-bind:class` to apply a list of classes:

```html
<div v-bind:class="array(activeClass, errorClass)">
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
<div v-bind:class='array(isActive ? activeClass : "", errorClass)'>
```

This will always apply `errorClass`, but will only apply `activeClass` when `isActive` is `true`.

However, this can be a bit verbose if you have multiple conditional classes.
That's why it's also possible to use the `map` syntax inside `array` syntax:

```html
<div v-bind:class='array(map("active", isActive), errorClass)'>
```

### GWT Styles

You should also check how to use [GWT GSS Styles](../gwt-integration/client-bundles-and-styles.md#styles) in your app.

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
<my-component v-bind:class='map("active", isActive)'></my-component>
```

When `isActive` is truthy, the rendered HTML will be:

```html
<p class="foo bar active">Hi</p>
```

## Binding Inline Styles

### Object Syntax

Vue.js supports Object syntax for `v-bind:style`.
As Vue GWT template expressions must be java, we don't support the JS object literal syntax.
Instead, you can use the map() function.
It will return a JS Object understandable by Vue.js.
Beware, in Java, string literals must use double quotes (").

For the CSS property names you can use either camelCase or kebab-case (use quotes with kebab-case):

```html
<div v-bind:style='map(e("color", activeColor), e("fontSize", fontSize + "px"))'>
    Here is some red big text.
</div>
```
```java
this.activeColor = "red";
this.fontSize = 20;
```

<div class="example-container" data-name="bindInlineStyleComponent">
    <span id="bindInlineStyleComponent"></span>
</div>

It is often a good idea to bind to a style object directly so that the template is cleaner:

```html
<div v-bind:style="styleObject"></div>
```
```java
@Component
public class StylishComponent implements IsVueComponent, HasCreated {
    @JsProperty JsObject<String> styleObject;

    @Override
    public void created() {
        this.styleObject = map(e("color", "red"), e("fontSize", "20px"));
    }
}
```

Again, the object syntax is often used in conjunction with computed properties that return objects.

### Array Syntax

The array syntax for `v-bind:style` allows you to apply multiple style objects to the same element:

```html
<div v-bind:style="array(baseStyles, overridingStyles)">
```

### Auto-prefixing

When you use a CSS property that requires [vendor prefixes](https://developer.mozilla.org/en-US/docs/Glossary/Vendor_Prefix) in `v-bind:style`, for example `transform`, Vue will automatically detect and add appropriate prefixes to the applied styles.

### Multiple Values

> 2.3.0+

Starting in 2.3 you can provide an array of multiple (prefixed) values to a style property, for example:

```html
<div v-bind:style='map("display", array("-webkit-box", "-ms-flexbox", "flex"))'>
```

This will only render the last value in the array which the browser supports.
In this example, it will render `display: flex` for browsers that support the unprefixed version of flexbox.

Vue GWT consider the expressions in JSON Object Values/Arrays to be Java expression.
If you actually want to pass a String you have to add escaped quotes around your expression.