# Render Functions

!INCLUDE "../dependencies.md"

*This page comes from the [official Vue.js documentation](https://vuejs.org/v2/guide/render-function.html) and has been adapted for Vue GWT.*

## Basics {#basics}

Vue recommends using templates to build your HTML in the vast majority of cases.
There are situations however, where you really need the full programmatic power of Java.
That's where you can use the **render function**, a closer-to-the-compiler alternative to templates.

<p class="warning-panel">
    Render function haven't been tested a lot with Vue GWT, so beware of bugs.
</p>

Let's dive into a simple example where a `render` function would be practical. Say you want to generate anchored headings:

```html
<h1>
  <a name="hello-world" href="#hello-world">
    Hello world!
  </a>
</h1>
```

For the HTML above, you decide you want this component interface:

```html
<anchored-heading :level="1">Hello world!</anchored-heading>
```

When you get started with a component that just generates a heading based on the `level` prop, you quickly arrive at this:

```html
<script type="text/x-template" id="anchored-heading-template">
  <h1 v-if="level === 1">
    <slot></slot>
  </h1>
  <h2 v-else-if="level === 2">
    <slot></slot>
  </h2>
  <h3 v-else-if="level === 3">
    <slot></slot>
  </h3>
  <h4 v-else-if="level === 4">
    <slot></slot>
  </h4>
  <h5 v-else-if="level === 5">
    <slot></slot>
  </h5>
  <h6 v-else-if="level === 6">
    <slot></slot>
  </h6>
</script>
```

```java
@Component
public class AnchoredHeadingComponent implements IsVueComponent {
    @JsProperty
    @Prop(required = true)
    Integer level;
}
```

That template doesn't feel great.
It's not only verbose, but we're duplicating `<slot></slot>` for every heading level and will have to do the same when we add the anchor element.

While templates work great for most components, it's clear that this isn't one of them. So let's try rewriting it with a `render` function:

```java
@Component(hasTemplate = false)
public class AnchoredHeadingComponent implements IsVueComponent, HasRender {
    @JsProperty
    @Prop(required = true)
    public Integer level;

    @Override
    public VNode render(VNodeBuilder builder) {
        return builder.el("h" + this.level, asVue().$slots().get("default"));
    }
}
```

As you may notice, several things are happening:

* We pass the `hasTemplate` property to false to the `@Component` annotation.
This tells Vue GWT that this component doesn't have a template.
* We implements the `HasRender` java interface and Override the `render` function defined in it.

Still the resulting Component is much simpler! Sort of.
The code is shorter, but also requires greater familiarity with Vue instance properties.
In this case, you have to know that when you pass children without a `slot` attribute into a component, like the `Hello world!` inside of `anchored-heading`, those children are stored on the component instance at `$slots.default`.
If you haven't already, **it's recommended to read through the [instance properties API](https://vuejs.org/v2/api/#vm-slots) before diving into render functions.**

## The `VNodeBuilder` Instance {#v-node-builder-instance}

The second thing you'll have to become familiar with is how to use template features in the `VNodeBuilder` builder.
This Vue GWT object wraps the Vue.js `createElement` function to provide a cleaner Java interface.
Here is the `VNodeBuilder` class:

```java
public class VNodeBuilder {
    ...
    
    /**
     * Create an empty VNode
     * @return a new empty VNode
     */
    public VNode el() { ... }

    /**
     * Create a VNode with the given HTML tag
     * @param tag HTML tag for the new VNode
     * @param children Children
     * @return a new VNode of this tag
     */
    public VNode el(String tag, Object... children) { ... }

    /**
     * Create a VNode with the given HTML tag
     * @param tag HTML tag for the new VNode
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @return a new VNode of this tag
     */
    public VNode el(String tag, VNodeData data, Object... children) { ... }


    /**
     * Create a VNode with the given {@link IsVueComponent}
     * @param isVueComponentClass Class for the {@link IsVueComponent} we want
     * @param data Information for the new VNode (attributes...)
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(Class<IsVueComponent> isVueComponentClass, VNodeData data, Object... children) { ... }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueFactory}
     * @param vueFactory {@link VueFactory} for the Component we want
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(VueFactory<IsVueComponent> vueFactory, Object... children) { ... }

    /**
     * Create a VNode with the {@link IsVueComponent} of the given {@link VueJsConstructor}
     * @param vueJsConstructor {@link VueJsConstructor} for the Component we want
     * @param children Children
     * @return a new VNode of this Component
     */
    public VNode el(VueConstructor<Vue> vueConstructor, Object... children) { ... }
}
```

### The `VNodeData` Object In-Depth

One thing to note: similar to how `v-bind:class` and `v-bind:style` have special treatment in templates, they have their own top-level fields in `VNodeData` objects.
This object also allows you to bind normal HTML attributes as well as DOM properties such as `innerHTML` (this would replace the `v-html` directive).

Here is the JavaScript definition of the Data Object:

```js
{
  // Same API as `v-bind:class`
  'class': {
    foo: true,
    bar: false
  },
  // Same API as `v-bind:style`
  style: {
    color: 'red',
    fontSize: '14px'
  },
  // Normal HTML attributes
  attrs: {
    id: 'foo'
  },
  // Component props
  props: {
    myProp: 'bar'
  },
  // DOM properties
  domProps: {
    innerHTML: 'baz'
  },
  // Event handlers are nested under `on`, though
  // modifiers such as in `v-on:keyup.enter` are not
  // supported. You'll have to manually check the
  // keyCode in the handler instead.
  on: {
    click: this.clickHandler
  },
  // For components only. Allows you to listen to
  // native events, rather than events emitted from
  // the component using `vm.$emit`.
  nativeOn: {
    click: this.nativeClickHandler
  },
  // Custom directives. Note that the binding's
  // oldValue cannot be set, as Vue keeps track
  // of it for you.
  directives: [
    {
      name: 'my-custom-directive',
      value: '2',
      expression: '1 + 1',
      arg: 'foo',
      modifiers: {
        bar: true
      }
    }
  ],
  // Scoped slots in the form of
  // { name: props => VNode | Array<VNode> }
  scopedSlots: {
    default: props => createElement('span', props.text)
  },
  // The name of the slot, if this component is the
  // child of another component
  slot: 'name-of-slot',
  // Other special top-level properties
  key: 'myKey',
  ref: 'myRef'
}
```

### Complete Example

With this knowledge, we can now finish the component we started:

```java
@Component(hasTemplate = false)
public class AnchoredHeadingComponent implements IsVueComponent, HasRender {
    private static RegExp camelCasePattern = RegExp.compile("([a-z])([A-Z]+)", "g");

    @JsProperty
    @Prop(required = true)
    Integer level;

    @Override
    public VNode render(VNodeBuilder builder) {
        String text =
            getChildrenTextContent(asVue().$slots().get("default")).trim().replaceAll(" ", "-");

        String headingId = camelCasePattern.replace(text, "$1-$2").toLowerCase();

        return builder.el("h" + this.level,
            builder.el("a",
                VNodeData.get().attr("name", headingId).attr("href", "#" + headingId),
                asVue().$slots().get("default")));
    }

    private String getChildrenTextContent(JsArray<VNode> children) {
        return children.map(child -> {
            if (child.getChildren() != null && child.getChildren().length > 0)
                return getChildrenTextContent(child.getChildren());
            return child.getText();
        }).join("");
    }
}
```

Used in a Component with this template:

```html
<div>
    <anchored-heading level="4">This is H4</anchored-heading>
    <anchored-heading level="5">This is a H5</anchored-heading>
</div>
```

Results in:
{% raw %}
<div class="example-container" data-name="renderAppComponent">
    <span id="renderAppComponent"></span>
</div>
{% endraw %}

### Constraints

#### VNodes Must Be Unique

All VNodes in the component tree must be unique. That means the following render function is invalid:

```java
@Override
public VNode render(VNodeBuilder builder) {
    // Don't do that!
    VNode myParagraph = builder.el("p", "Hello World");
    return builder.el("div", myParagraph, myParagraph);
}
```

## Replacing Template Features with Plain Java {#replacing-template-feature-in-java}

### `v-if` and `v-for`

Wherever something can be easily accomplished in plain JavaScript, Vue render functions do not provide a proprietary alternative.
For example, in a template using `v-if` and `v-for`:

```html
<ul v-if="items.length">
  <li v-for="Item item in items">{{ item.name }}</li>
</ul>
<p v-else>No items found.</p>
```

This could be rewritten with Java `if`/`else` and `map` in a render function:

```java
@Override
public VNode render(VNodeBuilder builder) {
    if (this.items.length) {
        return builder.el("ul", this.items.map(item -> {
            return builder.el("li", item.getName()));
        }));
    }
    
    return createElement("p", "No items found.");
}
```

### `v-model`

There is no direct `v-model` counterpart in render functions - you will have to implement the logic yourself.

This is the cost of going lower-level, but it also gives you much more control over the interaction details compared to `v-model`.

### Event & Key Modifiers

For the `.passive`, `.capture` and `.once` event modifiers, Vue offers prefixes that can be used with `on`:

| Modifier(s) | Prefix |
| ------ | ------ |
| `.passive` | `&` |
| `.capture` | `!` |
| `.once` | `~` |
| `.capture.once` or `.once.capture` | `~!` |

For example:

```java
vNodeData.on("!click", () -> {this.doThisInCapturingMode();})
        .on("~keyup", () -> {this.doThisOnce();})
        .on("~!mouseover", () -> {this.doThisOnceInCapturingMode();});
}
```

For all other event and key modifiers, no proprietary prefix is necessary, because you can simply use event methods in the handler.

| Modifier(s) | Equivalent in Handler |
| ------ | ------ |
| `.stop` | `event.stopPropagation()` |
| `.prevent` | `event.preventDefault()` |
| `.self` | `if (event.target !== event.currentTarget) return` |
| Keys:<br>`.enter`, `.13` | `if (event.keyCode !== 13) return` (change `13` to [another key code](http://keycode.info/) for other key modifiers) |
| Modifiers Keys:<br>`.ctrl`, `.alt`, `.shift`, `.meta` | `if (!event.ctrlKey) return` (change `ctrlKey` to `altKey`, `shiftKey`, or `metaKey`, respectively) |

Here's an example with all of these modifiers used together:

```java
vNodeData.on("keyup", (param) -> {
    KeyboardEvent event = (KeyboardEvent) param;
    
    // Abort if the element emitting the event is not
    // the element the event is bound to
    if (event.target != event.currentTarget) return;
    // Abort if the key that went up is not the enter
    // and the shift key was not held down
    // at the same time
    if (!event.shiftKey || !"Enter".equals(event.code)) return;
    // Stop event propagation
    event.stopPropagation();
    // Prevent the default keyup handler for this element
    event.preventDefault();
    // ...
  }
}
```

### Slots

You can access static slot contents as Arrays of VNodes from [`asVue().$slots()`](https://vuejs.org/v2/api/#vm-slots):

```java
@Override
public VNode render(VNodeBuilder builder) {
  // `<div><slot></slot></div>`
  return builder.el("div", asVue().$slots().get("default"));
}
```

And access scoped slots as functions that return VNodes from [`asVue().$scopedSlots()`](https://vuejs.org/v2/api/#vm-scopedSlots):

```java
@Override
public VNode render(VNodeBuilder builder) {
  // `<div><slot></slot></div>`
  return builder.el(
      "div",
      asVue().$scopedSlots().get("default").execute(JsObject.of("text", this.msg))
  );
}
```

To pass scoped slots to a child component using render functions, use the `scopedSlots` field in VNode data:

```data
vNodeData.scopedSlot("default", props -> {
    JsObject jsProps = (JsObject) props;
    return builder.el("span", jsProps.get("text"));
});
```

## JSX

Vue.js support [JSX templates](https://vuejs.org/v2/guide/render-function.html#JSX) this is not supported by Vue GWT.

## Functional Components

Vue.js support [Functional Components](https://vuejs.org/v2/guide/render-function.html#Functional-Components) this is not yet supported by Vue GWT.

## Template Compilation

Vue GWT uses Vue.js template compiler at Java compile time to compile your HTML template in JavaScript render functions.
If you are curious about how Vue.js compile templates you can [check the template compiler on their documentation](https://vuejs.org/v2/guide/render-function.html#Template-Compilation).