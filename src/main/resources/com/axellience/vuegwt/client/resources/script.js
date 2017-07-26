if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.jsTools)
	window.VueGWT.jsTools = {};

var jsTools = window.VueGWT.jsTools;
/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
jsTools.defineProperty = function (object, name, definition) {
	var def = {};
	if ('get' in definition) {
		def.get = function () {
			return definition.get(this);
		};
	}

	if ('set' in definition) {
		def.set = function (value) {
			definition.set(this, value);
		}
	}

	Object.defineProperty(object, name, def);
};
jsTools.getWindow = function () {
	return window;
};
jsTools.objectHasProperty = function (object, property) {
	return property in object;
};
jsTools.getObjectProperty = function (object, property) {
	return object[property];
};
jsTools.setObjectProperty = function (object, property, value) {
	object[property] = value;
};
jsTools.unsetObjectProperty = function (object, property) {
	delete object[property];
};
jsTools.setObjectFunction = function (object, property, func) {
	object[property] = function () {
		return func.exec.apply(this, arguments);
	};
};
jsTools.getArrayItem = function (array, index) {
	return array[index] || null;
};
jsTools.setArrayItem = function (array, index, value) {
	array[index] = value;
};
jsTools.call = function (func, thisArg, args) {
	return func.call(thisArg, args);
};
jsTools.getDeepValue = function (obj, path) {
	for (var i = 0, pathObj = path.split('.'), len = pathObj.length; i < len; i++) {
		obj = obj[pathObj[i]];
	}
	return obj;
};if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.observerManager)
	window.VueGWT.observerManager = {};

var observerManager = window.VueGWT.observerManager;

/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
observerManager.customizeVueObserver = function (ob) {
	var obProto = ob.__proto__;
	var vueWalk = obProto.walk;
	obProto.walk = function () {
		var obj = arguments[0];
		if (observerManager.observeJavaObject(obj))
			return;

		return vueWalk.apply(this, arguments);
	};
};

/**
 * Catch Vue Observer Prototype
 * Only way for now, if we don't want to fork Vue.js
 */
new Vue({
	created: function () {
		observerManager.customizeVueObserver(this.$data.__ob__);
	}
});if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.tools)
	window.VueGWT.tools = {};

var tools = window.VueGWT.tools;

/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
tools.createInstanceForVueClass = function (extendedVueClass) {
	return new extendedVueClass();
};
tools.extendVueClass = function (vueClassToExtend, vueComponentOptions) {
	return vueClassToExtend.extend(vueComponentOptions);
};
tools.getFunctionBody = function (myFunction) {
	// Get content between first { and last }
	var m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
	// Strip comments
	return m.replace(/^\s*\/\/.*$/mg, '').trim();
};
tools.javaArrayToJsArray = function (javaArray) {
	// No conversion is needed, but Java compiler won't agree otherwise
	return javaArray;
};
tools.wrapMethodWithBefore = function (object, methodName, beforeMethodCall) {
	var proto = object.__proto__;
	var originalFunc = proto[methodName];
	proto[methodName] = function () {
		beforeMethodCall(object, methodName, arguments);
		return originalFunc.apply(this, arguments);
	};
};
tools.wrapMethodWithAfter = function (object, methodName, afterMethodCall) {
	var proto = object.__proto__;
	var originalFunc = proto[methodName];
	proto[methodName] = function () {
		var result = originalFunc.apply(this, arguments);
		afterMethodCall(object, methodName, result, arguments);
		return result;
	};
};
tools.wrapMethod = function (object, methodName, beforeMethodCall, afterMethodCall) {
	var proto = object.__proto__;
	var originalFunc = proto[methodName];
	proto[methodName] = function () {
		beforeMethodCall(object, methodName, arguments);
		var result = originalFunc.apply(this, arguments);
		afterMethodCall(object, methodName, result, arguments);
		return result;
	};
};

/**
 * Vue GWT plugin that does the required transformations
 * on Vue Instances to make them compatible with Java
 */
Vue.use(function (Vue) {
	Vue.mixin({
		created: function () {
			if (!this.$options.vuegwt$javaComponentInstance)
				return;

			// This is required for GWT type checking to work
			var jciProto = this.$options.vuegwt$javaComponentInstance.__proto__;
			for (var protoProp in jciProto) {
				if (jciProto.hasOwnProperty(protoProp) && !this.hasOwnProperty(protoProp)) {
					this[protoProp] = jciProto[protoProp];
				}
			}
		}
	})
});