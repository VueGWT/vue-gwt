!INCLUDE "../dependencies.md"

# GWT Client Bundles (Images)

Vue GWT also support GWT `ClientBundles`.

Let see how this work with an example.

***Example10ClientBundle.java***

First, we create a simple `ClientBundle` with an `ImageResource`:

```java
public interface Example10ClientBundle extends ClientBundle
{
    Example10ClientBundle INSTANCE = GWT.create(Example10ClientBundle.class);

    @Source("example10.jpg")
    ImageResource myImage();
}
```

There is no need for any special annotations on it.

***Example10Component.java***

We then create a Component to use our `ClientBundle`.
In this component we add a public field for our `Example10ClientBundle`.
This will expose the bundle to the Template.

```java
@JsType
@Component
public class Example10Component extends VueComponent
{
    public Example10ClientBundle myBundle;

    @Override
    public void created() {
        myBundle = Example10ClientBundle.INSTANCE;
    }
}
```

***Example10Component.html***

We can then simply access our bundle instance from the template:

```html
<img v-bind:src="myBundle.myImage().getSafeUri().asString()"/>
```


{% raw %}
<p class="example-container" data-name="Live Example 10">
    <span id="example10"></span>
</p>
{% endraw %}

## Hey, Couldn't I Use This for Styles?

Yes you can.
If you want (or have to) separate your Style interface from your GSS files it's still possible to use them in your Template.

In that case:

* Don't add the `@Style` annotation on your Style interface
* Call `ensureInjected()` on your Styles somewhere in your App
* Create a `ClientBundle` for your Style (or add them to an existing one)

You can then expose your Styles (or their `ClientBundle`) in your Component instead of using the preferred `vue-gwt:import` method.