/**
 * This object provides methods to integrate Java in Vue.js world
 * @author Adrien Baron
 */
window.vueGwtTools = {
	createVueInstance: function (vueComponentDefinition) {
		return new Vue(vueComponentDefinition);
	},
	createInstanceForVueClass: function (extendedVueClass) {
		return new extendedVueClass();
	},
	getFunctionBody: function(myFunction) {
		// Get content between first { and last }
		var m = myFunction.toString().match(/\{([\s\S]*)\}/m)[1];
		// Strip comments
		return m.replace(/^\s*\/\/.*$/mg,'').trim();
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

			// This is required for GWT type checking to work
			var jciProto = this.$options.vuegwt$javaComponentInstance.__proto__;
			for (var protoProp in jciProto) {
				if (jciProto.hasOwnProperty(protoProp) && !this.hasOwnProperty(protoProp)) {
					this[protoProp] = jciProto[protoProp];
				}
			}
		}
	})
});