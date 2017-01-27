/**
 * This object provides methods to integrate Java in Vue.JS world
 */

window.vueGwt = {
	VUE_GWT_PREFIX: "$$vue_",

	/**
	 * Convert the Java representation of a VueComponent to a JS object that
	 * can be passed to Vue.JS
	 * This is going to be passed to either new Vue(), Vue.component(), or in a components array of another Component
	 *
	 * @param javaComponentDefinition
	 * @returns Object Vue component definition
	 */
	javaComponentDefinitionToJs: function (javaComponentDefinition) {
		// Base JsComponentDefinition structure
		var jsComponentDefinition = {
			methods: {},
			watch: {},
			computed: {},
			data: {}
		};

		this._processJavaComponentDefinitionToJs(jsComponentDefinition, javaComponentDefinition);

		var data = jsComponentDefinition.data;
		// Data is always a factory
		jsComponentDefinition.data = function () {
			// Each component will get it's own instance of the data model
			return JSON.parse(JSON.stringify(data));
		};

		return jsComponentDefinition;
	},

	_processJavaComponentDefinitionToJs: function (jsComponentDefinition, javaComponentDefinition) {
		var jci = javaComponentDefinition.getJavaComponentInstance();
		var jciProto = jci.__proto__;

		var dataPropertiesNames = javaComponentDefinition.getDataPropertiesNames();
		for (var i = 0; i < dataPropertiesNames.length; i++) {
			var dataPropertyName = dataPropertiesNames[i];

			jsComponentDefinition.data[dataPropertyName] = jci[dataPropertyName];
		}

		var methodsNames = javaComponentDefinition.getMethodsNames();
		for (i = 0; i < methodsNames.length; i++) {
			var methodName = methodsNames[i];

			jsComponentDefinition.methods[methodName] = jciProto[methodName];
		}
		var computed = javaComponentDefinition.getComputed();
		for (i = 0; i < computed.length; i++) {
			var computedNVP = computed[i];

			jsComponentDefinition.computed[computedNVP.getJsName()] = jciProto[computedNVP.getJavaName()];
		}
		var watched = javaComponentDefinition.getWatched();
		for (i = 0; i < watched.length; i++) {
			var watchedNVP = watched[i];

			jsComponentDefinition.watch[watchedNVP.getJsName()] = jciProto[watchedNVP.getJavaName()];
		}

		for (var propName in jci) {
			if (!jci.hasOwnProperty(propName))
				continue;

			// Skip non Vue GWT properties
			if (propName.indexOf(this.VUE_GWT_PREFIX) !== 0)
				continue;

			var propNameWithoutPrefix = propName.slice(this.VUE_GWT_PREFIX.length);
			var splitName = propNameWithoutPrefix.split("_");

			if (splitName.length == 1) {
				jsComponentDefinition[splitName[0]] = jci[propName];
			}
		}
	},

	/**
	 * Convert the Java representation of a VueDirective to a JS object that
	 * can be passed to Vue.JS
	 * This is going to be passed to either Vue.directive(), or in a directives array of a Component
	 *
	 * @param javaVueDirective
	 * @returns Object Vue directive definition
	 */
	javaDirectiveToVueDirectiveDefinition: function (javaVueDirective) {
		// Base VueModel definition structure
		var vueDirective = {};

		// Browse all the methods of our java object
		var proto = javaVueDirective.__proto__;
		for (var functionName in proto) {
			if (!proto.hasOwnProperty(functionName))
				continue;

			// We only take a look at function on the prototype
			var func = proto[functionName];
			if (func instanceof Object)
				continue;

			if (functionName.slice(0, 2) == "$$") {
				vueDirective[functionName.slice(2)] = func;
			}
		}

		return vueDirective;
	},

	/**
	 * Used to emit an event on the given instance
	 * @param vueInstance
	 * @param name
	 * @param value
	 */
	vue$emit: function (vueInstance, name, value) {
		vueInstance.$emit(name, value);
	},

	/**
	 * Used to listen to events on the given instance
	 * @param vueInstance
	 * @param name
	 * @param listener
	 */
	vue$on: function (vueInstance, name, listener) {
		vueInstance.$on(name, listener);
	}
};