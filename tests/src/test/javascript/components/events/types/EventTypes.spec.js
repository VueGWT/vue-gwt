import { expect } from 'chai'
import { vueGwtTests } from '../../../vue-gwt-tests-utils'

beforeEach(() => vueGwtTests.initForPackage("com.axellience.vuegwt.tests.client.components.events.types"));

describe('EventTypes.spec', () => {
	it("doesn't box primitive types", () => {
		const vm = vueGwtTests.createAndMountComponent('EmitTypesParentComponent');
		expect(vm.myInt).to.equal(10);
		expect(vm.myBoolean).to.equal(false);
		expect(vm.myDouble).to.equal(12);
		expect(vm.myFloat).to.equal(12.5);
	});

	it("doesn't unbox boxed types", () => {
		const vm = vueGwtTests.createAndMountComponent('EmitTypesParentComponent');
		expect(vm.getMyInteger()).to.deep.equal(vm.getTestIntegerValue());
	});

	it("passes objects untouched", () => {
		const vm = vueGwtTests.createAndMountComponent('EmitTypesParentComponent');
		expect(vm.myTodo.text).to.equal("Hello World");
	});
});