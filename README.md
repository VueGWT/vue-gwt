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

## Features

* VueJS Components with a Java controller
* Java type checking in the templates
* Iterate on Java collections in your template
* Use regular Java objects in your templates

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

### :white_check_mark: Configure Maven

#### Add the dependency
Add Vue GWT to your project `pom.xml`:

```xml
<properties>
    <vue-gwt.version>1.2-SNAPSHOT</vue-gwt.version>
</properties>
<dependency>
    <groupId>com.axellience</groupId>
    <artifactId>vue-gwt</artifactId>
    <version>${vue-gwt.version}</version>
</dependency>
```

#### Add Annotation processing configuration
In the `plugins` section:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <!-- version is important to have java annotation processing correctly handled -->
    <version>3.3</version><!--$NO-MVN-MAN-VER$-->
    <configuration>
        <compilerArgument>-parameters</compilerArgument>
        <testCompilerArgument>-parameters</testCompilerArgument>
        <useIncrementalCompilation>false</useIncrementalCompilation>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

This is useful for Eclipse:
```xml
<pluginManagement>
    <plugins>
        <!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
        <plugin>
            <groupId>org.eclipse.m2e</groupId>
            <artifactId>lifecycle-mapping</artifactId>
            <version>1.0.0</version>
            <configuration>
                <lifecycleMappingMetadata>
                    <pluginExecutions>
                        <pluginExecution>
                            <pluginExecutionFilter>
                                <groupId>
                                    org.codehaus.mojo
                                </groupId>
                                <artifactId>
                                    gwt-maven-plugin
                                </artifactId>
                                <versionRange>
                                    [2.7.0,)
                                </versionRange>
                                <goals>
                                    <goal>compile</goal>
                                </goals>
                            </pluginExecutionFilter>
                            <action>
                                <ignore></ignore>
                            </action>
                        </pluginExecution>
                    </pluginExecutions>
                </lifecycleMappingMetadata>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
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

### IDE Configuration

#### Eclipse
Just import your project as a Maven project.

#### IntelliJ IDEA
By default IntelliJ doesn't support automatic compilation on file change.
But don't worry, enabling it is easy!

Go to `File -> Settings -> Build, Execution, Deployment -> Compiler` and enable “Make project automatically”

Open the Action window :
* Linux : `CTRL+SHIFT+A`
* Mac OSX : `SHIFT+COMMAND+A`
* Windows : `CTRL+ALT+SHIFT+/`

Enter `Registry...` and enable `compiler.automake.allow.when.app.running`

You are good to go!

## Simple App

If you already know GWT then you should be familiar with the concept of Widgets.
In Vue.JS the equivalent of Widgets are Components.

Vue Components are like GWT Widgets, they are reusable and isolated pieces of your application.

Let's create our first Component and use it in a real app.
We will call it RootComponent.

***GwtIndex.html***

In our GWT index page we add a div to attach our root component.

```html
<div id="rootComponent"></div>
```

***RootComponent.java***

To create your Component, you must create a Class annotated by @Component and @JsType that extends `VueComponent`.
All the public attributes and methods of this class will be accessible in your template.

:exclamation: Don't forget to add the @JsType annotation to your Class.
This ensure that GWT doesn't change their name at compile time.

```java
@Component
@JsType
public class RootComponent extends VueComponent
{
    public String message = "Hello Vue GWT!";
}
```

***RootComponent.html***

We then create our Component template.
We place this file next to our Java class file.
VueGWT will detect it automatically and use it as the template.

```html
<div>
    {{ message }}
</div>
```

***RootGwtApp.java***

We need to bootstrap our `RootComponent` when GWT starts.
For that we simply call Vue.attach() and pass it our RootComponent class.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad()
    {
        // When our GWT app starts, we start our Vue app.
        Vue.attach("#rootComponent", RootComponent.class);
    }
}
```

Behind the scene, the instance of our Component will be converted to the format that Vue.JS is expecting:
```javascript
{
    el: "#rootComponent",
    template: "<div>{{ message }}</div>",
    data: {
        message: "Hello Vue GWT!"
    }
}
```

And passed down to `new Vue()`.


## Creating child components

Every components can have children.

Let's create a child Component called `ChildComponent`.
We will then instantiate this component in our RootComponent.

This Component will display a message coming from a html text box.

***ChildComponent.java***
```java
@Component
@JsType
public class ChildComponent extends VueComponent
{
    public String childMessage = "Hello Vue GWT!";
}
```

First we create an HTML file with our component template.
We add this file next to our Java class as before.

***ChildComponent.html***
```html
<div>
    <input type="text" v-model="childMessage"/>
    <p>{{ childMessage }}</p>
    <a href v-on:click="clearMessage">Clear message</a>
</div>
```

We will then make a few changes to our `RootComponent` to use `ChildComponent`:


***RootComponent.html***

```html
<div>
    <child></child>
</div>
```

***RootComponent.java***

```java
// ChildComponent is registered to be used in our RootComponent by passing it to the annotation
@Component(components = {ChildComponent.class})
@JsType
public class RootComponent extends VueComponent
{
}
```

### How is the component html name set?

The name of the html element for our Component in the template (here, `child`) is determined using the Component Class name.

The name is converted from CamelCase to kebab-case.
If the name ends with "Component" this part is dropped.

For example:

 * ChildComponent -> child
 * TodoListComponent -> todo-list
 * Header -> header
 
### Registering components globally

Components can also be registered globally.
They will then be usable in any component template in your app.

This is the equivalent of calling `Vue.component(...)` in Vue.JS.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad()
    {
        // Register ChildComponent globally
        Vue.registerComponent(ChildComponent.class);
    }
}
```
 

## Vue GWT Panel

For easy backward compatibility it's possible to wrap any Vue GWT Component in a gwt-user Panel.
For this you need to use `VueGwtPanel`

For example, let's instantiate our `ChildComponent` using this mechanism:
 
***GwtIndex.html***
 
```html
<div id="childComponentAttachPoint"></div>
```
 
***RootGwtApp.java***
 
```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad()
    {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtPanel vueGwtPanel = new VueGwtPanel(RootComponent.class);
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);
    }
}
```
 
 