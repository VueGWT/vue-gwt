# Form Input Bindings

!INCLUDE "../dependencies.md"

## The `v-model` Directive

This section is about the `v-model` directive.

It's important to note that for now in Vue GWT only `JsInterop` properties can be used directly in `v-model`.
For example if you have a property `myTodo` of type `Todo` then:
```html
<input v-model="myTodo.text">
```
Will only work if the `text` property in `Todo` has the `@JsProperty` annotation (or is public, and `Todo` has the `@JsType` annotation).

`v-model` expressions are also not type checked at compile time.

Apart from this limitations, `v-model` works the same way in Vue GWT than in Vue.js.

You can just **[see Vue.js documentation about Form Input Bindings](https://vuejs.org/v2/guide/forms.html)**.

## Making `v-model` Work with Regular Java Objects

### Using a Computed Property

You can use a computed properties along with the regular `v-model`:

```java
@Component
public class TodoTextComputedComponent extends VueComponent {
    @JsProperty Todo todo = new Todo("Hello World!");

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

{% raw %}
<div class="example-container" data-name="todoTextComputedComponent">
    <span id="todoTextComputedComponent"></span>
</div>
{% endraw %}

### Binding to Inputs Without The `v-model` Directive

In some case you can't use computed properties.
For example inside a `v-for` on the loop variable.

In those case you can use the following syntax:

```html
<div>
    <input :value="todo.getText()" @input="updateTodoText((NativeEvent) $event)"/>
    Todo Text: {{ todo.getText() }}
</div>
```

```java
@Component
public class TodoTextComponent extends VueComponent {
    @JsProperty Todo todo = new Todo("Hello World!");

    public void updateMessage(NativeEvent event) {
        this.todo.setText(JsTools.get(event.getEventTarget(), "value"));
    }
}
```

{% raw %}
<div class="example-container" data-name="todoTextComponent">
    <span id="todoTextComponent"></span>
</div>
{% endraw %}