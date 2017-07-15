# Handling User Input

!INCLUDE "../dependencies.md"

## Events
To let users interact with your app, we can use the `v-on` directive to attach event listeners that invoke methods on our Vue instances:

```html
<div>
    <p>{{ message }}</p>
    <button v-on:click="addExclamationMark()">Add an explanation mark</button>
</div>
```

```java
@JsType
@Component
public class ExclamationComponent extends Vue {
    public String message;
    
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

## The v-model Directive

Vue also provides the [v-model directive](../forms.md) that makes two-way binding between form input and app state a breeze:
```html
<div>
    <p>{{ message }}</p>
    <input type="text" v-model="message">
</div>
```

```java
@JsType
@Component
public class MessageComponent extends Vue {
    public String message;
    
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

A real world application is never just one Component, let's see how to **[compose Components together](./composing-with-components.md)**.