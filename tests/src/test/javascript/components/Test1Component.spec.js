import { expect } from 'chai'
import { vueGwtTests } from '../vue-gwt-tests-utils'

beforeEach(() => vueGwtTests.initForPackage("com.axellience.vuegwt.tests.client.components.test1"));

describe('Test1Component.spec', () => {
	it('renders a simple div', () => {
		const comp = vueGwtTests.createAndMountComponent('Test1Component');
		expect(comp.$el.innerText).to.equal("Hello Vue GWT");
	})
});