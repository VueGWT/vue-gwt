# Your First Component

!INCLUDE "../dependencies.md"

## Simple Link Component

Let's create our first Component and use it in a real app.
We will call it `SimpleLinkComponent`.
This Component will simply display an html link.

### Declaring the Component

#### Java Class
To create our Component, we must create a Class annotated by `@Component` that extends `Vue`.

***SimpleLinkComponent.java***
```java
@JsType
@Component
public class SimpleLinkComponent extends Vue {
    public String linkName;
    
    @Override
    public void created() {
        this.linkName = "Hello Vue GWT!";
    }
}
```

The method `created` that is overridden in our Component is mandatory.
It will be called each time an instance of your Component is created.
You can see it as your Component "constructor".

#### Why public methods/properties?

Only `JsInterop` attributes and methods of our Class will be accessible in our template.

Meaning either:

 * Adding the `@JsType` annotation to our Class and setting attributes/methods to `public`
 * Adding the `@JsProperty`/`@JsMethod` annotation to each attribute/method and setting them to `protected`

In this documentation we chose to use the `@JsType` annotation and to set all our properties/methods to `public` for brevity.

#### HTML Template

We will then create a template for our `SimpleLinkComponent`.
It should have the same name as our Java Class file and be in the same package.
Vue GWT will detect it automatically and use it as our Component template.

***SimpleLinkComponent.html***

```html
<a href="https://github.com/Axellience/vue-gwt">
    {{ linkName }}
</a>
```

In this template we see that we use the `linkName` property from our Java Class between double curly braces.
This is called String interpolation.
At runtime, this expression is replaced by the value we have set in our Class.

Bellow we have instantiated an instance of our `SimpleLinkComponent`:

{% raw %}
<div class="example-container" data-name="simpleLinkComponent">
    <span id="simpleLinkComponent"></span>
</div>
{% endraw %}

The bindings in the Components are dynamic, each time the value of `linkName` changes in your Component instance, the value is updated automatically in the view.

**You can try it now** on this page! Open the console of your browser and type:
```
simpleLinkComponent.linkName = "This is working!";
```
This works with all the examples of this documentation.

### Instantiating the Component

How do we instantiate our `SimpleLinkComponent`?
First, in our GWT index page we add a div to attach the instance:

***GwtIndex.html***

```html
<div id="simpleLinkComponentContainer"></div>
```

Then when GWT starts, we need to bootstrap an instance of our `SimpleLinkComponent` and attach it in our `simpleLinkComponentContainer` div.

To do this we simply call the Vue.attach() static method and pass the selector of our container and a `VueConstructor` for our Component.

***RootGwtApp.java***

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        Vue.attach("#simpleLinkComponentContainer", SimpleLinkComponentConstructor.get());
    }
}
```

In typical application we only do this for the first root Component.
We can then use [Component composition](composing-with-components.md). 

#### Where does `SimpleLinkComponentConstructor` comes from?

It comes from the annotation processor.
It automatically processes classes annotated with `@Component` and generate the corresponding `VueConstructor` for you.

The generated `VueConstructor` class have a static method `get()` to get an instance of it.
This instance is the Java representation of a Vue constructor in JavaScript.

Behind the scene, our Java Component Class is converted to the options that Vue.js is expecting:
```javascript
{
    template: '<a href="https://github.com/Axellience/vue-gwt">{{ linkName }}</a>',
    data: {
        linkName: null
    },
    methods: {
        created: function() {
            this.linkName = "Hello Vue GWT!";
        }
    }
}
```

Those options are passed to the [`Vue.extend()`](https://vuejs.org/v2/api/#Vue-extend) JavaScript method.
The result is a `VueConstructor` we can use to generate new instances of Component.

## Binding Element Attributes

Our `SimpleLinkComponent` component works, but the target of our link is set in the template.
What if we wanted to declare it in our Component?

Luckily, in addition to text interpolation, we can also bind element attributes.
Let see how to do this with a new `LinkComponent`.
This one will allow us to set the link `href` attribute in our Java Class.

```java
@JsType
@Component
public class LinkComponent extends Vue {
    public String linkTarget;
    public String linkName;
    
    @Override
    public void created() {
        this.linkTarget = "https://github.com/Axellience/vue-gwt";
        this.linkName = "Hello Vue GWT!";
    }
}
```

```html
<a v-bind:href="linkTarget">
    {{ linkName }}
</a>
```

{% raw %}
<div class="example-container" data-name="linkComponent">
    <span id="linkComponent"></span>
</div>
{% endraw %}

Here we are encountering something new.
The `v-bind` attribute you are seeing is called a directive.

Directives are prefixed with `v-` to indicate that they are special attributes provided by Vue, and as you may have guessed, they apply special reactive behavior to the rendered DOM.
Here it is basically saying "keep this element’s href attribute up-to-date with the `linkTarget` property on the Vue instance."

**[Make it dynamic with Conditions and Loops](./conditional-and-loops.md)**.