import {expect} from "chai";

class VueGwtTestsUtils {
	constructor() {
		this.onReadyPromise = new Promise((onSuccess) => {
			if (!window.onVueGwtTestsReady)
				window.onVueGwtTestsReady = [];
			window.onVueGwtTestsReady.push(onSuccess);
		});
	}

	initForPackage(rootPackage) {
		this.rootPackage = rootPackage;
		return this.onReadyPromise;
	}

	getComponentConstructor(testComponentName) {
		return window.VueGWT.getJsConstructor(`${this.rootPackage}.${testComponentName}`);
	}

	createAndMountComponent(testComponentName) {
		const div = document.createElement("div");
		document.body.appendChild(div);
		const ComponentConstructor = this.getComponentConstructor(testComponentName);
		return new ComponentConstructor({el: div});
	}

	clearComponent(vm) {
		vm.$destroy();
		document.body.removeChild(vm.$el);
	}

	onNextTick(testFunction) {
		return new Promise((resolve, reject) => {
			Vue.nextTick(() => {
				try {
					testFunction();
					resolve();
				} catch (e) {
					reject(e);
				}
			});
		});
	}
}

export const vueGwtTests = new VueGwtTestsUtils();