(function (context) {
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