# And more!

## Might Require Some 3rd Party Integrations

These features work for the most part, but might require JsInterop classes for some external JavaScript libraries (or the usage of Java ones).

* [Transition Effects](https://vuejs.org/v2/guide/transitions.html)
    * Will work for the most part, except when using Velocity (which would require JsInterop for [Velocity](http://velocityjs.org/)).
* [Transitioning State](https://vuejs.org/v2/guide/transitioning-state.html)
    * You would need to code the transitions code yourself, or code JsInterop for [tween.js](https://github.com/tweenjs/tween.js/).

## Unsupported Features

Vue GWT doesn't support all of Vue.js features yet.
If you would like to help we are [open to contributions](https://github.com/Axellience/vue-gwt)!

Here is the list of unsupported features:

* [Render Functions](https://vuejs.org/v2/guide/render-function.html)
    * Require JsInterop classes for VNodes.
* [Mixins](https://vuejs.org/v2/guide/mixins.html)
    * Would be pretty hard to achieve because of Java single inheritance and type checking in templates.
* [State Management](https://vuejs.org/v2/guide/state-management.html)
    * Would require JsInterop for Vuex.
* [Unit Testing](https://vuejs.org/v2/guide/unit-testing.html)
    * JS Unit testing should work if you load compiled GWT app in a JS environment and expose the JS Components (not tested).
    * Java Unit testing won't work for now, because it won't find implementation for JsObject and JsArray (maybe using [Nashorn](http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html) in Java 8?).
* [Server Side Rendering](https://vuejs.org/v2/guide/ssr.html)
    * Vue GWT components compiled and loaded in a Vue.js app should work server side (but not tested).
    * Full Vue GWT app would probably not work server side or would require lots of work.