import { expect } from 'chai'
import { mount } from 'vue-test-utils'
import { vueGwtTests } from '../../vue-gwt-tests-utils'

beforeEach(() => vueGwtTests.initForPackage("com.axellience.vuegwt.tests.client.components.test1"));

describe('Test1Component.spec', () => {
	it('renders a simple div', () => {
		const test1 = mount(vueGwtTests.getComponent('Test1Component'));
		expect(test1.find("div").text()).to.equal("Hello Vue GWT");
	})
});