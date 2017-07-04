!INCLUDE "../dependencies.md"

# Your First Component

## Simple Example Component

Let's create our first Component and use it in a real app.
We will call it `Example1Component`.

***Example1Component.java***

To create your Component, you must create a Class annotated by `@Component` and `@JsType` that extends `VueComponent`.

All the `public` attributes and methods of this class will be accessible in your template.

```java
@JsType
@Component
public class Example1Component extends VueComponent {
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

***Example1Component.html***

We will then create a template for our `Example1Component`.
It should have the same name as our Java class file.
We place this file next to our Java class file.
VueGWT will detect it automatically and use it as our Component template.

```html
<a href="https://github.com/Axellience/vue-gwt">
    {{ linkName }}
</a>
```

In this template we see that we use the `linkName` property from our Component between double curly braces.
This is called String interpolation.
At runtime, this expression is replaced by the value we have set in your Component class:

{% raw %}
<p class="example-container" data-name="Live Example 1">
    <span id="example1"></span>
</p>
{% endraw %}

This binding is dynamic, each time you will change the value of `linkName` in your Component instance, the value will update automatically in the template.

**You can try it now** on this page! Open the console of your browser and type:
```
example1.linkName = "This is working!";
```
This works with all the examples of this documentation.

***GwtIndex.html***

In our GWT index page we add a div to attach an instance of our `Example1Component`.

```html
<div id="example1Container"></div>
```

***RootGwtApp.java***

We have declared a Component, and the div to contain it.
Now need to bootstrap an instance of our `Example1Component` when GWT starts and attach it in our `example1Container` div.
For that we simply call the Vue.attach() static method and pass the selector of our container and our `Example1Component` class to it.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // When our GWT app starts, we start our Vue app.
        Vue.attach("#example1Container", Example1Component.class);
    }
}
```

Behind the scene, the instance of our Java Component will be converted to the format that Vue.js is expecting:
```javascript
{
    el: "#example1Container",
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

And passed down to the `new Vue()` JavaScript method.

## Binding Element Attributes

Our demo component works, but the target of our link is set in the template.
What if we wanted to declare it in our Component?

Luckily, in addition to text interpolation, we can also bind element attributes:

```java
@JsType
@Component
public class Example2Component extends VueComponent {
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
<p class="example-container" data-name="Live Example 2">
    <span id="example2"></span>
</p>
{% endraw %}

Here we are encountering something new.
The `v-bind` attribute you are seeing is called a directive.

Directives are prefixed with `v-` to indicate that they are special attributes provided by Vue, and as you may have guessed, they apply special reactive behavior to the rendered DOM.
Here it is basically saying "keep this element’s href attribute up-to-date with the linkTarget property on the Vue instance."

**[Make it dynamic with Conditions and Loops](./conditional-and-loops.md)**