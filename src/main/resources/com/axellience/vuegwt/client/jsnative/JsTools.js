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

	propertyInObject: function (property, object) {
		return property in object;
	},

	getObjectProperty: function (object, property) {
		var value = object[property];
		return value !== undefined ? value : "";
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

	getObjectIterator: function (object) {
		if (object[Symbol.iterator])
			return object[Symbol.iterator]();
		return null;
	},

	debugPackage: function (pack, packageName) {
		packageName = packageName || '';
		for (var childName in pack) {
			var child = pack[childName];
			if (typeof child === 'object')
				lteconsulting.debugPackage(child, packageName + '.' + childName);
			else if (typeof child === 'function')
				console.log('jsName:' + child.name + ' javaName:' + packageName + '.' + childName);
		}
	},

	convertObject: (function (prototypes) {
		return function (prototypeName, source) {
			function internal(prototypeName, source) {
				var prototype = prototypes[prototypeName];
				if (!prototype) {
					prototype = window;
					var parts = prototypeName.split('.');
					for (var i in parts)
						prototype = prototype[parts[i]];
					prototypes[prototypeName] = prototype;
				}

				var result = Object.create(prototype.prototype);
				if (source) {
					for (var prop in source)
						result[prop] = source[prop];
				}
				return result;
			}

			return internal(prototypeName, source);
		}
	})({})
};