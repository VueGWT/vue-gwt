# Unsupported Features

Vue GWT doesn't support all of Vue.js features yet.
If you would like to help we are [open to contributions](https://github.com/Axellience/vue-gwt)!

Here is the list of unsupported features:

* [Mixins](https://vuejs.org/v2/guide/mixins.html)
    * For now you can use [Java inheritance to extend a given Component](../composition/extending-components.md) instead.
* [Filters](https://vuejs.org/v2/guide/filters.html)
    * Would require the Vue GWT parser to support those expressions. Type checking would also be challenging.
* [State Management](https://vuejs.org/v2/guide/state-management.html)
    * Would require JsInterop for Vuex.
* [Server Side Rendering](https://vuejs.org/v2/guide/ssr.html)
    * Vue GWT components compiled and loaded in a Vue.js app should work server side (but not tested).
    * Full Vue GWT app would probably not work server side or would require lots of work.