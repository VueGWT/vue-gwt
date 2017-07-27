if (!window.VueGWT)
	window.VueGWT = {};
if (!window.VueGWT.observerManager)
	window.VueGWT.observerManager = {};

const observerManager = window.VueGWT.observerManager;

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