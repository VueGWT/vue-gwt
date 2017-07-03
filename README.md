# Vue GWT

Vue GWT integrates [Vue.js](https://vuejs.org/) with [GWT](http://www.gwtproject.org/) 2.8 using [JsInterop](https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/view).
It let you write Vue.js components in Java.

<p align="center">
<img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="MIT License"/>
<a href="https://gitter.im/Axellience/vue-gwt"><img src="https://img.shields.io/gitter/room/nwjs/nw.js.svg" alt="Chat"/></a>
</p>

Checkout our **[Vue GWT Demo page](https://axellience.github.io/vue-gwt-demo/)** to see it in action.

It's recommended to read [Vue.js introduction guide](https://vuejs.org/v2/guide/) if you are not familiar with it.

⚠️ Vue GWT is in an **experimental** state.
The syntax is not final and might change between versions.
It may also contain bugs.

## Features

* VueJS Components with a Java controller
* Java type checking in the templates
* Iterate on Java collections in your template
* Use regular Java objects in your templates


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

This is the equivalent of calling `Vue.component(...)` in Vue.js.

```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
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
    public void onModuleLoad() {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtPanel vueGwtPanel = new VueGwtPanel(RootComponent.class);
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);
    }
}
```
 
 