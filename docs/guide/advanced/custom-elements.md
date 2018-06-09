# Custom Elements support

## What are Custom Elements?

Custom Elements are a part of the Web Components standard. 
They let you create reusable DOM Elements that can be easily shared between web applications.
In a nutshell, it means being able to code your own custom DOM Elements, that can receive property values and emit events, using the native DOM API.

You can [find more information about the subject on MDN](https://developer.mozilla.org/en-US/docs/Web/Web_Components/Custom_Elements).

## How to Declare a Custom Element?

### About Vue and Custom Elements

Vue Components definitions are already very close to the Custom Element specification.
That shouldn't come as a surprise as Vue.js was largely inspired by the first draft of the Web Components specification.

For Vue.js, [a small library exists](https://github.com/karol-f/vue-custom-element) to declare Vue.js components as Custom Elements.
Vue GWT integrates a [slightly modified version](https://github.com/adrienbaron/vue-custom-element) of this library so you can declare your own Custom Elements easily.

### The AnimalSelector Example

Let's say you have a Vue GWT component called `AnimalSelectorComponent`.
It let's a user with a given name select an animal, and emits an event each time an animal is selected. 

```java
@Component
public class AnimalSelectorComponent implements IsVueComponent {
    @Prop
    @JsProperty
    String userName;

    @JsMethod
    public void selectAnimal(Event event) {
        vue().$emit("animal-selected", ((HTMLInputElement) event.target).value);
    }
}
```

```html
<div>
    <p>Hello {{ userName }}! Please select your animal:</p>
    <label>
        😸 <input type="radio" name="animal" value="😸" @click="selectAnimal"/>
    </label>
    <label>
        🐰 <input type="radio" name="animal" value="🐰" @click="selectAnimal"/>
    </label>
    <label>
        🐕 <input type="radio" name="animal" value="🐕" @click="selectAnimal"/>
    </label>
    <label>
        🐺 <input type="radio" name="animal" value="🐺" @click="selectAnimal"/>
    </label>
</div>
```

It just takes one line to register your Vue GWT Component as a Custom Element 🎉:

```java
// Register our AnimalSelectorComponent under the name animal-selector
Vue.customElement("animal-selector", AnimalSelectorComponent.class);
```

Any `<animal-selector></animal-selector>` in your document will then turn into an instance of that Vue Component.

::: tip
This will work even if the Element is present in the DOM before your GWT app is loaded.
You Custom Element will also work in Angular, React or Vanilla JS applications.
:::

::: warning
Due to the specification, the name of your Custom Element **MUST** contain at least one hyphen (-).
:::

Here is a live example:
<div class="example-container" data-name="animalSelector">
    <animal-selector user-name="Tom" id="animalSelector"></animal-selector>
</div>

## Browser Support

When we said one line was enough, we meant for recent browsers supporting the V1 of the Web Components specification.

| [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/firefox.png" alt="Firefox" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Firefox | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/chrome.png" alt="Chrome" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Chrome | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/safari.png" alt="Safari" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Safari | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/opera.png" alt="Opera" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Opera | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/safari-ios.png" alt="iOS Safari" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) iOS | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/chrome-android.png" alt="Chrome for Android" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Android |
|:---------:|:---------:|:---------:|:---------:|:---------:|:---------:|
| behind --flag | 54+ | 10.1+ | 42+ | 10.3+ | 55+

[Custom Elements v1 support](http://caniuse.com/#feat=custom-elementsv1)

For older browser you can use the small [document register element polyfill](https://github.com/WebReflection/document-register-element), which will give you IE9+ compatibility:

| [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/edge.png" alt="IE / Edge" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) IE / Edge | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/firefox.png" alt="Firefox" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Firefox | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/chrome.png" alt="Chrome" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Chrome | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/safari.png" alt="Safari" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Safari | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/opera.png" alt="Opera" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Opera | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/safari-ios.png" alt="iOS Safari" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) iOS | [<img src="https://raw.githubusercontent.com/godban/browsers-support-badges/master/src/images/chrome-android.png" alt="Chrome for Android" width="16px" height="16px" />](http://godban.github.io/browsers-support-badges/) Android |
|:---------:|:---------:|:---------:|:---------:|:---------:|:---------:|:---------:|
| IE9+, Edge| &check;| &check; | &check; | &check; | &check; | &check;

Just [download the latest version](https://github.com/WebReflection/document-register-element/blob/master/build/document-register-element.js) and add it to your project, or add the following to your index.html:

```html
<script src="https://cdnjs.cloudflare.com/ajax/libs/document-register-element/1.7.0/document-register-element.js"></script>
```

## Properties, Events and Slots

### Properties

You may have noticed that our `AnimalSelectorComponent` has an `@Prop` userName.
How do we pass the value to our Component?

Well, simply set it on the element (in camel case), and the Component will automatically get it:
```html
<animal-selector user-name="Tom"></animal-selector>
```

Even better, if the property changes, it will be passed again, just like a regular Vue property binding.
You can open your browser Dev Tools and change the property `user-name` to check that it works.

You can also set the value programmatically:

```js
document.getElementById("animalSelector").setAttribute("user-name", "Bobby");
```

Attributes values are always Strings.
The value will be automatically parsed for you for number types (int/float) and booleans.

### Events

You can also listen to events fired by your Custom Element using `addEventListener`.
The events fired are of type [`CustomEvent`](https://developer.mozilla.org/en-US/docs/Web/API/CustomEvent).
They have a read only `detail` property which contains an array of the values of the event.

So to get the value of AnimalSelector `animal-selected` event, we just do in JavaScript:

```js
document.getElementById("animalSelector").addEventListener("animal-selected", event => console.log(event.detail[0]));
```

### Slots

[Slots](../essentials/components.md#content-distribution) are also supported.
The only limitation is that it won't work for dynamic content as the DOM element is replaced with your Vue Component element when created.

Let's see `slots` in action.
For example with this `DemoSlotsComponent.html` template:

```html
<div>
    <slot name="header">No HEADER slot content passed (this is default value)</slot>
    <p>This is text from inside of the element</p>
    <slot>No DEFAULT slot content passed (this is default value)</slot>
    <slot name="footer">No FOOTER slot content passed (this is default value)</slot>
</div>
```

The following element in your document:

```html
<demo-slots>
  <p vue-slot="header">Some Header Content</p>
  <p>Default slot</p>
  <p vue-slot="footer">Some Footer Content</p>
</demo-slots>
```

Will render:

<div class="example-container" data-name="webComponentSlot">
    <div>
        <p>Some Header Content</p>
        <p>This is text from inside of the element</p>
        <p>Default slot</p>
        <p vue-slot="footer">Some Footer Content</p>
    </div>
</div>

## Manipulating our Custom Element

### Creating Instances Programmatically

The `customElement` method returns a `VueCustomElementType<T extends IsVueComponent>`.
This object can be used to create instances of your Custom Element easily.

```java
VueCustomElementType<AnimalSelectorComponent> animalSelectorElementType =
            Vue.customElement("animal-selector", AnimalSelectorComponent.class);

VueCustomElement<AnimalSelectorComponent> myElement = animalSelectorElementType.create();
// myElement is a regular DOM element
DomGlobal.document.body.appendChild(myElement);
```

You can also just use the regular `createElement` DOM method:

```java
VueCustomElement<AnimalSelectorComponent> myElement = Js.cast(DomGlobal.document.createElement("animal-selector"));
```

### Accessing the Vue Component of a Custom Element

#### From Java

From your Custom Element instance you can access the underlying Vue Component.
This will work no matter how your Custom Element instance is created.

```java
VueCustomElement<AnimalSelectorComponent> animalSelectorElement = Js.cast(DomGlobal.document.createElement("animal-selector"));
AnimalSelectorComponent animalSelectorComponent = myElement.getComponent();
// Access any public property, or call any public method on animalSelectorComponent.

VueCustomElement<TodoComponent> todoElement = Js.cast(DomGlobal.document.getElementById("myTodo"));
TodoComponent todoComponent = todoElement.getComponent();
// Access any public property, or call any public method on todoComponent.
```

#### From JavaScript

You can also access to your Vue Component instance from JavaScript:

```js
const myComponent = myCustomElement.__vue_custom_element__.$children[0];
```

Keep in mind that only the JsInterop properties and methods from your Component will be visible in JavaScript.

## Available Options

`Vue.customElement` takes an optional third argument of type `CustomElementOptions<T extends IsVueComponent>`.

Here are the available options:

* `constructorCallback(element)`: Called when your Custom Element is constructed
* `connectedCallback(element)`: Element is mounted to the DOM
* `disconnectedCallback(element)`: Element is disconnected from the DOM
* `attributeChangedCallback(element, name, oldValue, value)`: An attribute as changed
* `destroyTimeout`: Time in `ms` between when the Element is removed from the DOM and the associated Vue Component is destroyed. Default to `3000`.
* `shadow`: Use [shadow DOM](https://developer.mozilla.org/en-US/docs/Web/Web_Components/Shadow_DOM) (only supported on browsers that have native Web Components support). Default to `false`.
* `shadowCss`: CSS rules to apply in the shadow DOM.

Each callback get passed a reference to the Custom Element that fired the event.

```java
CustomElementOptions<AnimalSelectorComponent> customElementOptions =
    new CustomElementOptions<>()
        .setConnectedCallback(animalSelectorElement -> DomGlobal.console.log(animalSelectorElement));

Vue.customElement("animal-selector", AnimalSelectorComponent.class, customElementOptions);
```