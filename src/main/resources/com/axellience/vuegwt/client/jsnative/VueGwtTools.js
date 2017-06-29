/**
 * This object provides methods to integrate Java in Vue.JS world
 * @author Adrien Baron
 */
window.vueGwt = {
	createVueInstance: function (vueComponentDefinition) {
		return new Vue(vueComponentDefinition);
	},
	createInstanceForVueClass: function (extendedVueClass) {
		return new extendedVueClass();
	},
	/**
	 * Return a method that will transform a Java Collection
	 * to a JS Array whenever one of it's method is called
	 */
	_getCollectionWatchFunction: function (vueComponentInstance, collectionId) {
		var arrayName = collectionId + "_ARRAY";
		var that = this;
		return function (collection) {
			// Collection is undefined or already watched, just ignore
			if (!collection || collection.$vueIsWatched)
				return;

			// Will be called every call of a method on the Collection
			function onChange() {
				if (collection.$wueWatchIsDirty)
					return;
				collection.$wueWatchIsDirty = true;

				setTimeout(function () {
					vueComponentInstance[arrayName] = window.VueGwtJavaTools.collectionToJsArray(collection);
					collection.$wueWatchIsDirty = false;
				});
			}

			// Bind our onChange function to all the Collection's methods
			for (var methodName in collection) {
				if (!(typeof collection[methodName] === "function"))
					continue;

				that._bindToFunction(collection, methodName, onChange);
			}

			collection.$vueIsWatched = true;

			// Call on change once to init the Array
			onChange();
		}
	},
	_bindToFunction: function (object, methodName, cbk) {
		var originalMethod = object[methodName];
		object[methodName] = function () {
			cbk.call(object);
			return originalMethod.apply(object, arguments);
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

			// Inject styles
			this.$s = this.$options.vuegwt$styles;

			// This is required for GWT type checking to work
			var jciProto = this.$options.vuegwt$javaComponentInstance.__proto__;
			for (var protoProp in jciProto) {
				if (jciProto.hasOwnProperty(protoProp) && !this.hasOwnProperty(protoProp)) {
					this[protoProp] = jciProto[protoProp];
				}
			}
		},
		beforeMount: function () {
			if (!this.$options.vuegwt$collections)
				return;

			// Watch Java collections to convert them to JS Arrays
			for (var i = 0; i < this.$options.vuegwt$collections.length; i++) {
				var collectionId = this.$options.vuegwt$collections[i];

				// Get a function to watch this collection
				var watchFunc = window.vueGwt._getCollectionWatchFunction(this, collectionId);
				// Watch the current collection instance
				watchFunc(this[collectionId]());
				// Whenever the collection instance change, we will watch it again
				this.$watch(this[collectionId], watchFunc);
			}
		}
	})
});