# Getting Started

<p class="info-panel">
    This documentation comes from <a href="https://vuejs.org/v2/guide/">the official Vue.js guide</a> and has been adapted for Vue GWT.
</p>

If you already know GWT then you should be familiar with the concept of Widgets.
In Vue.js the equivalent of Widgets are Components.

Vue Components are like GWT Widgets, they are reusable and isolated pieces of your application.

## Your First Component {#your-first-component}

!INCLUDE "../dependencies.md"

### Simple Link Component

Let's create our first Component and use it in a real app.
We will call it `SimpleLinkComponent`.
This Component will simply display an html link.

#### Declaring the Component

##### Java Class
To create our Component, we must create a Class annotated by `@Component` that extends `VueComponent`.

***SimpleLinkComponent.java***
```java
@Component
public class SimpleLinkComponent extends VueComponent {
    @JsProperty String linkName;
    
    @Override
    public void created() {
        this.linkName = "Hello Vue GWT!";
    }
}
```

The method `created` that is overridden in our Component is mandatory.
It will be called each time an instance of your Component is created.
You can see it as your Component "constructor".

##### Why `@JsProperty`?

Only `JsInterop` attributes of our Class will be accessible in our template.

Meaning either:

 * Adding the `@JsType` annotation to our Class and setting attributes to `public`
 * Adding the `@JsProperty` annotation to each attribute and setting them to at least `protected`.

In this documentation we chose to use the `@JsProperty` annotation on properties.

##### HTML Template

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

#### Instantiating the Component

How do we instantiate our `SimpleLinkComponent`?
First, in our GWT index page we add a div to attach the instance:

***GwtIndex.html***

```html
<div id="simpleLinkComponentContainer"></div>
```

Then when GWT starts, we need to bootstrap an instance of our `SimpleLinkComponent` and attach it in our `simpleLinkComponentContainer` div.

To do this we simply call the Vue.attach() static method and pass the selector of our container and the class of our Component.

***RootGwtApp.java***

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        SimpleLinkComponent simpleLinkComponent = Vue.attach("#simpleLinkComponentContainer", SimpleLinkComponent.class);
    }
}
```

In typical application we only do this for the first root Component.
We can then use [Component composition](README.md#composing-with-components).

##### Why can't I just do `new SimpleLinkComponent()`?

Behind the scene, our Java `VueComponent` Class is converted to the options that Vue.js is expecting:
```javascript
{
    render: function() {
    	...
    },
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
The result is a `VueJsConstructor` we can use to generate new instances of our `VueComponent`.

When you call `attach`, `VueGWT` get this `VueJsConstructor` for you and use it to create a new instance of your Component.

### Binding Element Attributes

Our `SimpleLinkComponent` component works, but the target of our link is set in the template.
What if we wanted to declare it in our Component?

Luckily, in addition to text interpolation, we can also bind element attributes.
Let see how to do this with a new `LinkComponent`.
This one will allow us to set the link `href` attribute in our Java Class.

```java
@Component
public class LinkComponent extends VueComponent {
    @JsProperty String linkTarget;
    @JsProperty String linkName;
    
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

## Conditional and Loops {#conditional-and-loop}

### Conditional

It’s quite simple to toggle the presence of an element too.
For this we can use the `v-if` directive.
Let's check this with a small example:

```html
<div v-if="visible">
    The property "visible" is set to true!
</div>
```

```java
@Component
public class CanHideComponent extends VueComponent {
    @JsProperty boolean visible;

    @Override
    public void created() {
        this.visible = true;
    }
}
```

As you can see bellow the `div` is created if the property `visible` of the Component instance is set to `true`.

{% raw %}
<div class="example-container" data-name="canHideComponent">
    <span id="canHideComponent"></span>
</div>
{% endraw %}

You can try to interact in your browser console by typing:
```
canHideComponent.visible = false;
```
The `div` will be removed from the DOM.

This example demonstrates that we can bind data to not only text and attributes, but also the structure of the DOM.

### Loops

There are quite a few other directives, each with its own special functionality.
For example, the `v-for` directive can be used for displaying a list of items.

Let's assume we have a `Todo` POJO:
```java
public class Todo
{
    private String text;

    public Todo(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }
}
```

We will then create a list of `Todo` in a `SimpleTodoListComponent`.

Vue GWT observe Java Collections for you.
For now this work for `List`, `Set` and `Map`.

```java
@Component
public class SimpleTodoListComponent extends VueComponent {
    @JsProperty List<Todo> todos;
    
    @Override
    public void created() {
        this.todos = new LinkedList<>();
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}
```

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<ol>
    <li v-for="Todo todo in todos">
        {{ todo.getText() }}
    </li>
</ol>
```

Another difference with Vue.js is you must indicate your loop variable type.
This is because Vue GWT compile templates expressions to Java and so needs the type information.
You can import Java types in your template by using the `vue-gwt:import` element.

{% raw %}
<div class="example-container" data-name="simpleTodoListComponent">
    <span id="simpleTodoListComponent"></span>
</div>
{% endraw %}

As the `Todo` class does not have the `@JsInterop` annotation it's not possible to create new Todos from the JavaScript console.
But you can try removing a Todo in console:
```
simpleTodoListComponent.todos.shift();
```

## Handling User Input {#handling-user-input}

### Events
To let users interact with your app, we can use the `v-on` directive to attach event listeners that invoke methods on our Vue instances:

```html
<div>
    <p>{{ message }}</p>
    <button v-on:click="addExclamationMark()">Add an explanation mark</button>
</div>
```

```java
@Component
public class ExclamationComponent extends VueComponent {
    @JsProperty String message;
    
    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
    
    public void addExclamationMark() {
        this.message += "!";
    }
}
```

{% raw %}
<div class="example-container" data-name="exclamationComponent">
    <span id="exclamationComponent"></span>
</div>
{% endraw %}

Note in the addExclamationMark method we simply update the state of our app without touching the DOM - all DOM manipulations are handled by Vue, and the code you write is focused on the underlying logic.

You can even call the addExclamationMark from your browser's console:
```
exclamationComponent.addExclamationMark();
```

### The v-model Directive

Vue also provides the [v-model directive](../forms.md) that makes two-way binding between form input and app state a breeze:
```html
<div>
    <p>{{ message }}</p>
    <input type="text" v-model="message">
</div>
```

```java
@Component
public class MessageComponent extends VueComponent {
    @JsProperty String message;
    
    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
}
```

Changing the value of the input will automatically update the content of our message property.
Changing the value of our Java property will change the value of input:

{% raw %}
<div class="example-container" data-name="messageComponent">
    <span id="messageComponent"></span>
</div>
{% endraw %}

<p class="warning-panel">
    It's important to note that for now in Vue GWT only <code>JsInterop</code> properties can be used directly in <code>v-model</code>.
    <a href="../forms.html">Check here to see why and get solutions</a>.
</p>

A real world application is never just one Component, let's see how to **[compose Components together](README.md#composing-with-components)**.

## Composing with Components {#composing-with-components}

The component system is another important concept in Vue, because it’s an abstraction that allows us to build large-scale applications composed of small, self-contained, and often reusable components.
If we think about it, almost any type of application interface can be abstracted into a tree of components.

### Creating a Child Component

Let's create a child Component called `TodoComponent`.
We will then instantiate this component in an `ParentComponent`.

This Component will display a simple message.

***TodoComponent.java***

We first create a Class like for our previous examples.

```java
@Component
public class TodoComponent extends VueComponent {
    @Override
    public void created() {}
}
```

We then create an HTML file with our Component template.
We add this file next to our Java class as before.

***TodoComponent.html***
```html
<li>
    I'm an amazing Todo!
</li>
```

***ParentComponent.java***

We will then create our `ParentComponent` that uses `TodoComponent`.

We first register `TodoComponent` to be used in our `ParentComponent` by passing it to the `@Component` annotation.

```java
@Component(components = {TodoComponent.class})
public class ParentComponent extends VueComponent {
    @Override
    public void created() {}
}
```

***ParentComponent.html***

Now you can compose it in our `ParentComponent`’s template:

```html
<div>
    <ol>
        <todo></todo>
    </ol>
</div>
```
{% raw %}
<div class="example-container" data-name="parentComponent">
    <span id="parentComponent"></span>
</div>
{% endraw %}

### Passing Properties to Children Components

Right now we render the same text for every todo, which is not super interesting.
We should be able to pass data from the parent scope into child components.

Let’s modify our `TodoComponent` to make it accept a property.

***TodoComponent.java***
```java
@Component
public class TodoComponent extends VueComponent {
    @Prop
    @JsProperty
    Todo todo;
    
    @Override
    public void created() {}
}
```

The `@Prop` annotation tells Vue GWT that our `todo` property will be passed from a parent component.

Be careful, you still need to use the `@JsProperty` to tell VueGWT to not rename this property.

***TodoComponent.html***
```html
<li>
    {{ todo.getText() }}
</li>
```

***TodoListComponent.java***

Let's see how to pass this value from the parent.

We create a list of `Todos` like before.
Let's call it `TodoListComponent`:

```java
@Component(components = {TodoComponent.class})
public class TodoListComponent extends VueComponent {
    @JsProperty List<Todo> todos;
    
    @Override
    public void created() {
        this.todos = new LinkedList<>();
        this.todos.add(new Todo("Learn Java"));
        this.todos.add(new Todo("Learn Vue GWT"));
        this.todos.add(new Todo("Build something awesome"));
    }
}
```

***TodoListComponent.html***

And now we are able to pass the `Todo` into each repeated component using `v-bind`:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<div>
    <ol>
        <todo v-for="Todo todo in todos" v-bind:todo="todo"></todo>
    </ol>
</div>
```
{% raw %}
<div class="example-container" data-name="todoListComponent">
    <span id="todoListComponent"></span>
</div>
{% endraw %}

This is just a contrived example, but we have managed to separate our app into two smaller units, and the child is reasonably well-decoupled from the parent via the props interface.
We can now further improve our <todo-item> component with more complex template and logic without affecting the parent app.

In a large application, it is necessary to divide the whole app into components to make development manageable.
We will talk a lot more about components later in the guide, but here’s an (imaginary) example of what an app’s template might look like with components:

```html
<div id="app">
  <app-nav></app-nav>
  <app-view>
    <app-sidebar></app-sidebar>
    <app-content></app-content>
  </app-view>
</div>
```

### How Is the Component HTML Name Set?

The name of the html element for our Component in the template (here, `todo`) is determined using the Component's Class name.

The name is converted from CamelCase to kebab-case.
If the name ends with "Component" this part is dropped.

For example:

 * `TodoComponent -> todo`
 * `TodoListComponent -> todo-list`
 * `Header -> header`
  
### Registering Components Globally

Components can also be registered globally.
They will then be usable in any Component in your app.
You won't need to pass the class of your `VueComponent` to the `components` attribute in the `@Component` annotation.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // Register TodoComponent globally
        Vue.component("todo", TodoComponent.class);
    }
}
```

To understand more in depth how the Observation works, and avoid pitfalls it's recommended to read information on **[the Vue Instance](../essential/the-vue-instance.md)**.

Your shiny new Vue GWT Components will play nicely with GWT.
**[Checkout integration with GWT](../gwt-integration/styles.md)**.