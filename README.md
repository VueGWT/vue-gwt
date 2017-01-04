# Vue GWT

[![Join the chat at https://gitter.im/vue-gwt/Lobby](https://badges.gitter.im/vue-gwt/Lobby.svg)](https://gitter.im/vue-gwt/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Vue GWT is a wrapper of [Vue.JS](https://vuejs.org/) using [GWT](http://www.gwtproject.org/) 2.8 and [JsInterop](https://docs.google.com/document/d/10fmlEYIHcyead_4R1S5wKGs1t2I7Fnp_PaNaa7XTEk0/view).

:warning: Vue GWT is in an **experimental** state.
Please be careful as it may contain bugs. :warning:

Vue GWT makes heavy use of Vue.JS, it's recommended to read [Vue.JS introduction guide](https://vuejs.org/v2/guide/) if you are not familiar with it.

A simple demo project using Vue GWT is available here: [Vue GWT Demo](https://github.com/Axellience/vue-gwt-demo).

## Introduction

Vue GWT makes it easy to use Vue.JS to build your GWT applications.

### Setup on your project

Vue GWT uses maven.
For now there is now maven repository, so you must clone the source on your computer and mvn install them:

```bash
git clone https://github.com/Axellience/vue-gwt.git
cd vue-gwt
mvn install
```

Then add Vue GWT to your project pom.xml:

```xml
<dependency>
	<groupId>com.axellience</groupId>
	<artifactId>vue-gwt</artifactId>
	<version>${vue-gwt.version}</version>
</dependency>
```

You are ready to go!

### Simple App

The root of a Vue.JS app is a Vue instance.
In Vue GWT we have the notion of `VueApp`.

A `VueApp` is the root of a Vue GWT application.
You can see it as your root component in your GWT application.


***GwtIndex.html***

In our GWT index page we add a div with our app template.

```html
<div id="vueApp">
  {{ message }}
</div>
```

***SimpleVueApp.java***

This is our VueApp.
It will be instantiated on the template above. 

```java
@JsType
public class SimpleApp extends VueApp
{
	public String message = "Hello Vue GWT!";

	public SimpleApp()
	{
		// We pass the selector on which this vue app should initialize 
		this.init("#vueApp");
    }
}
```

***RootGwtApp.java***

We need to bootstrap our VueApp when GWT starts.
For that we simply call Vue.app() and pass it an instance of our app.

```java
@JsType
public class RootGwtApp implements EntryPoint {
	public void onModuleLoad()
	{
		// When our GWT app starts, we start our Vue app.
		Vue.app(new SimpleApp());
	}
}
```

## Components
If you already know GWT then you should be familiar with the concept of Widgets.
Vue Components are like GWT Widgets, they are reusable and isolated pieces of your application.

There is three main difference between Components and App in GWT Vue:

* Components have their template in an isolated HTML file
* Components have a tag name that you can use to instantiate them in your Vue App.

Let's see how to make a simple component in Vue GWT:

***SimpleComponent.html***
```html
<div>
  	{{ message }}
	<input type="text" v-model="message"/>
	<a href v-on:click="resetMessage()">Reset message</a>
</div>
```

***SimpleComponent.java***
```java
@JsType
public class SimpleComponent extends VueComponent
{
	public String message = "This is my first component!";

	public SimpleComponent()
	{
		// Init the component with the name "simple-component"
		this.init("simple-component", TEMPLATES.simpleComponent());
	}
    
	public void resetMessage() {
		message = "This is my first component!";
	}
}
```

We will then make a few changes to our VueApp to use this component:

***GwtIndex.html***

```html
<div id="vueApp">
	<!-- We use our component in our app template. -->
	<simple-component></simple-component>
</div>
```

***RootGwtApp.java***

```java
@JsType
public class RootGwtApp implements EntryPoint {
	public void onModuleLoad()
	{
		// We register our component in Vue
		Vue.component(new SimpleComponent());
		// When our GWT app starts, we start our Vue app.
		Vue.app(new SimpleApp());
	}
}
```

## Vue GWT Panel

For easy backward compatibility it's possible to wrap any GWT Vue Component in a gwt-user Panel.
For this you need to use `VueGwtPanel`

