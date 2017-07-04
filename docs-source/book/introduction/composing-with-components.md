# Composing with Components

The component system is another important concept in Vue, because it’s an abstraction that allows us to build large-scale applications composed of small, self-contained, and often reusable components.
If we think about it, almost any type of application interface can be abstracted into a tree of components.

## Creating a Child Component

Let's create a child Component called `TodoComponent`.
We will then instantiate this component in our `DemoComponent`.

This Component will display a simple message.

***TodoComponent.java***

We first create a Class like for our `DemoComponent`.

```java
@JsType
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

***DemoComponent.java***

We will then make a few changes to our `DemoComponent` to use `TodoComponent`.

We first register `TodoComponent` to be used in our `DemoComponent` by passing it to the `@Component` annotation.

```java
@JsType
@Component(components = {TodoComponent.class})
public class DemoComponent extends VueComponent {
}
```

***DemoComponent.html***

Now you can compose it in our DemoComponent’s template:

```html
<div>
    <ol>
        <todo></todo>
    </ol>
</div>
```

### How Is the Component Html Name Set?

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
You won't need to pass the Class of your Component to the `components` attribute in the `@Component` annotation.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // Register TodoComponent globally
        Vue.registerComponent(TodoComponent.class);
    }
}
```

This is the equivalent of calling `Vue.component(...)` in Vue.js.

## Passing Properties to Children Components

Right now we render the same text for every todo, which is not super interesting.
We should be able to pass data from the parent scope into child components.

Let’s modify our `TodoComponent` to make it accept a property.

***TodoComponent.java***
```java
@JsType
@Component
public class TodoComponent extends VueComponent {
    @Prop
    public Todo todo;
    
    @Override
    public void created() {}
}
```

The `@Prop` annotation tells Vue GWT that our `todo` property will be passed from a parent component.

***TodoComponent.html***
```html
<li>
    {{ todo.getText() }}
</li>
```

***DemoComponent.java***

Let's see how to pass this value from the parent.

There is no change in the Java from our previous `Todo` example.

```java
@JsType
@Component(components = {TodoComponent.class})
public class DemoComponent extends VueComponent
{
    public JsArray<String> todos;
    
    @Override
    public void created() {
        this.todos = new JsArray<>();
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
    }
}
```

***DemoComponent.html***

But now we can are able to pass the todo into each repeated component using `v-bind`:

```html
<vue-gwt:import class="com.mypackage.Todo"/>
<div>
    <ol>
        <todo v-for="String todo in todos" v-bind:todo="todo"></todo>
    </ol>
</div>
```

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

Your shiny new Vue GWT components will play nicely with existing gwt-user Widgets.
**[Checkout the backward compatibility](./backward-compatibility.md)**. 