# GWT User Panels

For easy backward compatibility it's possible to wrap any Vue GWT Component in a gwt-user `Panel`.
For this you need to use `VueGwtPanel`.

## Instantiating a DemoComponent

For example, let's instantiate a `DemoComponent` using this mechanism:
 
***GwtIndex.html***
 
```html
<div id="childComponentAttachPoint"></div>
```
 
***RootGwtApp.java***
 
```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtPanel<DemoComponent> vueGwtPanel = new VueGwtPanel<>(DemoComponent.class);
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);
    }
}
```

## Accessing our VueComponent Instance

It's even possible to interact with our instantiated `VueComponent`.

Let's say our `DemoComponent` has a method `increaseCounter()`.
We can do:

```java
VueGwtPanel<DemoComponent> vueGwtPanel = new VueGwtPanel<>(DemoComponent.class);
RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);

vueGwtPanel.vue().increaseCounter();
```

## Cleaning Up
If you don't need your `VueGwtPanel` anymore, you can destroy it's VueComponent instance by calling:

```java
vueGwtPanel.vue().$destroy();
```