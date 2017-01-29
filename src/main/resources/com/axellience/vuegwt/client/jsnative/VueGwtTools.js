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
	}
};

Vue.use(function (Vue) {
	Vue.mixin({
		created: function () {
			if (!this.$options.vuegwt$javaComponentInstance)
				return;

			// This is required for GWT type checking to work
			var jciProto = this.$options.vuegwt$javaComponentInstance.__proto__;
			for (var protoProp in jciProto) {
				if (jciProto.hasOwnProperty(protoProp) && !this[protoProp]) {
					this[protoProp] = jciProto[protoProp];
				}
			}
		}
	})
});