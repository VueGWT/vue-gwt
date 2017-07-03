# Your first Vue GWT Component

If you already know GWT then you should be familiar with the concept of Widgets.
In Vue.js the equivalent of Widgets are Components.

Vue Components are like GWT Widgets, they are reusable and isolated pieces of your application.

Let's create our first Component and use it in a real app.
We will call it RootComponent.

***GwtIndex.html***

In our GWT index page we add a div to attach our root component.

```html
<div id="rootComponent"></div>
```

***RootComponent.java***

To create your Component, you must create a Class annotated by `@Component` and `@JsType` that extends `VueComponent`.
All the public attributes and methods of this class will be accessible in your template.

⚠️  Don't forget to add the `@JsType` annotation to your Class.
This ensure that GWT doesn't change your public properties name at compile time.

```java
@JsType
@Component
public class RootComponent extends VueComponent {
    public String message;
    
    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
}
```

***RootComponent.html***

We then create our Component template.
It should have the same name as our Java class file.
We place this file next to our Java class file.
VueGWT will detect it automatically and use it as our Component template.

```html
<div>
    {{ message }}
</div>
```

***RootGwtApp.java***

We need to bootstrap our `RootComponent` when GWT starts.
For that we simply call Vue.attach() and pass it our RootComponent class.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // When our GWT app starts, we start our Vue app.
        Vue.attach("#rootComponent", RootComponent.class);
    }
}
```

Behind the scene, the instance of our Component will be converted to the format that Vue.js is expecting:
```javascript
{
    el: "#rootComponent",
    template: "<div>{{ message }}</div>",
    data: {
        message: "Hello Vue GWT!"
    }
}
```

And passed down to `new Vue()`.


# Creating child Components

Every components can have children.

Let's create a child Component called `ChildComponent`.
We will then instantiate this component in our `RootComponent`.

This Component will display a message coming from a html text box.

***ChildComponent.java***
```java
@Component
@JsType
public class ChildComponent extends VueComponent {
    public String childMessage;
    
    @Override
    public void created() {
        this.childMessage = "Hello Vue GWT!";
    }
}
```

First we create an HTML file with our component template.
We add this file next to our Java class as before.

***ChildComponent.html***
```html
<div>
    <input type="text" v-model="childMessage"/>
    <p>{{ childMessage }}</p>
    <a href v-on:click="clearMessage">Clear message</a>
</div>
```

We will then make a few changes to our `RootComponent` to use `ChildComponent`:


***RootComponent.html***

```html
<div>
    <child></child>
</div>
```

***RootComponent.java***

```java
// ChildComponent is registered to be used in our RootComponent
// by passing it to the annotation
@Component(components = {ChildComponent.class})
@JsType
public class RootComponent extends VueComponent
{
}
```