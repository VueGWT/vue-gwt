import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Event Types', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        "com.axellience.vuegwt.testsapp.client.components.events.types.EmitTypesParentComponent")
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it("should not box primitive types", () => {
    expect(component.getMyInt()).to.equal(10);
    expect(component.getMyBoolean()).to.equal(false);
    expect(component.getMyDouble()).to.equal(12);
    expect(component.getMyFloat()).to.equal(12.5);
  });

  it("should not box primitive types when using as any", () => {
    expect(component.getMyIntAsAny()).to.equal(10);
    expect(component.getMyBooleanAsAny()).to.equal(false);
    expect(component.getMyDoubleAsAny()).to.equal(12);
    expect(component.getMyFloatAsAny()).to.equal(12.5);
  });

  it("should not unbox boxed types", () => {
    expect(component.getMyInteger()).to.deep.equal(
        component.getTestIntegerValue()
    );
  });

  it("should not unbox boxed types when using any", () => {
    expect(component.getMyIntegerAsAny()).to.deep.equal(
        component.getTestIntegerValue()
    );
  });

  it("should passes objects untouched", () => {
    expect(component.getMyTodo().text).to.equal("Hello World");
  });

  it("should passes objects untouched as any", () => {
    expect(component.getMyTodo().text).to.equal("Hello World");
  });
});