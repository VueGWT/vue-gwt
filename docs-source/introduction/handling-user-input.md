# Handling User Input
To let users interact with your app, we can use the v-on directive to attach event listeners that invoke methods on our Vue instances:

```html
<div>
    <p>{{ message }}</p>
    <button v-on:click="addExclamationMark()">Add an explanation mark</button>
</div>
```

```java
@JsType
@Component
public class DemoComponent extends VueComponent {
    public String message;
    
    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
    
    public addExclamationMark() {
        this.message += "!";
    }
}
```

Clicking on the "Add an exclamation mark" button will add an exclamation mark to our message.

Note in the method we simply update the state of our app without touching the DOM - all DOM manipulations are handled by Vue, and the code you write is focused on the underlying logic.

Vue also provides the v-model directive that makes two-way binding between form input and app state a breeze:
```html
<div>
    <p>{{ message }}</p>
    <input v-model="message">
</div>
```

```java
@JsType
@Component
public class DemoComponent extends VueComponent {
    public String message;
    
    @Override
    public void created() {
        this.message = "Hello Vue GWT!";
    }
}
```

Changing the value of the input will automatically update the content of our message property.
Changing the value of our Java property will change the value of input.

⚠️ It's important to note that for now in Vue GWT only JsInterop properties can be used in `v-model`.
For example if you have a property `myTodo` of type `Todo` then:
```html
<input v-model="myTodo.text">
```
Will only work if the `text` property in `Todo` has the `@JsProperty` annotation.

`v-model` expression is also not type checked at compile time.
Those limitations only exists for `v-model`.

A real world application is never just one Components, let's see how to **[compose Component's together](./composing-with-components.md)**.