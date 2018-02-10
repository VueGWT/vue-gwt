# Vue GWT and Widgets

For easy backward compatibility it's possible to wrap any Vue GWT Component in a GWT `Widget`.
For this you need to use `VueGwtWidget`.

## Adding the dependency

`VueGwtWidget` is in it's own Maven module.
If you want to use it, add this Maven Dependency to your `pom.xml`:
```xml
<dependency>
	<groupId>com.axellience</groupId>
	<artifactId>vue-gwt-gwt2</artifactId>
	<version>${vue-gwt.version}</version>
</dependency>
```
And this in your `gwt.xml`:
```xml
<inherits name='com.axellience.vuegwt.GWT2'/>
``` 

## Instantiating a DemoComponent

Let's instantiate a `DemoComponent` with `VueGwtWidget`:
 
***GwtIndex.html***
 
```html
<div id="childComponentAttachPoint"></div>
```
 
***RootGwtApp.java***
 
```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtWidget<DemoComponent> vueGwtWidget = new VueGwtWidget<>(DemoComponent.class);
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtWidget);
    }
}
```

## Accessing our Vue Instance

It's even possible to interact with our instantiated `Vue`.

Let's say our `DemoComponent` has a method `increaseCounter()`.
We can do:

```java
VueGwtWidget<DemoComponent> vueGwtWidget = new VueGwtWidget<>(DemoComponent.class);
RootPanel.get("childComponentAttachPoint").add(vueGwtWidget);

vueGwtWidget.vue().increaseCounter();
```

## Cleaning Up
If you don't need your `VueGwtWidget` anymore, you can destroy it's Vue instance by calling:

```java
vueGwtWidget.vue().$destroy();
```