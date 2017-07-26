/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
window.vueGwtTools = {
	createInstanceForVueClass: function (extendedVueClass) {
		return new extendedVueClass();
	},
	extendVueClass: function (vueClassToExtend, vueComponentOptions) {
		return vueClassToExtend.extend(vueComponentOptions);
	},
	getFunctionBody: function (myFunction) {
		// Get content between first { and last }
		var m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
		// Strip comments
		return m.replace(/^\s*\/\/.*$/mg, '').trim();
	},
	javaArrayToJsArray: function (javaArray) {
		// No conversion is needed, but Java compiler won't agree otherwise
		return javaArray;
	},
	wrapMethodWithBefore: function (object, methodName, beforeMethodCall) {
		var proto = object.__proto__;
		var originalFunc = proto[methodName];
		proto[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			return originalFunc.apply(this, arguments);
		};
	},
	wrapMethodWithAfter: function (object, methodName, afterMethodCall) {
		var proto = object.__proto__;
		var originalFunc = proto[methodName];
		proto[methodName] = function () {
			var result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	},
	wrapMethod: function (object, methodName, beforeMethodCall, afterMethodCall) {
		var proto = object.__proto__;
		var originalFunc = proto[methodName];
		proto[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			var result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	}
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