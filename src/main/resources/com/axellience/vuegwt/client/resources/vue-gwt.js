if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.tools)
	window.VueGWT.tools = {};

const tools = window.VueGWT.tools;

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
	const m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
	// Strip comments
	return m.replace(/^\s*\/\/.*$/mg, '').trim();
};
tools.javaArrayToJsArray = function (javaArray) {
	// No conversion is needed, but Java compiler won't agree otherwise
	return javaArray;
};
tools.wrapMethodWithBefore = function (object, methodName, beforeMethodCall) {
	const proto = object.__proto__;
	const originalFunc = proto[methodName];
	proto[methodName] = function () {
		beforeMethodCall(object, methodName, arguments);
		return originalFunc.apply(this, arguments);
	};
};
tools.wrapMethodWithAfter = function (object, methodName, afterMethodCall) {
	const proto = object.__proto__;
	const originalFunc = proto[methodName];
	proto[methodName] = function () {
		var result = originalFunc.apply(this, arguments);
		afterMethodCall(object, methodName, result, arguments);
		return result;
	};
};
tools.wrapMethod = function (object, methodName, beforeMethodCall, afterMethodCall) {
	const proto = object.__proto__;
	const originalFunc = proto[methodName];
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
			const jciProto = this.$options.vuegwt$javaComponentInstance.__proto__;
			for (let protoProp in jciProto) {
				if (jciProto.hasOwnProperty(protoProp) && !this.hasOwnProperty(protoProp)) {
					this[protoProp] = jciProto[protoProp];
				}
			}
		}
	})
});if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.observerManager)
	window.VueGWT.observerManager = {};

const observerManager = window.VueGWT.observerManager;

/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
observerManager.customizeVueObserver = function (ob) {
	const obProto = ob.__proto__;
	const vueWalk = obProto.walk;
	obProto.walk = function () {
		const obj = arguments[0];
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
});