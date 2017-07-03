# Conditional and Loops

## Conditional

It’s quite simple to toggle the presence of an element too.
Let's check this with a small example:

```java
@JsType
@Component
public class DemoComponent extends VueComponent {
    public String visible;
    
    @Override
    public void created() {
        this.visible = true;
    }
}
```

```html
<p v-if="visible">
    The property "visible" is set to true!
</p>
```

In this case the `p` element will only be created if the property `visible` of your Component instance is set to `true`.
This example demonstrates that we can bind data to not only text and attributes, but also the structure of the DOM.

## Loops

There are quite a few other directives, each with its own special functionality.
For example, the v-for directive can be used for displaying a list of items.

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

We will then create a list of `Todo` in our `DemoComponent`.
Because Vue.js is only able to observe JavaScript Arrays you must use the provided JsArray collection class in your Components.
This collection will be represented as a native JavaScript array in the browser.

```java
@JsType
@Component
public class DemoComponent extends VueComponent {
    public JsArray<Todo> todos;
    
    @Override
    public void created() {
        this.todos = new JsArray<>();
        this.todos.push(new Todo("Learn Java"));
        this.todos.push(new Todo("Learn Vue GWT"));
        this.todos.push(new Todo("Build something awesome"));
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
This is because Vue GWT compile templates expressions to Java and so need the type information.
You can import Java types in your template by using the `vue-gwt:import` element.

**[Let users interact with our Component](./handling-user-input.md)**