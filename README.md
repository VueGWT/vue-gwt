# Vue GWT

Vue GWT integrates [Vue.JS](https://vuejs.org/) with [GWT](http://www.gwtproject.org/) 2.8 using [JsInterop](https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/view).
It let you write Vue.JS components in Java.

<p align="center">
<img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="MIT License"/>
<a href="https://gitter.im/Axellience/vue-gwt"><img src="https://img.shields.io/gitter/room/nwjs/nw.js.svg" alt="Chat"/></a>
</p>

Checkout our **[Vue GWT Demo page](https://axellience.github.io/vue-gwt-demo/)** to see it in action.

It's recommended to read [Vue.JS introduction guide](https://vuejs.org/v2/guide/) if you are not familiar with it.

:warning: Vue GWT is in an **experimental** state.
The syntax is not final and might change between versions.
It may also contain bugs.

## Setup on your project

### :white_check_mark: Get Vue GWT

Vue GWT uses Maven.
For now there is no Maven repository.

You must clone the source on your computer and mvn install them:

```bash
git clone https://github.com/Axellience/vue-gwt.git
cd vue-gwt
mvn clean install
```

### :white_check_mark: Add the Maven dependency
Add Vue GWT to your project `pom.xml`:

```xml
<properties>
    <vue-gwt.version>1.1-SNAPSHOT</vue-gwt.version>
</properties>
<dependency>
    <groupId>com.axellience</groupId>
    <artifactId>vue-gwt</artifactId>
    <version>${vue-gwt.version}</version>
</dependency>
```

### Configure JsInterop
Vue GWT relies heavily on JsInterop.
You must enable it in SuperDevMode and Maven.

#### :white_check_mark: SuperDevMode
For SuperDevMode, simply add this flag to your devMode parameters:

`-generateJsInteropExports`

#### :white_check_mark: Maven
For Maven, if you use [GWT Maven Plugin](https://gwt-maven-plugin.github.io/gwt-maven-plugin/), add the following in your `pom.xml`:

```xml
<plugins>
    <!-- Mojo's Maven Plugin for GWT -->
    <plugin>
        ...
        <configuration>
            ...
            <generateJsInteropExports>true</generateJsInteropExports>
        </configuration>
    </plugin>
</plugins>
```

### :white_check_mark: Add Vue.JS library to your index.html

In your GWT app index.html add the following tag in the &lt;head&gt; section:

```html
<!-- Vue JS Framework -->
<script src="https://unpkg.com/vue/dist/vue.js"></script>
```

You are good to go!

## Simple App

If you already know GWT then you should be familiar with the concept of Widgets.
In Vue.JS the equivalent of Widgets are Components.

Vue Components are like GWT Widgets, they are reusable and isolated pieces of your application.

Let's create our first Component and use it in a real app.
We will call it RootComponent.

***GwtIndex.html***

In our GWT index page we add a div with our root component template.

```html
<div id="rootComponent">
  {{ message }}
</div>
```

***RootComponent.java***

To create your Component, you must create a Class that extends `VueComponent`.
All the public attributes and methods of this class will be accessible in your template.

:exclamation: Don't forget to add the @JsType annotation to your Class.
This ensure that GWT doesn't change their name at compile time.

```java
@JsType
public class RootComponent extends VueComponent
{
    public String message = "Hello Vue GWT!";
}
```

***RootGwtApp.java***

We need to bootstrap our `RootComponent` when GWT starts.
For that we simply call Vue.attach() and pass it an instance of our RootComponent class.

```java
@JsType
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad()
    {
        // When our GWT app starts, we start our Vue app.
        Vue.attach("#rootComponent", new RootComponent());
    }
}
```

Behind the scene, the instance of our Component will be converted to the format that Vue.JS is expecting:
```javascript
{
    el: "#rootComponent",
    data: {
        message: "Hello Vue GWT!"
    }
}
```

And passed down to `new Vue()`.


## Separating the template

Our `RootComponent` works well, but it's not easily reusable because it's template is in our index.html file.

Let's create a reusable Component called `ChildComponent` with it's own template.
We will then instantiate this component in our RootComponent.

This Component will display a message coming from a html text box.

First we create an HTML file with our component template.

***ChildComponent.html***
```html
<div>
    <input type="text" v-model="message"/>
    <p>{{ message }}</p>
    <a href v-on:click="clearMessage">Clear message</a>
</div>
```

We add this template as a GWT resource.

***VueTemplatesResources.java***
```java
public interface VueTemplatesResources extends ClientBundle
{
    VueTemplatesResources TEMPLATES = GWT.create(VueTemplatesResources.class);

    @Source("ChildComponent.html")
    TextResource child();
}
```


***ChildComponent.java***

We can then simply set our Component template in it's constructor.

```java
@JsType
public class ChildComponent extends VueComponent
{
    public String message = "This is my first component!";

    public SimpleComponent()
    {
        this.setTemplate(TEMPLATES.child());
    }

    public void clearMessage() {
        message = "";
    }
}
```

We will then make a few changes to our `RootComponent` to use `ChildComponent`:

***GwtIndex.html***

```html
<div id="rootComponent">
    <child></child>
</div>
```

***RootComponent.java***

```java
@JsType
public class RootComponent extends VueComponent
{
    public RootComponent()
    {
        // ChildComponent is registered to be used in our RootComponent
        this.registerComponent(new ChildComponent());
    }
}
```

#### How is the component html name set?

The name of the html element for our Component in the template (here, `child`) is determined using the Component Class name.

The name is converted from CamelCase to kebab-case.
If the name ends with "Component" this part is dropped.

For example:

 * ChildComponent -> child
 * TodoListComponent -> todo-list
 * Header -> header
 
 

## Vue GWT Panel

For easy backward compatibility it's possible to wrap any Vue GWT Component in a gwt-user Panel.
For this you need to use `VueGwtPanel`

:exclamation: For it to work, your Component must have a template.


For example, let's instantiate our `ChildComponent` using this mechanism:
 
***GwtIndex.html***
 
```html
<div id="childComponentAttachPoint"></div>
```
 
***RootGwtApp.java***
 
```java
@JsType
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad()
    {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtPanel vueGwtPanel = new VueGwtPanel(new RootComponent());
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);
    }
}
```
 
 