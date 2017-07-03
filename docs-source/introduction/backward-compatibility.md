# Backward Compatibility

For easy backward compatibility it's possible to wrap any Vue GWT Component in a gwt-user `Panel`.
For this you need to use `VueGwtPanel`

For example, let's instantiate our `DemoComponent` using this mechanism:
 
***GwtIndex.html***
 
```html
<div id="childComponentAttachPoint"></div>
```
 
***RootGwtApp.java***
 
```java
public class RootGwtApp implements EntryPoint {
    public void onModuleLoad() {
        // Create a VueGwtPanel, it's a regular GWT Widget and can be attached to any GWT Widget
        VueGwtPanel vueGwtPanel = new VueGwtPanel(DemoComponent.class);
        
        // Attach it to inside our DOM element
        RootPanel.get("childComponentAttachPoint").add(vueGwtPanel);
    }
}
```