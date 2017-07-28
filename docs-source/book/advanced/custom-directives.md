# Custom Directives

!INCLUDE "../dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/custom-directive.html) and has been adapted for Vue GWT.*

## Intro

In addition to the default set of directives shipped in core (`v-model` and `v-show`), Vue also allows you to register your own custom directives.
Note that in Vue 2.0, the primary form of code reuse and abstraction is components - however there may be cases where you just need some low-level DOM access on plain elements, and this is where custom directives would still be useful.
An example would be focusing on an input element, like this one:

{% raw %}
<div class="example-container" data-name="focusDirectiveComponent">
    <span id="focusDirectiveComponent"></span>
</div>
{% endraw %}

When the page loads, that element gains focus (note: autofocus doesn't work on mobile Safari).
In fact, if you haven't clicked on anything else since visiting this page, the input above should be focused now.
Now let's build the directive that accomplishes this.

Fist we declare a Java class, like for our Components.
Except that we extend `VueDirective` and add the `@Directive` annotation.

```java
@Directive
public class FocusDirective extends VueDirective {
    @Override
    public void inserted(Element el, VueDirectiveBinding binding) {
        el.focus();
    }
}
```

We can then register our Directive globally:

```java
public class VueGwtExamplesApp implements EntryPoint {
    public void onModuleLoad() {
        Vue.directive(new FocusDirectiveOptions());
    }
}
```

The name of our directive is determined by the Class name (same as Component).
So in this case the directive will be named `v-focus`.
For `FocusAndHighlightDirective` the name would have been `v-focus-and-highlight`.

If you want to register a directive locally instead, the `@Component` also accept a `directives` option:

```java
@Component(directives = FocusDirective.class)
public class FocusDirectiveComponent extends VueComponent
```

Then in a template, you can use the new `v-focus` attribute on any element, like this:

```html
<input v-focus>
```

## Hook Functions

A directive definition object can override several hook functions (all optional):

- `bind`: called only once, when the directive is first bound to the element.
This is where you can do one-time setup work.

- `inserted`: called when the bound element has been inserted into its parent node (this only guarantees parent node presence, not necessarily in-document).

- `update`: called after the containing component has updated, __but possibly before its children have updated__.
The directive's value may or may not have changed, but you can skip unnecessary updates by comparing the binding's current and old values (see below on hook arguments).

- `componentUpdated`: called after the containing component __and its children__ have updated.

- `unbind`: called only once, when the directive is unbound from the element.

We'll explore the arguments passed into these hooks (i.e. `el` and `binding`) in the next section.

## Directive Hook Arguments

Directive hooks are passed these arguments:

- **el**: The element the directive is bound to. This can be used to directly manipulate the DOM.
- **binding**: An object containing the following properties.
  - **name**: The name of the directive, without the `v-` prefix.
  - **value**: The value passed to the directive. For example in `v-my-directive="1 + 1"`, the value would be `2`.
  - **oldValue**: The previous value, only available in `update` and `componentUpdated`. It is available whether or not the value has changed.
  - **expression**: The expression of the binding as a string. For example in `v-my-directive="1 + 1"`, the expression would be `"1 + 1"`.
  - **arg**: The argument passed to the directive, if any. For example in `v-my-directive:foo`, the arg would be `"foo"`.
  - **modifiers**: An object containing modifiers, if any. For example in `v-my-directive.foo.bar`, the modifiers object would be `{ foo: true, bar: true }`.
- **vnode**: The virtual node produced by Vue’s compiler. See the [VNode API](https://vuejs.org/v2/api/#VNode-Interface) for full details.
- **oldVnode**: The previous virtual node, only available in the `update` and `componentUpdated` hooks.

<p class="warning-panel">
Apart from <code>el</code>, you should treat these arguments as read-only and never modify them.
If you need to share information across hooks, it is recommended to do so through element's <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLElement/dataset">dataset</a>.
</p>

You can see an [example of these arguments values](https://vuejs.org/v2/guide/custom-directive.html#Directive-Hook-Arguments) in the official Vue.js doc.

## Object Literals

If your directive needs multiple values, you can also pass in a JSON object literal.

```html
<div v-demo='{ color: "\"white\"", text: "\"hello!\"" }></div>
```

```java
@Override
public void inserted(Element el, VueDirectiveBinding binding)
{
    JsObject value = (JsObject) binding.value;
    value.get("color") // white
    value.get("text") // hello!
}
```