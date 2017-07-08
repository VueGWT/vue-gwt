# Form Input Bindings

## The v-model Directive

This section is about the `v-model` directive.

It's important to note that for now in Vue GWT only `JsInterop` properties can be used in `v-model`.
For example if you have a property `myTodo` of type `Todo` then:
```html
<input v-model="myTodo.text">
```
Will only work if the `text` property in `Todo` has the `@JsProperty` annotation (or is public, and `Todo` has the `@JsType` annotation).

`v-model` expressions are also not type checked at compile time.

Apart from this limitations, `v-model` works the same way in Vue GWT than in Vue.js.

You can just **[see Vue.js documentation about Form Input Bindings](https://vuejs.org/v2/guide/forms.html)**.

## Binding to Inputs Without The v-model Directive

It's possible of course to bind to an input without using the v-model directive.
For example if you want to set directly the property of a non `JsInterop` object.

You can use the following syntax:

```html
<input :value="todo.getText()" @input="updateTodoText((NativeEvent) $event)">
```

```java
@JsType
@Component
public class TodoTextComponent extends VueComponent {
    public Todo todo;
    
    @Override
    public void created() {
        this.todo = new Todo("Hello World!");
    }

    public void updateMessage(NativeEvent event) {
        this.todo.setText(JsTools.get(event.getEventTarget(), "value"));
    }
}
```