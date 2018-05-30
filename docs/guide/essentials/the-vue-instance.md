# The Vue Instance

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/instance.html) and has been adapted for Vue GWT.*

## Creating a Vue Instance

Every Vue application starts by creating a new **root Vue instance**, for example with the `Vue.attach()` method:

```java
DemoComponent vm = Vue.attach("#container", DemoComponent.class);
```

Although not strictly associated with the [MVVM pattern](https://en.wikipedia.org/wiki/Model_View_ViewModel), Vue's design was partly inspired by it.
As a convention, we often use the variable `vm` (short for ViewModel) to refer to our Vue instances.

## @Component to Vue.js Data Model

In Vue.js you pass all the data you want to observe as the `data` option of your Vue constructor.
This `data` object is built for you by Vue GWT based on the `@JsProperty` of your Java Class.

For example this Vue Component:
```java
@Component
public class DemoComponent implements IsVueComponent {
    @JsProperty Todo todo;
}
```

Will have the following `data` object in it's Vue.js options:
```js
var data = {todo: null};
```

Each Vue instance **proxies** all the properties found in its `data` object.
When the values of those properties change, the view will "react", updating to match the new values.

So Vue.js will automatically be warned whenever you set teh value of a `@JsProperty` and recursively observe all the properties of the Object you set on it.

For example if somewhere in your component you do:
```java
this.todo = new Todo();
```

And your `Todo` class looks like this:
```java
public class Todo {
    private String text;

    public Todo(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }
}
```

Then `this.todo.text` is automatically observed.

::: warning
To avoid gotchas in Vue GWT, make sure to read about the [Reactivity System](reactivity-system.md).
:::

## Component Properties and Methods

In addition to data properties, Vue instances expose a number of useful instance properties and methods.
These properties and methods are prefixed with `$` to differentiate them from proxied data properties.

In Vue GWT these methods and properties are defined in `VueComponent`, usually with the same name.
Because your Components implements `IsVueComponent` you can access them in your Components using the `vue()` method.

For example:

```java
@Component
public class DemoComponent implements IsVueComponent, HasCreated {
    @JsProperty Todo todo;
    
    @Override
    public void created() {
        this.todo = new Todo();
        
        if (vue().$data.get("todo") == this.todo) {
            // true
        }
        
        vue().$watch(() -> this.todo, (newValue, oldValue) -> {
            // Todo has changed!
        });
    }
}
```

Consult the [Vue.js API reference](https://vuejs.org/v2/api/) for the full list of instance properties and methods.

## Instance Lifecycle Hooks

Each Vue instance goes through a series of initialization steps when it is created - for example, it needs to set up data observation, compile the template, mount the instance to the DOM, and update the DOM when data changes.
Along the way, it will also invoke some **lifecycle hooks**, which give us the opportunity to execute custom logic.
For example, the [`mounted`](https://vuejs.org/v2/api/#mounted) hook is called after the instance is mounted:

```java
@Component
public class DemoComponent implements IsVueComponent, HasMounted {
    @JsProperty Todo todo;
    
    @Override
    public void mounted() {
        // Hey there, I've been mounted!
    }
}
```

There are also other hooks which will be called at different stages of the instance's lifecycle, for example [`created`](https://vuejs.org/v2/api/#created), [`updated`](https://vuejs.org/v2/api/#updated), and [`destroyed`](https://vuejs.org/v2/api/#destroyed).
You may have been wondering where the concept of "controllers" lives in the Vue world and the answer is: there are no controllers.
Your custom logic for a component would be split among these lifecycle hooks.

## Lifecycle Diagram

Below is a diagram for the instance lifecycle. You don't need to fully understand everything going on right now, but this diagram will be helpful in the future.

*Diagram from the [official Vue.js documentation](https://vuejs.org/v2/guide/instance.html)*
![Lifecycle](https://vuejs.org/images/lifecycle.png)