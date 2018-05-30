# Introduction

Vue GWT integrates [Vue.js](https://vuejs.org/) with [GWT 2.8](http://www.gwtproject.org/) using [JsInterop](https://github.com/google/jsinterop-base) and [Elemental2](https://github.com/google/elemental2).
It lets you write Vue.js components in Java.

## Features

* **Vue.js** Components with a **Java controller**
* Template expressions **type checking** at compile time
* [**Web Components** (Custom Elements)](advanced/custom-elements.md) support
* **HTML templates are compiled** during Java Compilation (only requires Vue.js runtime)
* Use **regular Java Objects and Collections** in your templates
* Supports [**injection** in Components](essentials/dependency-injection.md)
* Supports **most of Vue.js features**
* Integrates with [GWT Resources](gwt-integration/client-bundles-and-styles.md) and [Widgets](gwt-integration/widgets.md)

## Who is this for?

* You want to code a **Vue app with type checking**, and take advantage of GWT optimizations.
* You have **GWT app**, and you want **easy to write views** with **2 way data binding**.
* You have a **Vue app** and you need to **use a Java library in some Components**.

## Let's do this!

When you are ready, you can get started with by **[setting it up on your project](./project-setup.md)**.

<p class="info-panel">
    We are looking for feedback and contributions.
    If you use it on a project, please <a href="https://gitter.im/Axellience/vue-gwt" target="_blank">let us know how it goes</a>.
</p>

*This documentation was last updated for [Vue.js v2.5.16](https://github.com/vuejs/vue/releases/tag/v2.5.16) and Vue GWT 1.0-beta-8*
