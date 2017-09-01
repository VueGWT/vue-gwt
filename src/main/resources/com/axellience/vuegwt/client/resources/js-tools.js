(function (context) {
	if (!context.VueGWT)
		context.VueGWT = {};
	if (!context.VueGWT.jsTools)
		context.VueGWT.jsTools = {};

	const jsTools = context.VueGWT.jsTools;
	/**
	 * Source: https://github.com/ltearno/angular2-gwt/
	 */
	jsTools.defineProperty = function (object, name, definition) {
		const def = {};
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
		return context;
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
	jsTools.callOnContext = function (func, args) {
		return func(args);
	};
	jsTools.getDeepValue = function (obj, path) {
		for (let i = 0, pathObj = path.split('.'), len = pathObj.length; i < len; i++) {
			obj = obj[pathObj[i]];
		}
		return obj;
	};
	jsTools.createFunction = function (body) {
		return new Function(body);
	};
	jsTools.getFunctionBody = function (myFunction) {
		// Get content between first { and last }
		const m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
		// Strip comments
		return m.replace(/^\s*\/\/.*$/mg, '').trim();
	};
	jsTools.debugger = function () {
		debugger;
	};
	jsTools.delete = function (object, property) {
		return object[property];
	};
	jsTools.isUndefined = function (value) {
		return value === undefined;
	};
})(window);