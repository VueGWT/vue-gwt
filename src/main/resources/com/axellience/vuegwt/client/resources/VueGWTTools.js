if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.tools)
	window.VueGWT.tools = {};

/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
window.VueGWT.tools.createInstanceForVueClass = function (extendedVueClass) {
	return new extendedVueClass();
};
window.VueGWT.tools.extendVueClass = function (vueClassToExtend, vueComponentOptions) {
	return vueClassToExtend.extend(vueComponentOptions);
};
window.VueGWT.tools.getFunctionBody = function (myFunction) {
	// Get content between first { and last }
	var m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
	// Strip comments
	return m.replace(/^\s*\/\/.*$/mg, '').trim();
};
window.VueGWT.tools.javaArrayToJsArray = function (javaArray) {
	// No conversion is needed, but Java compiler won't agree otherwise
	return javaArray;
};
window.VueGWT.tools.wrapMethodWithBefore = function (object, methodName, beforeMethodCall) {
	var proto = object.__proto__;
	var originalFunc = proto[methodName];
	proto[methodName] = function () {
		beforeMethodCall(object, methodName, arguments);
		return originalFunc.apply(this, arguments);
	};
};
window.VueGWT.tools.wrapMethodWithAfter = function (object, methodName, afterMethodCall) {
	var proto = object.__proto__;
	var originalFunc = proto[methodName];
	proto[methodName] = function () {
		var result = originalFunc.apply(this, arguments);
		afterMethodCall(object, methodName, result, arguments);
		return result;
	};
};
window.VueGWT.tools.wrapMethod = function (object, methodName, beforeMethodCall, afterMethodCall) {
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