/**
 * Source: https://github.com/ltearno/angular2-gwt/
 */
window.axellience = {
	defineProperty: function (object, name, definition) {
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
	},
	getWindow: function () {
		return window;
	},
	objectHasProperty: function (object, property) {
		return property in object;
	},
	getObjectProperty: function (object, property) {
		return object[property];
	},
	setObjectProperty: function (object, property, value) {
		object[property] = value;
	},
	unsetObjectProperty: function (object, property) {
		delete object[property];
	},
	setObjectFunction: function (object, property, func) {
		object[property] = function () {
			return func.exec.apply(this, arguments);
		};
	},
	getArrayItem: function (array, index) {
		return array[index] || null;
	},
	setArrayItem: function (array, index, value) {
		array[index] = value;
	},
	call: function (func, thisArg, args) {
		return func.call(thisArg, args);
	},
	getDeepValue: function (obj, path) {
		for (var i = 0, pathObj = path.split('.'), len = pathObj.length; i < len; i++) {
			obj = obj[pathObj[i]];
		}
		return obj;
	}
};