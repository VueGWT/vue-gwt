!INCLUDE "../dependencies.md"

# GWT Styles

## Adding Style to Our Component

Vue GWT is compatible with GSS styles.
You can declare your `CssResource` interfaces and use them within your Vue GWT Components.

You also get type checking of your styles rules at compile type.
So if you try to use `myStyle.bob()` in your template, and this style doesn't exist, the compilation will break with an explicit error.

Let see it in action with a simple example.

We will create a simple style that set the text color to red.

***Example9ComponentStyle.java***

First, we create a `CssResource` interface.
We also add the `@Style` annotation to it.
This annotation will automatically generate a GWT Bundle for us.

```java
@Style
public interface Example9ComponentStyle extends CssResource
{
    String red();
}
```

***Example9ComponentStyle.gss***

We then create the style for this interface.
It's important to give the gss file the same name as your Interface.
This is because the binding is made automatically by Vue GWT based on the name.

```css
.red {
    color: red;
}
```

***Example9Component.java***

Let's use our style in a small Component.
For the Java part, nothing is needed, so we can just create an empty Component class.

```java
@JsType
@Component
public class Example9Component extends VueComponent
{
    @Override
    public void created() {}
}
```

***Example9Component.html***

In the template, we can now import our style.
We just have to add an `vue-gwt:import` statement on top of our component.
In the `style` attribute, we put the full class name of our Style interface.
We also add a `name` attribute to give our style a name in the template.

We can then simply use Vue `v-bind` directive to bind our style.

```html
<vue-gwt:import style="com.mypackage.Example9ComponentStyle" name="myStyle"/>
<div v-bind:class="myStyle.red()">
	I'm a red text!
</div>
```

{% raw %}
<p class="example-container" data-name="Live Example 9">
    <span id="example9"></span>
</p>
{% endraw %}

## Adding More Than One CSS Class

You can add more than one CSS Class to your Components by using the Array syntax:

```html
<div v-bind:class="[myStyle.red(), myStyle.bold()]">
	I have more than one CSS Class applied
</div>
```

## Conditional Styling

You can apply a Style conditionally by using a ternary expression (be careful with the quotes):

```html
<div v-bind:class='isInvalid ? myStyle.red() : ""'>
	I'm red if isInvalid is true 
</div>
```

## Conditional Styling With Several CSS Class

You can combine both the Array and the ternary syntax:
 
```html
<div v-bind:class='[isInvalid ? myStyle.red() : "", myStyle.bold()]'>
	I'm red if isInvalid is true, and I'm always bold
</div>
```