(function (context) {
	if (!context.VueGWT)
		context.VueGWT = {};
	if (!context.VueGWT.tools)
		context.VueGWT.tools = {};

	const tools = context.VueGWT.tools;

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
	tools.javaArrayToJsArray = function (javaArray) {
		// No conversion is needed, but Java compiler won't agree otherwise
		return javaArray;
	};
	tools.wrapMethodWithBefore = function (object, methodName, beforeMethodCall) {
		const proto = Object.getPrototypeOf(object);
		const originalFunc = proto[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		proto[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			return originalFunc.apply(this, arguments);
		};
	};
	tools.wrapMethodWithAfter = function (object, methodName, afterMethodCall) {
		const proto = Object.getPrototypeOf(object);
		const originalFunc = proto[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		proto[methodName] = function () {
			var result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	};
	tools.wrapMethod = function (object, methodName, beforeMethodCall, afterMethodCall) {
		const proto = Object.getPrototypeOf(object);
		const originalFunc = proto[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		proto[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			const result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	};
	tools.mergeVueConstructorWithJavaComponent = function (extendedVueConstructor, jsTypeConstructor) {
		const vueProto = extendedVueConstructor.prototype;
		let jsTypeProto = jsTypeConstructor.prototype;
		// Copy from the @JsType VueComponent prototype
		for (let protoProp in jsTypeProto) {
			if (jsTypeProto.hasOwnProperty(protoProp) && !vueProto.hasOwnProperty(protoProp)) {
				vueProto[protoProp] = jsTypeProto[protoProp];
			}
		}
		// Also copy from our VueComponent prototype
		jsTypeProto = Object.getPrototypeOf(jsTypeProto);
		for (let protoProp in jsTypeProto) {
			if (jsTypeProto.hasOwnProperty(protoProp) && !vueProto.hasOwnProperty(protoProp)) {
				vueProto[protoProp] = jsTypeProto[protoProp];
			}
		}
	};
})(window);(function (context) {
	if (!context.VueGWT)
		context.VueGWT = {};
	if (!context.VueGWT.observerManager)
		context.VueGWT.observerManager = {};

	const observerManager = context.VueGWT.observerManager;

	/**
	 * This object provides methods to integrate Java in Vue.js world
	 * @author Adrien Baron
	 */
	observerManager.customizeVueObserver = function (ob) {
		const obProto = Object.getPrototypeOf(ob);
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
})(window);