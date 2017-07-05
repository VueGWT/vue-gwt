# Your First Component

!INCLUDE "../dependencies.md"

## Simple Link Component

Let's create our first Component and use it in a real app.
We will call it `SimpleLinkComponent`.
This Component will simply display an html link.

### Declaring the Component


#### Java Class
To create our Component, we must create a Class annotated by `@Component` and `@JsType` that extends `VueComponent`.

All the `public` attributes and methods of this Class will be accessible in our template.

***SimpleLinkComponent.java***
```java
@JsType
@Component
public class SimpleLinkComponent extends VueComponent {
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

#### HTML Template

We will then create a template for our `SimpleLinkComponent`.
It should have the same name as our Java Class file and be in the same package.
VueGWT will detect it automatically and use it as our Component template.

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
<p class="example-container" data-name="simpleLinkComponent">
    <span id="simpleLinkComponent"></span>
</p>
{% endraw %}

The bindings in the Components are dynamic, each time the value of `linkName` changes in your Component instance, the value is updated automatically in the view.

**You can try it now** on this page! Open the console of your browser and type:
```
simpleLinkComponent.linkName = "This is working!";
```
This works with all the examples of this documentation.

### Instantiating the Component

How do we create the instance of our `SimpleLinkComponent`?
First, in our GWT index page we add a div to attach the instance:

***GwtIndex.html***

```html
<div id="simpleLinkComponentContainer"></div>
```

Then when GWT starts, we need to bootstrap an instance of our `SimpleLinkComponent` and attach it in our `simpleLinkComponentContainer` div.

To do this we simply call the Vue.attach() static method and pass the selector of our container and our `SimpleLinkComponent` class to it.

***RootGwtApp.java***

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        Vue.attach("#simpleLinkComponentContainer", SimpleLinkComponent.class);
    }
}
```

Behind the scene, the instance of our Java Component will be converted to the format that Vue.js is expecting:
```javascript
{
    el: "#simpleLinkComponentContainer",
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

Our `SimpleLinkComponent` component works, but the target of our link is set in the template.
What if we wanted to declare it in our Component?

Luckily, in addition to text interpolation, we can also bind element attributes.
Let see how to do this with a new `LinkComponent`.
This one will allow us to set the link `href` attribute in our Java Class.

```java
@JsType
@Component
public class LinkComponent extends VueComponent {
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
<p class="example-container" data-name="linkComponent">
    <span id="linkComponent"></span>
</p>
{% endraw %}

Here we are encountering something new.
The `v-bind` attribute you are seeing is called a directive.

Directives are prefixed with `v-` to indicate that they are special attributes provided by Vue, and as you may have guessed, they apply special reactive behavior to the rendered DOM.
Here it is basically saying "keep this element’s href attribute up-to-date with the `linkTarget` property on the Vue instance."

**[Make it dynamic with Conditions and Loops](./conditional-and-loops.md)**