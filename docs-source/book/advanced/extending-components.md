# Extending Components

!INCLUDE "../dependencies.md"

In Vue.JS it's possible to [extend existing components](https://vuejs.org/v2/api/#extends).
In Vue GWT we can use Java inheritance to extend a base Component.

This allows you to reuse some behavior across Components.

<p class="warning-panel">
Inheritance is a powerful tool, but don't use it too much.
Try to limit to a single level of inheritence, and use it only when necessary.
</p>

## Extending an existing Java Component {#extending-java-component}

### The Parent Component

Let's say we have the following Component:

```java
@Component
public abstract class ParentComponent extends VueComponent {
    @JsProperty String parentMessage = "This is a message from the parent";

    @JsMethod
    public int parentMultiplyBy2(int value) {
        return value * 2;
    }

    @Computed
    public String getParentComputed() {
        return "Computed Message | " + this.parentMessage;
    }
}
```

It's just a regular Vue GWT component.
Only remarkable thing is it is abstract, which tells Vue GWT that it doesn't have a template.

### The Child Component

Let's extend our `ParentComponent`.
For this we just have to use classic Java inheritance.

```java
@Component
public class ChildJavaComponent extends ParentJsComponent implements HasCreated {
    @JsProperty int childValue;

    public void created() {
        this.childValue = 10;
    }

    public int childMultiplyBy10(int value) {
        return value * 10;
    }

    public int childMultiplyBy4(int value) {
        return this.parentMultiplyBy2(this.parentMultiplyBy2(value));
    }

    @Computed
    public String getChildComputed() {
        return "Child Computed | " + this.childValue;
    }
}
```

We can then use our parent methods and properties in the Child template:

```html
<div>
    <div>Parent Message: {{ parentMessage }}</div>
    <div>Parent Method (value*2): {{ parentMultiplyBy2(childValue) }}</div>
    <div>Parent Computed Property: {{ parentComputed }}</div>

    <div>Child Value: {{ childValue }}</div>
    <div>Child Method (value*10): {{ childMultiplyBy10(childValue) }}</div>
    <div>Child Method using Parent Method (value*4): {{ childMultiplyBy4(childValue) }}</div>
    <div>Child Computed Property: {{ childComputed }}</div>
</div>
```

And here is the traditional live example for proof 😉  :

<div class="example-container" data-name="extendJavaComponent">
    <span id="extendJavaComponent"></span>
</div>

## Extending an existing JS Component {#extending-js-component}

Has you saw in [integrating with JS Components](integrating-with-js-components.md) that we can use JS Components in Vue GWT.

Well, we can also extends them!

If we have this Component:

```js
window.ParentJsComponent = Vue.extend({
    data: function () {
        return {
            parentMessage: "This is a message from the Parent"
        };
    },
    methods: {
        parentMultiplyBy2: function (value) {
            return value * 2;
        }
    },
    computed: {
        parentComputed: function () {
            return "Computed Message | " + this.parentMessage;
        }
    }
});
```

We declare this Java interface:

```java
@JsComponent
@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class ParentJsComponent extends VueComponent {
    public String parentMessage;

    public native int parentMultiplyBy2(int value);

    @Computed
    public native String getParentComputed();
}
```

We can then just inherit from it the same way we did for our Java Component:

```java
@Component
public class ChildJavaComponent extends ParentJsComponent {
    @JsProperty int childValue;

    public ChildJavaComponent() {
        this.childValue = 10;
    }

    public int childMultiplyBy10(int value) {
        return value * 10;
    }

    public int childMultiplyBy4(int value) {
        return this.parentMultiplyBy2(this.parentMultiplyBy2(value));
    }

    @Computed
    public String getChildComputed() {
        return "Child Computed | " + this.childValue;
    }
}
```

```html
<div>
    <div>JS Parent Message: {{ parentMessage }}</div>
    <div>JS Parent Method (value*2): {{ parentMultiplyBy2(childValue) }}</div>
    <div>JS Parent Computed Property: {{ parentComputed }}</div>

    <div>Child Value: {{ childValue }}</div>
    <div>Child Method (value*10): {{ childMultiplyBy10(childValue) }}</div>
    <div>Child Method using JS Parent Method (value*4): {{ childMultiplyBy4(childValue) }}</div>
    <div>Child Computed Property: {{ childComputed }}</div>
</div>
```

Once instantiated:

{% raw %}
<div class="example-container" data-name="extendJsComponent">
    <span id="extendJsComponent"></span>
</div>
{% endraw %}