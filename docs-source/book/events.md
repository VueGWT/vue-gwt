# Event Handling

!INCLUDE "dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/events.html) and has been adapted for Vue GWT.*

## Listening to Events

We can use the `v-on` directive to listen to DOM events and run some JavaScript when they're triggered.

For example:

```html
<div>
  <button v-on:click="counter += 1">Add 1</button>
  <p>The button above has been clicked {{ counter }} times.</p>
</div>
```
```java
@JsType
@Component
public class ButtonPlusOneComponent extends VueComponent {
    public int counter;

    @Override
    public void created() {
        this.counter = 0;
    }
}
```

Result:

{% raw %}
<div class="example-container" data-name="buttonPlusOneComponent">
    <span id="buttonPlusOneComponent"></span>
</div>
{% endraw %}

## Method Event Handlers

The logic for many event handlers will be more complex though, so keeping your JavaScript in the value of the `v-on` attribute simply isn't feasible.
That's why `v-on` can also accept a method call.

> Vue.js supports passing the method name (without the parenthesis), this doesn't work in Vue GWT.
Vue GWT always expect statements in a `v-on` to be valid Java. 

For example:

```html
<button v-on:click="greet()">Greet</button>
```

```java
@JsType
@Component
public class GreetComponent extends VueComponent {
    @Override
    public void created() {}

    public void greet() {
        Window.alert("Hello from GWT!");
    }
}
```

Result:

{% raw %}
<div class="example-container" data-name="greetComponent">
    <span id="greetComponent"></span>
</div>
{% endraw %}

You can also invoke the method in JavaScript.
Try this in your browser console:

```js
greetComponent.greet();
```

## Accessing the Event

Sometimes we also need to access the original DOM event in an inline statement handler.
You can pass it into a method using the special `$event` variable:

```html
<button v-on:click='warn("Form cannot be submitted yet.", (NativeEvent) $event)'>
    Submit
</button>
```

```java
// ...
public void warn(String message, NativeEvent event) {
    if (event != null) {
        event.preventDefault();
        message += " -> Event Type: " + event.getType();
    }

    Window.alert(message);
}
```

You will notice that we cast the `$event` variable to `NativeEvent` in our template.
This is to indicate the type of the `$event` variable to Vue GWT.

This means that if your `warn` method was actually declared like this: `void warn(NativeEvent event, String message)` it would break at compile time with an explicit error.

You don't have to import the `NativeEvent` class to use it in your template.
It's already imported by default for convenience.

You'll see further that `$event` can also be used when communicating between Components.
In those case you might cast it to another type, like `Todo` for example.


{% raw %}
<div class="example-container" data-name="vOnWithDOMEventComponent">
    <span id="vOnWithDOMEventComponent"></span>
</div>
{% endraw %}

## Event Modifiers

It is a very common need to call `event.preventDefault()` or `event.stopPropagation()` inside event handlers.
Although we can do this easily inside methods, it would be better if the methods can be purely about data logic rather than having to deal with DOM event details.

To address this problem, Vue provides **event modifiers** for `v-on`.
Recall that modifiers are directive postfixes denoted by a dot.

- `.stop`
- `.prevent`
- `.capture`
- `.self`
- `.once`

```html
<!-- the click event's propagation will be stopped -->
<a v-on:click.stop="doThis()"></a>

<!-- the submit event will no longer reload the page -->
<form v-on:submit.prevent="onSubmit()"></form>

<!-- modifiers can be chained -->
<a v-on:click.stop.prevent="doThat()"></a>

<!-- just the modifier -->
<form v-on:submit.prevent></form>

<!-- use capture mode when adding the event listener -->
<!-- i.e. an event targeting an inner element is handled here before being handled by that element -->
<div v-on:click.capture="doThis()">...</div>

<!-- only trigger handler if event.target is the element itself -->
<!-- i.e. not from a child element -->
<div v-on:click.self="doThat()">...</div>
```

ℹ️  Order matters when using modifiers because the relevant code is generated in the same order.
Therefore using `@click.prevent.self` will prevent **all clicks** while `@click.self.prevent` will only prevent clicks on the element itself.

> New in Vue.js 2.1.4

```html
<!-- the click event will be triggered at most once -->
<a v-on:click.once="doThis()"></a>
```

Unlike the other modifiers, which are exclusive to native DOM events, the `.once` modifier can also be used on [component events](components.html#Using-v-on-with-Custom-Events).
If you haven't read about components yet, don't worry about this for now.

## Key Modifiers

When listening for keyboard events, we often need to check for common key codes. Vue also allows adding key modifiers for `v-on` when listening for key events:

```html
<!-- only call vm.submit() when the keyCode is 13 -->
<input v-on:keyup.13="submit()">
```

Remembering all the keyCodes is a hassle, so Vue provides aliases for the most commonly used keys:

```html
<!-- same as above -->
<input v-on:keyup.enter="submit()">

<!-- also works for shorthand -->
<input @keyup.enter="submit()">
```

Here's the full list of key modifier aliases:

- `.enter`
- `.tab`
- `.delete` (captures both "Delete" and "Backspace" keys)
- `.esc`
- `.space`
- `.up`
- `.down`
- `.left`
- `.right`

In Vue.js you can also [define custom key modifier aliases](https://vuejs.org/v2/api/#keyCodes) via the global `config.keyCodes` object:

```js
// enable v-on:keyup.f1
Vue.config.keyCodes.f1 = 112
```

This is not doable yet in Vue GWT.

## Modifier Keys

> New in Vue.js 2.1.0

You can use the following modifiers to trigger mouse or keyboard event listeners only when the corresponding modifier key is pressed:

- `.ctrl`
- `.alt`
- `.shift`
- `.meta`

> Note: On Macintosh keyboards, meta is the command key (⌘).
On Windows keyboards, meta is the windows key (⊞).
On Sun Microsystems keyboards, meta is marked as a solid diamond (◆).
On certain keyboards, specifically MIT and Lisp machine keyboards and successors, such as the Knight keyboard, space-cadet keyboard, meta is labeled “META”.
On Symbolics keyboards, meta is labeled “META” or “Meta”.

For example:

```html
<!-- Alt + C -->
<input @keyup.alt.67="clear()">

<!-- Ctrl + Click -->
<div @click.ctrl="doSomething()">Do something</div>
```

ℹ️  Note that modifier keys are different from regular keys and when used with `keyup` events, they have to be pressed when the event is emitted.
In other words, `keyup.ctrl` will only trigger if you release a key while holding down `ctrl`.
It won't trigger if you release the `ctrl` key alone.

### Mouse Button Modifiers

> New in Vue.js 2.2.0

- `.left`
- `.right`
- `.middle`

These modifiers restrict the handler to events triggered by a specific mouse button.

## Why Listeners in HTML?

You might be concerned that this whole event listening approach violates the good old rules about "separation of concerns".
Rest assured - since all Vue handler functions and expressions are strictly bound to the ViewModel that's handling the current view, it won't cause any maintenance difficulty.
In fact, there are several benefits in using `v-on`:

1. It's easier to locate the handler function implementations within your Java code by simply skimming the HTML template.

2. Since you don't have to manually attach event listeners in Java, your ViewModel code can be pure logic and DOM-free. This makes it easier to test.

3. When a ViewModel is destroyed, all event listeners are automatically removed. You don't need to worry about cleaning it up yourself.