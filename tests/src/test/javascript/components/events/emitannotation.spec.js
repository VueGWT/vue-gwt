import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady,
  triggerEvent
} from '../../vue-gwt-tests-utils'

describe('Emit Annotation', () => {
  let component, child;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        "com.axellience.vuegwt.tests.client.components.events.emitannotation.EmitAnnotationParent");
    child = component.$refs.child;
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it("should fire simple events", () => {
    const button = getElement(child, "#myEvent");
    triggerEvent(button, "click");
    expect(component.isMyEventReceived()).to.be.true;
  });

  it("should fire events with value", () => {
    const button = getElement(child, "#myEventWithValue");
    triggerEvent(button, "click");
    expect(component.getMyEventWithValue()).to.equal(10);
  });

  it("should fire events with value as parameter not the return value", () => {
    const button = getElement(child, "#myEventWithValueAndReturnValue");
    triggerEvent(button, "click");
    expect(component.getMyEventWithValueAndReturnValue()).to.equal(15);
  });

  it("should fire events with a custom name", () => {
    const button = getElement(child, "#myEventWithValueAndCustomName");
    triggerEvent(button, "click");
    expect(component.getCustomEvent()).to.equal(20);
  });

  it("should be able to call to @Emit methods from Java", () => {
    child.callEmitMethodFromJava(25);
    expect(component.getMyEventWithValue()).to.equal(25);
  });

  it("should work without @JsMethod annotation", () => {
    child.callNoJsMethodAnnotation(30);
    expect(component.getNoJsMethodAnnotation()).to.equal(30);
  });
});