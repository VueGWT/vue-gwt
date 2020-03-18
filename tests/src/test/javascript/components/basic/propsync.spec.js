import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady,
  triggerEvent
} from '../../vue-gwt-tests-utils'

describe('@Prop sync', () => {
  let component;

  beforeEach(async () => {
        await onGwtReady();
        component = createAndMountComponent(
            'com.axellience.vuegwt.tests.client.components.basic.propsync.PropSyncParentTestComponent');
      }
  );

  afterEach(() => {
    destroyComponent(component);
  });

  it('should have correct value at start', () => {
    const parentPropDomValue = getElement(component, '#parentProp').innerText;
    const propDomValue = getElement(component, '#prop').innerText;

    expect(parentPropDomValue).to.equal('originalValue');
    expect(propDomValue).to.equal('originalValue');
  });

  it('should sync the value from the child up to the parent', async () => {
    const button = getElement(component, '#setParentPropButton');
    triggerEvent(button, "click");

    await nextTick();
    const parentPropDomValue = getElement(component, '#parentProp').innerText;
    const propDomValue = getElement(component, '#prop').innerText;
    expect(parentPropDomValue).to.equal('changedValue');
    expect(propDomValue).to.equal('changedValue');
  });
});