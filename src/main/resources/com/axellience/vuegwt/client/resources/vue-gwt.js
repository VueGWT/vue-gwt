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
		const originalFunc = object[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		object[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			return originalFunc.apply(this, arguments);
		};
	};
	tools.wrapMethodWithAfter = function (object, methodName, afterMethodCall) {
		const originalFunc = object[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		object[methodName] = function () {
			const result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	};
	tools.wrapMethod = function (object, methodName, beforeMethodCall, afterMethodCall) {
		const originalFunc = object[methodName];
		if (originalFunc === null)
			console.error("Attempting to wrap a non existing method", object, methodName);

		object[methodName] = function () {
			beforeMethodCall(object, methodName, arguments);
			const result = originalFunc.apply(this, arguments);
			afterMethodCall(object, methodName, result, arguments);
			return result;
		};
	};
	tools.extendVueConstructorWithJavaPrototype = function (extendedVueConstructor, componentJavaPrototype) {
		const vueProto = extendedVueConstructor.prototype;

		// Copy from the Java Component prototype
		for (let protoProp in componentJavaPrototype) {
			if (!vueProto.hasOwnProperty(protoProp)) {
				vueProto[protoProp] = componentJavaPrototype[protoProp];
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
	 * Wrap the default Vue Observer walk method to observe Java object.
	 * Java world sometimes do things that are not observable by Vue.js. For example java
	 * collections are not reactive.
	 * By wrapping the observer we are able to customize how we observe those object.
	 * @param ob
	 */
	observerManager.customizeVueObserver = function (ob) {
		const obProto = Object.getPrototypeOf(ob);
		const vueWalk = obProto.walk;
		obProto.walk = function (obj) {
			if (observerManager.observeJavaObject(obj)) {
				return;
			}

			vueWalk(obj);
		};

		observerManager.observeArray = obProto.observeArray;
		observerManager.makeReactive = vueWalk;
	};

	/**
	 * Due to GWT optimization, properties on java object defined like this are not observable in Vue.js:
	 * <br>
	 * private String myText = "Default text";
	 * <br>
	 * This is because GWT define the default value on the prototype and don't define it on the object.
	 * Therefore Vue.js don't see those properties when initializing it's observer.
	 * To fix the issue, we manually look for those properties and set them explicitly on the
	 * object.
	 */
	const staticPropertiesCache = {};
	observerManager.makeStaticallyInitializedPropertiesReactive = function (javaObject, className) {
		let cache = staticPropertiesCache[className];
		if (!cache) {
			cache = [];
			const proto = Object.getPrototypeOf(javaObject);
			for (let key in proto) {
				const value = proto[key];
				if (value === null || (typeof value !== "function" && typeof value !== "object"))
					cache.push({key: key, value: value});
			}
		}

		// Set values on the object
		cache.forEach(entry => {
			if (!javaObject.hasOwnProperty(entry.key))
				javaObject[entry.key] = entry.value;
		});
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