# Reactivity System

::: tip
Vue.js reactivity system is based on [ES5 getter/setters](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Functions/get) which have limitations.

In the future, Vue will use [ES6 proxies](https://caniuse.com/#feat=proxy) for it's reactivity system, and the limitations below will disappear :tada:.
:::

## Objects Observation

When you assign an Object to a `@JsProperty` of a Component, all it's properties **must** already be set in the JS world to be observed.
This means that they must have a value set, even if null.

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

## Java Collections Observation

Due to JavaScript getter/setter limitations [Vue.js cannot observe some changes on JavaScript Arrays](https://vuejs.org/v2/guide/list.html#Array-Change-Detection).

All Java Collections have a JavaScript implementation provided by GWT.
In some cases, these implementations use JavaScript Arrays to store their data.
This mean that some Java Collection cannot be automatically observed by Vue.js.

Luckily, Vue GWT goes around this limitation for you by wrapping java Collections mutation methods.
When calling any of these methods, Vue GWT will warn Vue.js observer to trigger a view update.

The wrapped methods are:

- For `List`: `add`, `addAll`, `clear`, `remove`, `removeAll`, `retainAll`, `set`
- For `Set`: `add`, `addAll`, `clear`, `remove`, `removeAll`, `retainAll`
- For `Map`: `put`, `putAll`, `putIfAbsent`, `replace`, `clear`, `remove`

For this to work the name of these methods must not change when your code is optimized by GWT.

To tell GWT that you want this, you must always:

- Use either `List`, `Set` or `Map` for your property type, never the concrete type
- Always the `@JsProperty` annotation on top of your fields, in any collection you want to use in your template.

Here is a valid example of a `UsersRepository` containing a collection of Users:

```java
class UsersRepository {
  @JsProperty
  List<User> myUsers;
}
```

::: warning
If you don't respect **both** these constraints **observation will break when compiling for production**, so be extra careful.

We will try to add a compile time error for these cases in the future.
:::
