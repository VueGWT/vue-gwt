# The Vue Instance

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/instance.html) and has been adapted for Vue GWT.*

## The `vm`

Every Vue vm is bootstrapped by creating a **root Vue instance**, for example with the `Vue.attach()` method:

```java
DemoComponent vm = Vue.attach("#container", DemoComponent.class);
```

Although not strictly associated with the [MVVM pattern](https://en.wikipedia.org/wiki/Model_View_ViewModel), Vue's design was partly inspired by it.
As a convention, we often use the variable `vm` (short for ViewModel) to refer to our Vue instances.

## Factory

Vue GWT generate a `VueFactory` for each of your Vue Components using annotation processing.
Using this generated `VueFactory` we can generate several instance of our Vue Component:

```java
DemoComponentFactory demoFactory = DemoComponentFactory.get();

DemoComponent vm1 = demoFactory.create();
DemoComponent vm2 = demoFactory.create();
DemoComponent vm3 = demoFactory.create();
vm1.$mount("#myContainer1");
vm2.$mount("#myContainer2");
vm3.$mount("#myContainer3");
```

Although it is possible to create extended instances imperatively, most of the time it is recommended to compose them declaratively in templates as custom elements.
For now, you just need to know that all Vue components are essentially extended Vue instances.
Vue GWT configure those instance for you, but in the browser they really are just regular Vue instances.

## Observation

Let's talk a little about how Vue.js deals with observation.

### @Component to Vue.js Data Model

In Vue.js you pass all the data you want to observe as the `data` option of your Vue constructor.
This `data` object is built for you by Vue GWT based on the `@JsProperty` of your Java Class.

For example this Vue Component:
```java
@Component
public class DemoComponent implements IsVueComponent {
    @JsProperty Todo todo;
}
```

Will have the following `data` object in it's Vue options:
```js
var data = {todo: null};
```

Each Vue instance **proxies** all the properties found in its `data` object.

So Vue.js will automatically be warned whenever you set `this.todo`.
It will then recursively observe all the properties of the Object you set on this property.

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


### ⚠️ Important Note on Vue GWT Observation
In Vue.js only these proxied (observed) properties are **reactive**.
If you attach a new property to the JS instance after it has been created, it will not trigger any view updates.

When you add your Objects to your Component all their properties **must** already be set in the JS world.
This means that they must have a value set, even if null before being set in your Component.

In this case the `text` property **WON'T** be observed:
```java
public class Todo {
    private String text;
    public Todo() {}
    
    public void setText(String text) { this.text = text; }
}

@Component
public class MyComponent implements IsVueComponent, HasCreated {
    @JsProperty todo;
    
    @Override
    public void created() {
        todo = new Todo();
        todo.setText("Bob"); // Won't update the Vue :(
    }
}
```

But in these cases it will **WILL**:
```java
// Any of those
public class Todo {
    private String text = null;
    ...
}

public class Todo {
    private String text;
    public Todo() {
        this.text = null;
    }
    ...
}

public class Todo {
    private String text = "Default Value";
    ...
}

public class Todo {
    private String text;
    public Todo() {
        this.text = "Default Value";
    }
    ...
}

public class Todo {
    private String text;
    public Todo(String defaultText) {
        this.text = defaultText;
    }
    ...
}

// The text property will be observable in your Component
@Component
public class MyComponent implements IsVueComponent, HasCreated {
    @JsProperty todo;
    
    @Override
    public void created() {
        todo = new Todo();
        todo.setText("Bob"); // Will update the Vue! 
    }
}
```

If you don't have the control over the Java class, you can also set a value before attaching your object to your Component data model.

```java
// This class comes from some library, I can't change it! How do I make text reactive?
public class Todo {
    private String text;
    public Todo() {}
    
    public void setText(String text) { this.text = text; }
}

@Component
public class MyComponent implements IsVueComponent, HasCreated {
    @JsProperty todo;
    
    @Override
    public void created() {
        Todo myTodo = new Todo(); // We store in a local variable
        myTodo.setText("Bob"); // We set the value, this define the property
        
        this.todo = myTodo; // We attach myTodo, it starts being observed, Bob displays in the Vue!
        todo.setText("Mickael"); // Will update the Vue to Mickael! 
    }
}
```

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

## Instance Lifecycle Hooks {#lifecycle-hooks}

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