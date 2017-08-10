# Components

!INCLUDE "../dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/components.html) and has been adapted for Vue GWT.*

## What are Components? {#what-are-components}

Components are one of the most powerful features of Vue.
They help you extend basic HTML elements to encapsulate reusable code.
At a high level, components are custom elements that Vue's compiler attaches behavior to.
In some cases, they may also appear as a native HTML element extended with the special `is` attribute.

## Using Components {#using-components}

### Registration

We already saw how to define a Component in Vue GWT.

```java
@Component
public class MyComponent extends VueComponent {
}
```

```html
<div>
  A custom component!
</div>
```

To register a global component, you can use `Vue.component(tagName, MyComponent.class)`. For example:

```java
Vue.component("my-component", MyComponent.class);
```

<p class="info-panel">
    Note that Vue does not enforce the <a href="http://www.w3.org/TR/custom-elements/#concepts">W3C Rules</a> for custom tag names (all-lowercase, must contain a hyphen) though following this convention is considered good practice.
</p>

Once registered, a component can be used in an instance's template as a custom element, `<my-component></my-component>`.
Make sure the component is registered **before** you instantiate the root Component.
Here's the full example:

***ParentComponent***

```html
<div>
  <my-component></my-component>
</div>
```

```java
@Component
public class ParentComponent extends VueComponent {
}
```

```java
// Somewhere in your App
Vue.component("my-component", MyComponent.class);
Vue.attach("#rootComponent", RootComponent.class);
```

Which will render:

```html
<div>
  <div>A custom component!</div>
</div>
```

### Local Registration

You don't have to register every component globally.
You can make a component available only in the scope of another instance/component by registering it with the `components` annotation option:

```java
@Component(components = {MyComponent.class})
public class ParentComponent extends VueComponent {
}
```

In that case the name of your Component is determined using the Component's Class name.

The name is converted from CamelCase to kebab-case.
If the name ends with "Component" this part is dropped.

For example:

 * `TodoComponent -> todo`
 * `TodoListComponent -> todo-list`
 * `Header -> header`

The same principle applies for other registerable Vue features, such as directives.

### DOM Template Parsing Caveats

There are some restrictions that are inherent to how HTML works, because Vue can only retrieve the template content **after** the browser has parsed and normalized it.
Most notably, some elements such as `<ul>`, `<ol>`, `<table>` and `<select>` have restrictions on what elements can appear inside them, and some elements such as `<option>` can only appear inside certain other elements.

This will lead to issues when using custom components with elements that have such restrictions, for example:

```html
<table>
  <my-row>...</my-row>
</table>
```

The custom component `<my-row>` will be hoisted out as invalid content, thus causing errors in the eventual rendered output.
A workaround is to use the `is` special attribute:

```html
<table>
  <tr is="my-row"></tr>
</table>
```

### Data sharing between Components instances

If you know Vue.js then you know that [the `data` property of your Component must be a function](https://vuejs.org/v2/guide/components.html#data-Must-Be-a-Function).

Vue GWT manage this for you and pass a function so that all your Components have separate data models.

When parsing your the Component class we build a `dataObject` Object for you. For example a Component like this:

```java
@Component
public class StarkComponent extends VueComponent {
    @JsProperty String winter;
    @JsProperty boolean is;
    @JsProperty String coming;
}
```

Will result in the following `dataObject` Object:
```js
var dataObject = {
	winter: null,
    is: null,
    coming: null
};
```

To get a new instance of this `dataObject` Object to every Component, Vue GWT uses `JSON.parse` and `JSON.stringify`.
So the `data` function passed to Vue.js looks like this:

```js
var options = {
	data: function () {
		return JSON.parse(JSON.stringify(dataObject));
	}
}
```

If you don't want the factory behavior on a Component for some reasons, you can pass the option useFactory to `false` on your `@Component` annotation.
The same `dataObject` instance will be passed to all your instance of your Component and their data model will be shared.

Here is a working example:

```java
@Component(useFactory = false)
public class SharedDataModelComponent extends VueComponent {
    @JsProperty int counter = 0;
}
```

```html
<button @click="counter++">{{ counter }}</button>
```

We then instantiate 3 of those Components:

{% raw %}
<div class="example-container" data-name="simpleLinkComponent">
    <span id="simpleLinkComponent"></span>
</div>
{% endraw %}

### Composing Components

Components are meant to be used together, most commonly in parent-child relationships: component A may use component B in its own template.
They inevitably need to communicate to one another: the parent may need to pass data down to the child, and the child may need to inform the parent of something that happened in the child.
However, it is also very important to keep the parent and the child as decoupled as possible via a clearly-defined interface.
This ensures each component's code can be written and reasoned about in relative isolation, thus making them more maintainable and potentially easier to reuse.

In Vue, the parent-child component relationship can be summarized as **props down, events up**.
The parent passes data down to the child via **props**, and the child sends messages to the parent via **events**.
Let's see how they work next.

<p style="text-align: center">
  <img style="width:300px" src="https://vuejs.org/images/props-events.png" alt="props down, events up">
</p>

## Props {#props}

### Passing Data with Props

Every component instance has its own **isolated scope**.
This means you cannot (and should not) directly reference parent data in a child component's template.
Data can be passed down to child components using **props**.

A prop is a custom attribute for passing information from parent components.
A child component needs to explicitly declare the props it expects to receive using the `@Prop` annotation:

```java
@Component
public class ChildComponent extends VueComponent {
    @Prop
    @JsProperty
    public String message;
}
```

Then we can pass a plain string to it like so:

```html
<child message="Hello World!"></todo>
```

### camelCase vs. kebab-case

HTML attributes are case-insensitive, so when using non-string templates, camelCased prop names need to use their kebab-case (hyphen-delimited) equivalents:

```java
@Component
public class ChildComponent extends VueComponent {
    @Prop
    @JsProperty
    String myMessage;
}
```

```html
<!-- kebab-case in HTML -->
<child my-message="Hello World!"></child>
```

### Dynamic Props

Similar to binding a normal attribute to an expression, we can also use `v-bind` for dynamically binding props to data on the parent.
Whenever the data is updated in the parent, it will also flow down to the child:

```html
<div>
  <input v-model="parentMsg">
  <br>
  <child v-bind:my-message="parentMsg"></child>
</div>
```

It's often simpler to use the shorthand syntax for `v-bind`:

```html
<child :my-message="parentMsg"></child>
```

### Literal vs. Dynamic

A common mistake beginners tend to make is attempting to pass down a number using the literal syntax:

```html
<!-- this passes down a plain string "1" -->
<comp some-prop="1"></comp>
```

However, since this is a literal prop, its value is passed down as a plain string `"1"` instead of an actual number.
If we want to pass down an actual Number, we need to use `v-bind` so that its value is evaluated as a Java expression:

```html
<!-- this passes down an actual number -->
<comp v-bind:some-prop="1"></comp>
```

### One-Way Data Flow

All props form a **one-way-down** binding between the child property and the parent one: when the parent property updates, it will flow down to the child, but not the other way around.
This prevents child components from accidentally mutating the parent's state, which can make your app's data flow harder to reason about.

In addition, every time the parent component is updated, all props in the child component will be refreshed with the latest value.
This means you should **not** attempt to mutate a prop inside a child component.
If you do, Vue will warn you in the console.

There are usually two cases where it's tempting to mutate a prop:

1. The prop is used to only pass in an initial value, the child component simply wants to use it as a local data property afterwards;

2. The prop is passed in as a raw value that needs to be transformed.

The proper answer to these use cases are:

1. Define a local data property that uses the prop's initial value as its initial value:

```java
@Component
public class MyComponent extends VueComponent implements HasCreated {
    @Prop
    @JsProperty
    public String initialCounter;
    
    @JsProperty String counter;
    
    @Override
    public void created() {
        this.counter = this.initialCounter;
    }
}
```

2. Define a computed property that is computed from the prop's value:

```java
@Component
public class MyComponent extends VueComponent {
    @Prop
    @JsProperty
    public String size;
    
    @Computed
    public String getNormalizedSize() {
        return this.size.trim().toLowerCase();
    }
}
```

<p class="info-panel">
    Note that objects and arrays in JavaScript (like in Java) are passed by reference, so if the prop is an array or object, mutating the object or array itself inside the child <bold>will</bold> affect parent state.
</p>

### Prop Validation

In Vue.js, it is possible for a component to specify requirements for the props it is receiving.
If a requirement is not met, Vue will emit warnings.
This is especially useful when you are authoring a component that is intended to be used by others.

Vue GWT can Vue.js to check the type based on the type of your Java property.
If you want this behavior you can pass `checkType` to true to your `@Prop` annotation.

When prop validation fails, Vue will produce a console warning (if using the development build).

## Non-Prop Attributes

A non-prop attribute is an attribute that is passed to a component, but does not have a corresponding prop defined.

While explicitly defined props are preferred for passing information to a child component, authors of component libraries can't always foresee the contexts in which their components might be used.
That's why components can accept arbitrary attributes, which are added to the component's root element.

For example, imagine we're using a 3rd-party `bs-date-input` component with a Bootstrap plugin that requires a `data-3d-date-picker` attribute on the `input`.
We can add this attribute to our component instance:

```html
<bs-date-input data-3d-date-picker="true"></bs-date-input>
```

And the `data-3d-date-picker="true"` attribute will automatically be added to the root element of `bs-date-input`.

### Replacing/Merging with Existing Attributes

Imagine this is the template for `bs-date-input`:

```html
<input type="date" class="form-control">
```

To add specify a theme for our date picker plugin, we might need to add a specific class, like this:

```html
<bs-date-input
  data-3d-date-picker="true"
  class="date-picker-theme-dark"
></bs-date-input>
```

In this case, two different values for `class` are defined:

- `form-control`, which is set by the component in its template
- `date-picker-theme-dark`, which is passed to the component by its parent

For most attributes, the value provided to the component will replace the value set by the component.
So for example, passing `type="large"` will replace `type="date"` and probably break it!
Fortunately, the `class` and `style` attributes are a little smarter, so both values are merged, making the final value: `form-control date-picker-theme-dark`.

## Custom Events

We have learned that the parent can pass data down to the child using props, but how do we communicate back to the parent when something happens?
This is where Vue's custom event system comes in.

### Using `v-on` with Custom Events

Every Vue instance implements an [events interface](https://vuejs.org/v2/api/#Instance-Methods-Events), which means it can:

- Listen to an event using `$on(eventName)`
- Trigger an event using `$emit(eventName)`

<p class="info-panel">
    Note that Vue's event system is separate from the browser's <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget">EventTarget API</a>.
    Though they work similarly, <code>$on</code> and <code>$emit</code> are <strong>not<strong> aliases for <code>addEventListener</code> and <code>dispatchEvent</code>.
</p>

In addition, a parent component can listen to the events emitted from a child component using `v-on` directly in the template where the child component is used.

<p class="info-panel">
    You cannot use <code>$on</code> to listen to events emitted by children. You must use <code>v-on</code> directly in the template, as in the example below.
</p>

Here's an example:

```html
<button v-on:click="increment()">{{ counter }}</button>
```

```java
@Component
public class ButtonCounterComponent extends VueComponent {
    @JsProperty int counter = 0;

    public void increment() {
        this.counter++;
        // When incrementing, we fire an event.
        this.$emit("increment");
    }
}
```

```html
<div>
    <p>{{ total }}</p>
    <button-counter v-on:increment="incrementTotal()"></button-counter>
    <button-counter v-on:increment="incrementTotal()"></button-counter>
</div>
```

```java
@Component(components = {ButtonCounterComponent.class})
public class CounterWithEventComponent extends VueComponent {
    @JsProperty int total = 0;

    public void incrementTotal() {
        this.total++;
    }
}
```

{% raw %}
<div class="example-container" data-name="counterWithEventComponent">
    <span id="counterWithEventComponent"></span>
</div>
{% endraw %}

In this example, it's important to note that the child component is still completely decoupled from what happens outside of it.
All it does is report information about its own activity, just in case a parent component might care.

#### Binding Native Events to Components

There may be times when you want to listen for a native event on the root element of a component.
In these cases, you can use the `.native` modifier for `v-on`. For example:

```html
<my-component v-on:click.native="doTheThing()"></my-component>
```

### `.sync` Modifier

> 2.3.0+

In some cases we may need "two-way binding" for a prop - in fact, in Vue 1.x this is exactly what the `.sync` modifier provided. 
When a child component mutates a prop that has `.sync`, the value change will be reflected in the parent.
This is convenient, however it leads to maintenance issues in the long run because it breaks the one-way data flow assumption: the code that mutates child props are implicitly affecting parent state.

This is why we removed the `.sync` modifier when 2.0 was released.
However, we've found that there are indeed cases where it could be useful, especially when shipping reusable components.
What we need to change is **making the code in the child that affects parent state more consistent and explicit.**

In 2.3 we re-introduced the `.sync` modifier for props, but this time it is just syntax sugar that automatically expands into an additional `v-on` listener:

The following

```html
<comp :foo.sync="bar"></comp>
```

is expanded into:

```html
<comp :foo="bar" @update:foo="val => bar = val"></comp>
```

For the child component to update `foo`'s value, it needs to explicitly emit an event instead of mutating the prop:

```java
this.$emit('update:foo', newValue);
```

### Form Input Components using Custom Events

Custom events can also be used to create custom inputs that work with `v-model`. Remember:

```html
<input v-model="something">
```

is just syntactic sugar for:

```html
<input
  v-bind:value="something"
  v-on:input="setSomethingValue((NativeEvent) $event)">
```

When used with a component, this simplifies to:

```html
<custom-input
  :value="something"
  @input="something = (Type) $event">
</custom-input>
```

So for a component to work with `v-model`, it should (these can be configured in 2.2.0+):

- accept a `value` prop
- emit an `input` event with the new value

### Customizing Component `v-model`

Vue.js support [customizing component `v-model`](https://vuejs.org/v2/guide/components.html#Customizing-Component-v-model).
This is not yet supported by Vue GWT.

### Non Parent-Child Communication

Sometimes two components may need to communicate with one-another but they are not parent/child to each other.

For this you can use a [GWT EventBus](http://www.gwtproject.org/javadoc/latest/com/google/gwt/event/shared/EventBus.html).

In more complex cases, you should consider employing a dedicated [state-management pattern](https://vuejs.org/v2/guide/state-management.html).

## Content Distribution with Slots {#content-distribution}

When using components, it is often desired to compose them like this:

```html
<app>
  <app-header></app-header>
  <app-footer></app-footer>
</app>
```

There are two things to note here:

1. The `<app>` component does not know what content it will receive. It is decided by the component using `<app>`.

2. The `<app>` component very likely has its own template.

To make the composition work, we need a way to interweave the parent "content" and the component's own template.
This is a process called **content distribution** (or "transclusion" if you are familiar with Angular).
Vue.js implements a content distribution API that is modeled after the current [Web Components spec draft](https://github.com/w3c/webcomponents/blob/gh-pages/proposals/Slots-Proposal.md), using the special `<slot>` element to serve as distribution outlets for the original content.

### Compilation Scope

Before we dig into the API, let's first clarify which scope the contents are compiled in.
Imagine a template like this:

```html
<child-component>
  {{ message }}
</child-component>
```

Should the `message` be bound to the parent's data or the child data?
The answer is the parent.
A simple rule of thumb for component scope is:

> Everything in the parent template is compiled in parent scope; everything in the child template is compiled in child scope.

A common mistake is trying to bind a directive to a child property/method in the parent template:

```html
<!-- does NOT work -->
<child-component v-show="someChildProperty"></child-component>
```

Assuming `someChildProperty` is a property on the child component, the example above would not work.
The parent's template is not aware of the state of a child component.

If you need to bind child-scope directives on a component root node, you should do so in the child component's own template:

```java
@Component
public class ChildComponent extends VueComponent {
    @JsProperty boolean someChildProperty;
}
```

```html
<div v-show="someChildProperty">Child</div>
```

Similarly, distributed content will be compiled in the parent scope.

### Single Slot

Parent content will be **discarded** unless the child component template contains at least one `<slot>` outlet.
When there is only one slot with no attributes, the entire content fragment will be inserted at its position in the DOM, replacing the slot itself.

Anything originally inside the `<slot>` tags is considered **fallback content**.
Fallback content is compiled in the child scope and will only be displayed if the hosting element is empty and has no content to be inserted.

Suppose we have a component called `my-component` with the following template:

```html
<div>
  <h2>I'm the child title</h2>
  <slot>
    This will only be displayed if there is no content
    to be distributed.
  </slot>
</div>
```

And a parent that uses the component:

```html
<div>
  <h1>I'm the parent title</h1>
  <my-component>
    <p>This is some original content</p>
    <p>This is some more original content</p>
  </my-component>
</div>
```

The rendered result will be:

```html
<div>
  <h1>I'm the parent title</h1>
  <div>
    <h2>I'm the child title</h2>
    <p>This is some original content</p>
    <p>This is some more original content</p>
  </div>
</div>
```

### Named Slots

`<slot>` elements have a special attribute, `name`, which can be used to further customize how content should be distributed.
You can have multiple slots with different names.
A named slot will match any element that has a corresponding `slot` attribute in the content fragment.

There can still be one unnamed slot, which is the **default slot** that serves as a catch-all outlet for any unmatched content.
If there is no default slot, unmatched content will be discarded.

For example, suppose we have an `app-layout` component with the following template:

```html
<div class="container">
  <header>
    <slot name="header"></slot>
  </header>
  <main>
    <slot></slot>
  </main>
  <footer>
    <slot name="footer"></slot>
  </footer>
</div>
```

Parent markup:

```html
<app-layout>
  <h1 slot="header">Here might be a page title</h1>

  <p>A paragraph for the main content.</p>
  <p>And another one.</p>

  <p slot="footer">Here's some contact info</p>
</app-layout>
```

The rendered result will be:

```html
<div class="container">
  <header>
    <h1>Here might be a page title</h1>
  </header>
  <main>
    <p>A paragraph for the main content.</p>
    <p>And another one.</p>
  </main>
  <footer>
    <p>Here's some contact info</p>
  </footer>
</div>
```

The content distribution API is a very useful mechanism when designing components that are meant to be composed together.

### Scoped Slots

Vue.js support [scoped slots](https://vuejs.org/v2/guide/components.html#Scoped-Slots).
It allows to pass properties to your slot.
This is not yet supported by Vue GWT.

## Dynamic Components {#dynamic-components}

You can use the same mount point and dynamically switch between multiple components using the reserved `<component>` element and dynamically bind to its `is` attribute:

```java
@Component(components = { TargaryenComponent.class, StarkComponent.class, LannisterComponent.class })
public class HousesComponent extends VueComponent {
    @JsProperty String currentHouse = "targaryen";
}
```

```html
<component v-bind:is="currentHouse">
  <!-- component changes when vm.currentHouse changes! -->
</component>
```

### `keep-alive`

If you want to keep the switched-out components in memory so that you can preserve their state or avoid re-rendering, you can wrap a dynamic component in a `<keep-alive>` element:

```html
<keep-alive>
  <component :is="currentView">
    <!-- inactive components will be cached! -->
  </component>
</keep-alive>
```

Check out more details on `<keep-alive>` in the [API reference](https://vuejs.org/v2/api/#keep-alive).

## Misc

### Authoring Reusable Components

When authoring components, it's good to keep in mind whether you intend to reuse it somewhere else later.
It's OK for one-off components to be tightly coupled, but reusable components should define a clean public interface and make no assumptions about the context it's used in.

The API for a Vue component comes in three parts - props, events, and slots:

- **Props** allow the external environment to pass data into the component

- **Events** allow the component to trigger side effects in the external environment

- **Slots** allow the external environment to compose the component with extra content.

With the dedicated shorthand syntaxes for `v-bind` and `v-on`, the intents can be clearly and succinctly conveyed in the template:

```html
<my-component
  :foo="baz"
  :bar="qux"
  @event-a="doThis()"
  @event-b="doThat()"
>
  <img slot="icon" src="...">
  <p slot="main-text">Hello!</p>
</my-component>
```

### Child Component Refs

Despite the existence of props and events, sometimes you might still need to directly access a child component in Java.
To achieve this you have to assign a reference ID to the child component using `ref`. For example:

```html
<div id="parent">
  <user-profile ref="profile"></user-profile>
</div>
```

```java
@Component(components = UserProfileComponent.class)
public class ParentComponent extends VueComponent implements HasCreated {
    @Override
    public void created() {
        UserProfileComponent userProfileComponent = this.$refs.get("profile");
    }
}
```

When `ref` is used together with `v-for`, the ref you get will be an array containing the child components mirroring the data source.

<p class="info-panel">
    <code>$refs</code> are only populated after the component has been rendered, and it is not reactive.
    It is only meant as an escape hatch for direct child manipulation - you should avoid using <code>$refs</code> in templates or computed properties.
</p>

### Async Components

Vue.js supports [dynamically loading components with caching](https://vuejs.org/v2/guide/components.html#Async-Components).
This is not yet supported by Vue GWT.

### Recursive Components

Components can recursively invoke themselves in their own template.
However, they can only do so with the `name` option:

```java
@Component(name = "unique-name-of-my-component")
```

When you register a component globally using `Vue.component`, the global ID is automatically set as the component's `name` option.

```java
Vue.component("unique-name-of-my-component", MyComponent.class);
```

If you're not careful, recursive components can also lead to infinite loops:

```java
@Component(name = "stack-overflow")
```

```html
<div><stack-overflow></stack-overflow></div>
```

A component like the above will result in a "max stack size exceeded" error, so make sure recursive invocation is conditional (i.e. uses a `v-if` that will eventually be `false`).

Bellow is an example recursive component:

```java
@Component(name = "recursive")
public class RecursiveComponent extends VueComponent implements HasCreated {
    @Prop
    @JsProperty
    Integer counter;

    @Override
    public void created() {
        if (this.counter == null)
            this.counter = 0;
    }
}
```

```html
<span>
    {{ counter }}
    <recursive v-if="counter < 5" :counter="counter + 1"></recursive>
</span>
```

{% raw %}
<div class="example-container" data-name="recursiveComponent">
    <span id="recursiveComponent"></span>
</div>
{% endraw %}

### Circular References Between Components

Let's say you're building a file directory tree, like in Finder or File Explorer.
You might have a `tree-folder` component with this template:

```html
<p>
    <span>{{ myFolder.name }}</span>
    <tree-folder-content v-if="myFolder.hasContent()" :content="myFolder.getContent()"/>
</p>
```

Then a `tree-folder-contents` component with this template:

```html
<vue-gwt:import class="com.mypackage.Folder"/>
<ol>
    <li v-for="Folder child in content">
        <tree-folder :folder="child"/>
    </li>
</ol>
```

When you look closely, you'll see that these components will actually be each other's descendent _and_ ancestor in the render tree - a paradox!
In Vue GWT (like in Vue.js) this paradox is solved automatically, whether you declare your components globally or by passing them to the `@Component` annotation.

Here is a working and running tree example for you:

{% raw %}
<div class="example-container" data-name="treeComponent">
    <span id="treeComponent"></span>
</div>
{% endraw %}

### Inline Templates

Vue.js support [inline templates](https://vuejs.org/v2/guide/components.html#Inline-Templates).
This is not supported by Vue GWT.

### X-Templates

Vue.js support [X-Templates](https://vuejs.org/v2/guide/components.html#X-Templates).
This is not supported by Vue GWT.

### Cheap Static Components with `v-once`

Rendering plain HTML elements is very fast in Vue, but sometimes you might have a component that contains **a lot** of static content.
In these cases, you can ensure that it's only evaluated once and then cached by adding the `v-once` directive to the root element, like this:

```html
<div v-once>
    <h1>Terms of Service</h1>
    ... a lot of static content ...
</div>
```