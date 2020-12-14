# Form Input Bindings

## The `v-model` Directive

This section is about the `v-model` directive.

It's important to note that for now in Vue GWT only `@Data` fields or `@Computed` with a getter and a setter can be used directly in `v-model`.
Apart from this limitations, `v-model` works the same way in Vue GWT than in Vue.js.

You can just **[see Vue.js documentation about Form Input Bindings](https://vuejs.org/v2/guide/forms.html)**.

### Using a Computed Property

You can use a Computed property with `v-model`:

```java
@Component
public class TodoTextComputedComponent implements IsVueComponent {
    @Data Todo todo = new Todo("Hello World!");

    @Computed
    public String getTodoText() {
        return this.todo.getText();
    }

    @Computed
    public void setTodoText(String text) {
        this.todo.setText(text);
    }
}
```

```html
<div>
    <input v-model="todoText"/>
    Todo Text: {{ todoText }}
</div>
```

<div class="example-container" data-name="todoTextComputedComponent">
    <span id="todoTextComputedComponent"></span>
</div>

### Binding to Inputs Without The `v-model` Directive

In some case you can't use computed properties.
For example inside a `v-for` on the loop variable.

In those case you can use the long form of v-model with the following syntax:
```html
<div v-for="Task todo in todoList">
    <input :value="todo.getText()" @input="todo.text = (String) $event"/>
    Todo Text: {{ todo.getText() }}
</div>
```
Or this syntax :
```html
<div v-for="Task todo in todoList">
    <input :value="todo.getText()" @input="updateTodoText"/>
    Todo Text: {{ todo.getText() }}
</div>
```

```java
@Component
public class TodoTextComponent implements IsVueComponent {
    @Data Todo todo = new Todo("Hello World!");

    @JsMethod
    public void updateTodoText(HTMLInputElement event) {
        this.todo.setText(event.target.value);
    }
}
```

<div class="example-container" data-name="todoTextComponent">
    <span id="todoTextComponent"></span>
</div>
