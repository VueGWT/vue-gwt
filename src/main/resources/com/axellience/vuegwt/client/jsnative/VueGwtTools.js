/**
 * This object provides methods to integrate Java in Vue.JS world
 */

window.vueGwt = {
	/**
	 * Convert the Java representation of a VueComponent to a JS object that
	 * can be passed to Vue.JS
	 * This is going to be passed to either new Vue(), Vue.component(), or in a components array of another Component
	 *
	 * @param javaVueComponent
	 * @returns Object Vue component definition
	 */
	javaComponentToVueComponentDefinition: function (javaVueComponent) {
		// Base VueModel definition structure
		var vueComponent = {
			methods: {},
			watch: {},
			computed: {}
		};
		var data = {};

		// Browse all the properties of our java object
		for (var propName in javaVueComponent) {
			if (!javaVueComponent.hasOwnProperty(propName))
				continue;

			this._manageJavaProperty(propName, javaVueComponent[propName], data, vueComponent);
		}

		// Data is always a factory
		vueComponent.data = function () {
			// Each component will get it's own instance of the data model
			return JSON.parse(JSON.stringify(data));
		};

		// Browse all the methods of our java object
		var proto = javaVueComponent.__proto__;
		for (propName in proto) {
			if (!proto.hasOwnProperty(propName))
				continue;

			// Exclude some GWT specific methods and the constructor
			if (propName.indexOf("$init") === 0 || propName.indexOf("___") === 0 || propName == "constructor")
				continue;

			var value = proto[propName];
			if (typeof value != "function") {
				this._manageJavaProperty(propName, value, data, vueComponent);
			}

			// Get computed and watch properties and register them in the right property
			var splitName = propName.split("_");
			if (splitName[0] == "watch") {
				vueComponent.watch[this._removeFirstWord(splitName)] = value;
			} else if (splitName[0] == "computed") {
				vueComponent.computed[this._removeFirstWord(splitName)] = value;
			} else {
				vueComponent.methods[propName] = value;
			}
		}

		return vueComponent;
	},

	_manageJavaProperty: function (propertyName, propertyValue, data, vueModel) {
		if (propertyName.slice(0, 2) == "$$") {
			// Properties starting with $$ are not data properties of our VM
			// but should be directly copied in the vueModelDefinition (example $$el)
			vueModel[propertyName.slice(2)] = propertyValue;
		} else {
			// Other properties are data properties
			data[propertyName] = propertyValue;
		}
	},

	_removeFirstWord: function (splitWords) {
		splitWords.shift();
		return splitWords.join("");
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