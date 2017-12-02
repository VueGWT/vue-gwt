# Client Bundles and Styles

!INCLUDE "../dependencies.md"

You can use any valid Java expression in your template.
This means that Vue GWT is compatible with GWT 2.x `ClientBundles`.

## Exposing a `ClientBundle` to the Template {#client-bundles}

Lets see how this work with an example.

First, we create a simple `ClientBundle` with an `ImageResource`:

```java
public interface KittenClientBundle extends ClientBundle {
    KittenClientBundle INSTANCE = GWT.create(KittenClientBundle.class);

    @Source("kitten.jpg")
    ImageResource myKitten();
}
```

There is no need for any special annotations on it.

We then create a Component to use our `ClientBundle`.
In this component we add a computed property for our `KittenClientBundle`.
This will expose the bundle to the Template.

```java
@Component
public class KittenComponent extends VueComponent {
    @Computed
    public KittenClientBundle getMyKittenBundle() {
        return KittenClientBundle.INSTANCE;
    }
}
```

We can then simply access our bundle instance from the template:

```html
<img v-bind:src="myKittenBundle.myKitten().getSafeUri().asString()"/>
```

{% raw %}
<div class="example-container" data-name="kittenComponent">
    <span id="kittenComponent"></span>
</div>
{% endraw %}

You could also directly expose your ImageResource to the template, it's really up to you.

<p class="info-panel">
    You can use <a href="../composition/extending-components.html">Component inheritance</a> to declare a base component in your app and avoid
    having to expose your resources/styles manually in every components.
</p>

## Using `CssResources` in Vue GWT {#styles}

Vue GWT is compatible with GSS styles.
You can declare your `CssResource` interfaces and use them within your Vue GWT Components.

You also get type checking of your styles rules at compile type.
So if you try to use `myStyle.bob()` in your template, and this style doesn't exist, the compilation will break with an explicit error.

Let see it in action with a simple example.

### Setting up a Small `CssResource`

Let's say we have the following `CssResource` (and it's associated `gss` file):

```java
public interface MelisandreComponentStyle extends CssResource {
    String red();
    String bold();
}
```

Our `MelisandreComponentStyle` is exposed in an `AppClientBundle`:

```java
public interface AppClientBundle extends ClientBundle {
    AppClientBundle INSTANCE = GWT.create(AppClientBundle.class);

    @Source("melisandreComponentStyle.gss")
    MelisandreComponentStyle melisandreStyle();
}
```

And in our `RootGwtApp` we call `ensureInjected` to make sure it's injected:

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        VueGWT.init();
        AppClientBundle.INSTANCE.melisandreStyle().ensureInjected();
        // ...
    }
}
```

### Using Our Small `CssResource` in a Component

Let's use our style in a small Component.
As with the `ClientBundle`, we can use a computed method to expose our Style.

```java
@Component
public class MelisandreComponent extends VueComponent {
    @Computed
    public MelisandreComponentStyle getMyStyle() {
        return MelisandreComponentClientBundle.INSTANCE.melisandreComponentStyle();
    }
}
```

We can then simply use Vue `v-bind` directive to bind our style.

```html
<div v-bind:class="myStyle.red()">
    For the night is dark and full of terrors.
</div>
```

#### Adding More Than One CSS Class

You can add more than one CSS Class to your Components by using the [`array` builder](../js-interop/README.md#array):

```html
<div v-bind:class="array(myStyle.red(), myStyle.bold())">
    For the night is dark, full of terrors and bold.
</div>
```

#### Conditional Styling

You can apply a Style conditionally.
First we add a boolean in our `MelisandreComponent`:

```java
@Component
public class MelisandreComponent extends VueComponent {
    @JsProperty boolean isRed = true;
    
    @Computed
    public MelisandreComponentStyle getMyStyle() {
        return MelisandreComponentClientBundle.INSTANCE.melisandreComponentStyle();
    }
}
```
 
Then we simply use a ternary expression (be careful with the quotes):

```html
<div v-bind:class='isRed ? myStyle.red() : ""'>
    For the night is dark, full of terrors and might or not be red.
</div>
```

#### Conditional Styling With Several CSS Class

You can combine both the `array` and the ternary syntax:
 
```html
<div v-bind:class='array(isRed ? myStyle.red() : "", myStyle.bold())'>
    For the night is dark, full of terrors, might or not be red and always BOLD.
</div>
```

#### Here is our finished `MelisandreComponent`

{% raw %}
<div class="example-container" data-name="melisandreComponent">
    <span id="melisandreComponent"></span>
</div>
{% endraw %}

You can try toggling the red color on the two bottom sentences by typing in your browser console:

```
melisandreComponent.isRed = false;
```

<p class="info-panel">
    You can use <a href="../composition/extending-components.html">Component inheritance</a> to declare a base component in your app and avoid
    having to expose your resources/styles manually in every components.
</p>