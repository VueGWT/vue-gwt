import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Event Types', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        "com.axellience.vuegwt.tests.client.components.events.types.EmitTypesParentComponent")
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it("should not box primitive types", () => {
    expect(component.getMyInt()).to.equal(10);
    expect(component.getMyBoolean()).to.equal(false);
    expect(component.getMyDouble()).to.equal(12);
    expect(component.getMyFloat()).to.equal(12.5);
  });

  it("should not unbox boxed types", () => {
    expect(component.getMyInteger()).to.deep.equal(
        component.getTestIntegerValue());
  });

  it("should passes objects untouched", () => {
    expect(component.getMyTodo().text).to.equal("Hello World");
  });
});