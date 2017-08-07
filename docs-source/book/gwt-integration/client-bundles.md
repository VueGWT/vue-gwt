# GWT Client Bundles (Images)

!INCLUDE "../dependencies.md"

Vue GWT also support GWT `ClientBundles`.

Let see how this work with an example.

***KittenClientBundle.java***

First, we create a simple `ClientBundle` with an `ImageResource`:

```java
public interface KittenClientBundle extends ClientBundle
{
    KittenClientBundle INSTANCE = GWT.create(KittenClientBundle.class);

    @Source("kitten.jpg")
    ImageResource myKitten();
}
```

There is no need for any special annotations on it.

***KittenClientBundle.java***

We then create a Component to use our `ClientBundle`.
In this component we add a public field for our `KittenClientBundle`.
This will expose the bundle to the Template.

```java
@Component
public class KittenComponent extends VueComponent {
    @JsProperty KittenClientBundle myKittenBundle = KittenClientBundle.INSTANCE;
}
```

***KittenClientBundle.html***

We can then simply access our bundle instance from the template:

```html
<img v-bind:src="myKittenBundle.myKitten().getSafeUri().asString()"/>
```


{% raw %}
<div class="example-container" data-name="kittenComponent">
    <span id="kittenComponent"></span>
</div>
{% endraw %}

## Hey, Couldn't I Use This for Styles?

Yes you can.
If you want (or have to) separate your Style interface from your GSS files it's still possible to use them in your Template.

In that case:

* Don't add the `@Style` annotation on your Style interface
* Call `ensureInjected()` on your Styles somewhere in your App
* Create a `ClientBundle` for your Style (or add them to an existing one)

You can then expose your Styles (or their `ClientBundle`) in your Component instead of using the preferred `vue-gwt:import` method.