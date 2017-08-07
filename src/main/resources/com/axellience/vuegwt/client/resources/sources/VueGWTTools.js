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
	tools.extendVueConstructorWithJavaComponent = function (extendedVueConstructor, componentWithTemplate) {
		const vueProto = extendedVueConstructor.prototype;
		const componentWithTemplateProto = Object.getPrototypeOf(componentWithTemplate);

		// Copy from the ComponentWithTemplate prototype
		for (let protoProp in componentWithTemplateProto) {
			if (!vueProto.hasOwnProperty(protoProp)) {
				vueProto[protoProp] = componentWithTemplateProto[protoProp];
			}
		}
	};
})(window);