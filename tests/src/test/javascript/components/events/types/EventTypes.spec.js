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
    expect(vm.myInt).to.equal(10);
    expect(vm.myBoolean).to.equal(false);
    expect(vm.myDouble).to.equal(12);
    expect(vm.myFloat).to.equal(12.5);
  });

  it("doesn't unbox boxed types", () => {
    expect(vm.getMyInteger()).to.deep.equal(vm.getTestIntegerValue());
  });

  it("passes objects untouched", () => {
    expect(vm.myTodo.text).to.equal("Hello World");
  });
});