if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.jsTools)
	window.VueGWT.jsTools = {};

/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
window.VueGWT.jsTools.defineProperty = function (object, name, definition) {
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
window.VueGWT.jsTools.getWindow = function () {
	return window;
};
window.VueGWT.jsTools.objectHasProperty = function (object, property) {
	return property in object;
};
window.VueGWT.jsTools.getObjectProperty = function (object, property) {
	return object[property];
};
window.VueGWT.jsTools.setObjectProperty = function (object, property, value) {
	object[property] = value;
};
window.VueGWT.jsTools.unsetObjectProperty = function (object, property) {
	delete object[property];
};
window.VueGWT.jsTools.setObjectFunction = function (object, property, func) {
	object[property] = function () {
		return func.exec.apply(this, arguments);
	};
};
window.VueGWT.jsTools.getArrayItem = function (array, index) {
	return array[index] || null;
};
window.VueGWT.jsTools.setArrayItem = function (array, index, value) {
	array[index] = value;
};
window.VueGWT.jsTools.call = function (func, thisArg, args) {
	return func.call(thisArg, args);
};
window.VueGWT.jsTools.getDeepValue = function (obj, path) {
	for (var i = 0, pathObj = path.split('.'), len = pathObj.length; i < len; i++) {
		obj = obj[pathObj[i]];
	}
	return obj;
};