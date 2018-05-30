# Dependency Injection

## Why Dependency Injection?

When developing your App *you want to keep a maximum of business logic out of your Components*.
For example you can create Services that can be reused across Components.

But in Vue GWT you are not responsible for your Components instantiation.
Apart from your application root, other Components are instantiated for you by Vue depending on your model.

So how do you pass the instance of those Services to your Components?
Sure you could have a static instance of those Services as a Singleton, but this is awful for testing.

Luckily, **Vue GWT supports Dependency Injection**.
An instance of your Services can be automatically provided when your Components are instantiated.
You can also provide a mock yourself when you are [testing your Components](../tooling/unit-testing.md).

## Setting up Dagger 2

In GWT two solutions exist for Dependency Injection:

* [Dagger 2](https://google.github.io/dagger/users-guide)
* [GIN](https://github.com/nishtahir/google-gin)

In this documentation we will explain Injection with Dagger 2 as GIN is not actively developed anymore and not recommended for new projects.
However if you are using GIN already, Vue GWT also works with it.

To setup Dagger 2 on your project you can follow this guide: [Dependency injection in GWT using Dagger 2](http://www.g-widgets.com/2017/06/28/dependency-injection-in-gwt-using-dagger-2/).

The version of the Maven dependency in the guide is a little out of date.
You can check for the latest version of Dagger GWT to use on the [Dagger GWT Maven repo](https://mvnrepository.com/artifact/com.google.dagger/dagger-gwt).

## Injecting a Vue GWT Component

### Declaring the Component

Let's say we have a `GotQuotesService` that is able to provide us with a random quote from a famous TV Show.

We want to use that service in a `GotQuotesComponent` that will display a random quote.
We simply add `GotQuotesService` as an attribute of our Component with the `@Inject` annotation.

::: warning
It's not possible to use injected constructor parameters as Java constructors are not supported in Vue GWT Components.
:::

```java
@Component
public class GotQuotesComponent implements IsVueComponent, HasCreated {
    @JsProperty GotQuote quote;
    @Inject GotQuotesService gotQuotesService;

    @Override
    public void created() {
        changeQuote();
    }

    @JsMethod
    protected void changeQuote() {
        quote = gotQuotesService.getRandomQuote();
    }
}
```

We can then simply use `quote` in our `GotQuotesComponent` template:

```html
<div>
    <blockquote>
        {{ quote.getText() }}
        <cite>
            **{{ quote.getAuthor() }}**, Season **{{ quote.getSeason() }}**, Episode **{{ quote.getEpisode() }}**
        </cite>
    </blockquote>
    <button @click="changeQuote">Give me another!</button>
</div>
```

To make this Component work we now must find a way to have it Injected.

### Instantiating the `GotQuotesComponent` with Injection

Every Vue GWT Component get an associated Factory generated for them by the Vue GWT annotation processor.
This means that `GotQuotesComponent` has a generated `GotQuotesComponentFactory`.

To bootstrap injection we need to inject `GotQuotesComponentFactory`.
Every instance of our `GotQuotesComponent` created using the injected `GotQuotesComponentFactory` will then be correctly injected.

To inject this factory we declare a Dagger 2 Component.

::: warning
Dagger 2 has it's own `Component` annotation.
So you must be careful to use the right one when declaring your Dagger 2 Component.
:::

```java
@Component // ⚠️ This is Dagger 2 @Component annotation, not the Vue GWT one.
@Singleton
public interface ExampleInjector {
    GotQuotesComponentFactory gotQuoteComponentFactory();
}
```

::: tip
If `GotQuotesComponentFactory` doesn't exist, then check your annotation processor configuration in your IDE to make sure that it's running.
:::


We can then create our Dagger Injector, get our `GotQuotesComponentFactory` and use it to create our injected `GotQuotesComponent`:

```java
public class VueGwtExamplesApp implements EntryPoint { 
    public void onModuleLoad() {
        // Create Dagger Injector
        ExampleInjector exampleInjector = DaggerExampleInjector.builder().build();
        // Get our Factory from the Injector
        GotQuotesComponentFactory factory = exampleInjector.gotQuoteComponentFactory();
        // Create our Component, it will be correctly injected
        GotQuotesComponent component = factory.create();
        // Mount (attach) our Component to an existing div
        component.$mount("#gotQuotesComponent");
    }
}
```

And here is the resulting live Component:

<div class="example-container" data-name="gotQuotesComponent">
    <br/>
    <span id="gotQuotesComponent"></span>
</div>

## Injecting Component's Children

We injected our root Component, that's great, but how about it's children?

Here is the good news: if you inject your root Component, then the whole tree of locally declared Components will be injected 🎉!

For example if you have the following Components, you only need to inject `RootComponentFactory` and use it to create your `RootComponent`.
All the Components used in the template and declared in `components` will be injected.

### RootComponent

```java
@Component(components = {Child1Component.class, Child2Component.class})
public class RootComponent implements IsVueComponent {}
```

```html
<div>
    <!-- Create 10 instances of Child1Component -->
    <child1 v-for="int i in 10"></child1>
    <child2></child2>
</div>
```

### Child1Component

```java
@Component(components = GrandChild1Component.class)
public class Child1Component implements IsVueComponent {
    @Inject MyService myService;
}
```

```html
<div>
    <grand-child1></grand-child1>
</div>
```

### Child2Component

```java
@Component
public class Child2Component implements IsVueComponent {
    @Inject MyService myService;
    @Inject AnotherService anotherService;
}
```

### GrandChild1Component

```java
@Component
public class GrandChild1Component implements IsVueComponent {
    @Inject AnotherService anotherService;
}
```

## Globally Registered Components

It's also possible to inject Components that are declared globally.
For this you only need to inject their factory and use it for the global registration.
When used in the templates, the instances will automatically be injected.

```java
public class VueGwtExamplesApp implements EntryPoint { 
    public void onModuleLoad() {
        ExampleInjector exampleInjector = DaggerExampleInjector.builder().build();
        MyGlobalComponentFactory factory = exampleInjector.getMyGlobalComponentFactory();
        VueGWT.register("my-global-component", factory);
    }
}
```

## Injecting Routes in Vue GWT Router

If you are using [Vue GWT Router](https://github.com/Axellience/vue-router-gwt), you can also inject your Routes.

```java
public class RoutesConfig implements CustomizeOptions {
    @Inject LoginComponentFactory loginComponentFactory;
    @Inject HomeComponentFactory homeComponentFactory;
    @Inject SettingsComponentFactory settingsComponentFactory;

    @Override
    public void customizeOptions(VueComponentOptions vueComponentOptions) {
        RouterOptions routerOptions = new RouterOptions()
            .setMode(RouterMode.HISTORY)
            .addRoute("/login", loginComponentFactory)
            .addRoute("/home", homeComponentFactory)
            .addRoute("/settings", homeComponentFactory);
        
        vueComponentOptions.set("router", new VueRouter(routerOptions));
    }
}
```