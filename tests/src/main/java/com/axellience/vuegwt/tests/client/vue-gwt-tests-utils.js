class VueGwtTestsUtils {
	constructor() {
		this.onReadyPromise = new Promise((onSuccess) => {
			this.setVueGwtIsReady = onSuccess;
			window.onVueGwtTestsReady = () => this.setVueGwtIsReady();
		});
	}

	initForPackage(rootPackage) {
		this.rootPackage = rootPackage;
		return this.onReadyPromise;
	}

	getComponent(testComponentName) {
		return window.VueGWT.getJsConstructor(`${this.rootPackage}.${testComponentName}`);
	}
}

export const vueGwtTests = new VueGwtTestsUtils();