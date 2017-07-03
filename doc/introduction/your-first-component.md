# Your First Component

## Simple Demo Component

Let's create our first Component and use it in a real app.
We will call it `DemoComponent`.

***DemoComponent.java***

To create your Component, you must create a Class annotated by `@Component` and `@JsType` that extends `VueComponent`.

All the `public` attributes and methods of this class will be accessible in your template.

```java
@JsType
@Component
public class DemoComponent extends VueComponent {
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

***DemoComponent.html***

We will then create a template for our `DemoComponent`.
It should have the same name as our Java class file.
We place this file next to our Java class file.
VueGWT will detect it automatically and use it as our Component template.

```html
<a src="https://github.com/Axellience/vue-gwt">
    {{ linkName }}
</a>
```

In this template we see that we use the `linkName` property from our Component between double curly braces.
This is called String interpolation.
At runtime, this expression will be replaced by the value we have set in your Component class.

This binding is dynamic, each time you will change the value of `linkName` in your Component instance, the value will update automatically in the template.

***GwtIndex.html***

In our GWT index page we add a div to attach an instance of our `DemoComponent`.

```html
<div id="demoComponentContainer"></div>
```

***RootGwtApp.java***

We have declared a Component, and the div to contain it.
Now need to bootstrap an instance of our `DemoComponent` when GWT starts and attach it in our `demoComponentContainer` div.
For that we simply call the Vue.attach() static method and pass the selector of our container and our `DemoComponent` class to it.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // When our GWT app starts, we start our Vue app.
        Vue.attach("#demoComponentContainer", DemoComponent.class);
    }
}
```

Behind the scene, the instance of our Java Component will be converted to the format that Vue.js is expecting:
```javascript
{
    el: "#demoComponent",
    template: "<div>{{ message }}</div>",
    data: {
        message: null
    },
    methods: {
        created: function() {
            this.message = "Hello Vue GWT!";
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
public class DemoComponent extends VueComponent {
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
<a v-bind:src="linkTarget">
    {{ linkName }}
</a>
```

Here we are encountering something new.
The `v-bind` attribute you are seeing is called a directive.

Directives are prefixed with `v-` to indicate that they are special attributes provided by Vue, and as you may have guessed, they apply special reactive behavior to the rendered DOM.
Here it is basically saying "keep this element’s href attribute up-to-date with the linkTarget property on the Vue instance."

**[Make it dynamic with Conditions and Loops](./conditional-and-loops.md)**