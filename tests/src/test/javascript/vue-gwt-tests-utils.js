class VueGwtTestsUtils {
	constructor() {
		this.onReadyPromise = new Promise((onSuccess) => {
			window.onVueGwtTestsReady = () => onSuccess();
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