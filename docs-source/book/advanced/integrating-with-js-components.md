# Integrating With JS Components

!INCLUDE "../dependencies.md"

## Using JS Components in Java {#using-js-components-in-java}

Vue GWT allows us to make our nice Java Components.
But the Vue.js ecosystem is vast, it would be a shame if we couldn't take advantage of existing JS Components.
Well let's see how we can do that!

<p class="info-panel">
    This examples declare Vue components directly in the index.html.
    For this to work, you need Vue loaded before you declare them.
    You can use <code>VueGWT.initWithoutVueLib()</code> in your GWT app, and a JS dependency to Vue in your index.html instead. 
</p>

### Instantiating a JS Component in Java

Let's say we have the following JS Component declared in JavaScript:

```js
window.FullJsComponent = Vue.extend({
    template: "<div>I Come In Peace From the JS World.</div>"
});
```

Let's try see how to instantiate it in Java:

```java
// First, we get the VueJsConstructor from Window
VueJsConstructor vueJsConstructor = ((JsPropertyMap<VueJsConstructor>) DomGlobal.window).get("FullJsComponent");

// We can then manipulate it exactly like
// our VueJsConstructor generated from our Java Components
IsVueComponent myFullJsComponent = vueJsConstructor.instantiate();
myFullJsComponent.asVue().$mount("#fullJsComponent");
```

And here it is live:

{% raw %}
<div class="example-container" data-name="fullJsComponent">
    <span id="fullJsComponent"></span>
</div>
{% endraw %}

### Declaring our JS Component Interface

Our first JS component was rather easy, let's say we now have this one:

```js
window.FullJsWithMethodsComponent = Vue.extend({
    template: "<div>My Value: {{ value }}. My Value x2: {{ multiplyBy2(value) }}</div>",
    data: function () {
        return {
            value: 10
        }
    },
    methods: {
        multiplyBy2: function (value) {
            return value * 2;
        }
    }
});
```

We would like to manipulate our instances from Java.
For this we need some kind of Java definition.

Let's do it for our `FullJsWithMethodsComponent`:

```java
@JsComponent("FullJsWithMethodsComponent")
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Function")
public class FullJsWithMethodsComponent implements IsVueComponent {
    public int value;

    public native int multiplyBy2(int value);
}
```

Looks pretty similar to our Java Components, but there a few key differences:

* We use the `@JsComponent` annotation instead of `@Component`
* We have to use the `@JsType` to tell GWT this is a native `Function`
* Our JS methods are declared as `native` in our Java class.
We don't need to provide their implementations, they will come from the JS Component.
* You only need to declare what you want to interact with in the Class.
If you don't need to call some methods or access some attributes from Java, you don't have to declare them.

Because of the `@JsComponent` annotation, Vue GWT generates a `VueFactory` class for us, just like with our Java components.

So we can do:

```java
FullJsWithMethodsComponent myComponent = FullJsWithMethodsComponentFactory.get().create();
myComponent.asVue().$mount("#fullJsWithMethodComponent");
JsTools.log(myComponent.value); // 10
JsTools.log(myComponent.multiplyBy2(25)); // 50
myComponent.value = 15; // Change the value in the instance of our Component
```

Bellow is the component instantiated, without any change to it's original value.
You can interact with it in the console, it's just a regular Vue instance.

{% raw %}
<div class="example-container" data-name="fullJsWithMethodsComponent">
    <span id="fullJsWithMethodsComponent"></span>
</div>
{% endraw %}

#### How does `@JsComponent` find the existing JS Component?

You may have noticed we also pass `"FullJsWithMethodsComponent"` to our `@JsComponent` annotation.
This is used to find the Js Component on `window`.

So if you put `@JsComponent("MyVueLib.MyComponent")`, it will link your `JsComponent` class to: `window.MyVueLib.MyComponent`.

#### What if the JS library exposes a Component options object?

Some libraries like [Vue ChartJS](http://vue-chartjs.org/) exposes a Component options object and not a Component Constructor.
You can use the same method, and Vue GWT will automatically create a Vue Component constructor from those options for you.
The only change is you have to tell GWT that the instance is an `Object` and not a `Function`:

```java
@JsComponent("VueChartJs.Line")
@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class AbstractVueLineChartComponent implements IsVueComponent
```

### Using Our JS Component in a Java One

You can use your JS Component just like the Java ones.
Which means you can also pass them to the `@Component` annotation [as local components](../essential/components.md#using-components):

```java
@Component(components = FullJsWithMethodsComponent.class)
```

You can then simply use it in your Java Component template:

```html
<div>
    <full-js-with-methods></full-js-with-methods>
</div>
```

### Extending a JS Component

We can also [extend JS Components](../composition/extending-components.md#extending-js-component) (this is required for some Vue plugins for example).

## Using our Java Components in JS {#using-java-components-in-js}

It's possible to get our Java Components to use them in a JS application.

The `VueGWT` global object has a `getJsConstructor` that allow you to retrieve the JS Constructor for your Java Component.
Beware this object only exist after your GWT app loading (which is async).
 
```js
const MyJavaComponent = VueGWT.getJsConstructor("com.package.MyJavaComponent");
const vm = new MyJavaComponent();
vm.$mount("#javaComponent");
```

`MyJavaComponent` is just a regular Vue Constructor, you can use it anywhere, you can extend it etc...