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
}

export const vueGwtTests = new VueGwtTestsUtils();