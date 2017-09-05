# Plugins

Vue GWT transform your Java Component to regular `ComponentOptions` that are then passed to Vue.
Because of this, it is compatible in theory with most Vue plugins.

Depending on the plugin, integration might be more or less complicated.

## Steps Required

### Integrate the JS

Usually plugins require you to integrate some more JS.
You can just integrate the file in your index.html.
If you start to have a lot of dependencies you might have to start concatenating them together to improve load time.

### Options and Configuration

Most plugins have options or configuration objects.
You will have to either write the JsInterop classes for those, or manipulate directly [JsObject](../js-interop/README.md#js-object) and [JsArray](../js-interop/README.md#js-array) to build the required objects.

In the future we will probably have something to convert TypeScript definitions to Java JsInterop objects, but for now this has to be done manually.

### Configuring Components

Some plugins, like Vue Router will require you to customize options when declaring your Component.

For this, create a class that extends `CustomizeOptions` and override `customizeOptions(VueComponentOptions componentOptions)` in it.
This method will be called once when translating your Java Component to `VueComponentOptions` to pass to Vue.js.
You can simply set options on the given `componentOptions` parameter and they will be passed along to Vue.js.

If you need, you can inject attributes in your `CustomizeOptions` class.

To bind this class to your Component, simply pass it to the `customizeOptions` parameter of your `@Component` annotation.

If you need, you can even add your own methods, data or any property from [ComponentOptions](https://github.com/vuejs/vue/blob/dev/types/options.d.ts).

### Escape Hatch

Remember that you can always integrate regular JS in your `index.html`.
This can be useful if your plugin requires some kind of registration, or other step that are not yet doable in Java.

## Example With Vue Router

We built a [JsInterop wrapper of the Vue Router](routing.md) plugin for you.
You can [checkout how it works](https://github.com/Axellience/vue-router-gwt) if you need a working example of integration.