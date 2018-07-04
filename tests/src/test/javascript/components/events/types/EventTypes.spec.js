import {expect} from 'chai'
import {
  createAndMountComponent,
  onGwtReady,
  vueGwtTests
} from '../../../vue-gwt-tests-utils'

describe('EventTypes.spec', () => {
  let vm;

  beforeEach(() => onGwtReady().then(() => {
    vm = createAndMountComponent(
        "com.axellience.vuegwt.tests.client.components.events.types.EmitTypesParentComponent")
  }));

  it("doesn't box primitive types", () => {
    expect(vm.getMyInt()).to.equal(10);
    expect(vm.getMyBoolean()).to.equal(false);
    expect(vm.getMyDouble()).to.equal(12);
    expect(vm.getMyFloat()).to.equal(12.5);
  });

  it("doesn't unbox boxed types", () => {
    expect(vm.getMyInteger()).to.deep.equal(vm.getTestIntegerValue());
  });

  it("passes objects untouched", () => {
    expect(vm.getMyTodo().text).to.equal("Hello World");
  });
});