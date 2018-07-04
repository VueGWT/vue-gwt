import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('@Prop', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.prop.PropParentTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should have correct value at start', () => {
    const optionalPropDomValue = getElement(component,
        '#optional-prop').innerText;
    const requiredPropDomValue = getElement(component,
        '#required-prop').innerText;

    expect(optionalPropDomValue).to.equal('6');
    expect(requiredPropDomValue).to.equal('');
  });

  it('should have correct value when its passed value changes', () => {
    component.setOptionalPropParent(16);
    component.getRequiredPropParent().setStringProperty('value');

    return nextTick().then(() => {
      const optionalPropDomValue = getElement(component,
          '#optional-prop').innerText;
      const requiredPropDomValue = getElement(component,
          '#required-prop').innerText;

      expect(optionalPropDomValue).to.equal('16');
      expect(requiredPropDomValue).to.equal('value');
    });
  });

  it('should not be observed if original value wasn\'t', () => {
    expect(component.getNonObservedObject().__ob__).to.be.undefined;
  });
});