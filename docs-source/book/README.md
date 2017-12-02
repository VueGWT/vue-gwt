# Vue GWT

<p align="center">
    <a href="https://axellience.github.io/vue-gwt/">
        <img src="https://axellience.github.io/vue-gwt/resources/images/Vue-GWT-logo.png" alt="Vue GWT Logo" style="max-height: 100px"/>
    </a>
</p>

Vue GWT integrates [Vue.js](https://vuejs.org/) with [GWT 2.8](http://www.gwtproject.org/) using [JsInterop](https://github.com/google/jsinterop-base) and [Elemental2](https://github.com/google/elemental2).
It lets you write Vue.js components in Java.

<p align="center">
    <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="MIT License"/>
    <a href="https://gitter.im/Axellience/vue-gwt"><img src="https://img.shields.io/gitter/room/nwjs/nw.js.svg" alt="Chat"/></a>
</p>

## Features

* **Vue.js** Components with a **Java controller**
* Template expressions **type checking** at compile time
* [**Web Components** (Custom Elements)](advanced/custom-elements.md) support
* **HTML templates are compiled** during Java Compilation (only requires Vue.js runtime)
* Use **regular Java Objects and Collections** in your templates
* Supports [**injection** in Components](essential/dependency-injection.md)
* Supports **most of Vue.js features**
* Integrates with [GWT Resources](gwt-integration/client-bundles.md) and [Widgets](gwt-integration/widgets.md)

## Who is this for?

* You want to code a **Vue app with type checking**, and take advantage of GWT optimizations. 
* You have **GWT app**, and you want **easy to write views** with **2 way data binding**.
* You have a **Vue app** and you need to **use a Java library in some Components**.

## What does it look like?

You can start reading **[our documentation](introduction/README.md)** it contains live examples. 
You can also checkout the **[Vue GWT Demo page](https://axellience.github.io/vue-gwt-demo/)**.

Curious about the implementation? See the sources in our public **[Github repository](https://github.com/Axellience/vue-gwt)**.

## Let's do this!

When you are ready, you can get started with by **[setting it up on your project](./project-setup.md)**.

<p class="info-panel">
    Vue GWT is in an <strong>beta</strong> state.
    It may still contain some bugs.<br/>
    We are looking for feedback and contributions.
    If you use it on a small project, please <a href="https://gitter.im/Axellience/vue-gwt" target="_blank">let us know how it goes</a>.
</p>

*This documentation was last updated for [Vue.js v2.5.9](https://github.com/vuejs/vue/releases/tag/v2.5.9) and Vue GWT 1.0-beta-5*

## Who made this?

<p align="center">
    Vue GWT is developed by the awesome people at<br/>
    <a href="https://www.genmymodel.com" target="_blank">
        <img src="https://axellience.github.io/vue-gwt/resources/images/GenMyModel-Logo-Black.png" alt="GenMyModel" height="50"/>
    </a>
</p>