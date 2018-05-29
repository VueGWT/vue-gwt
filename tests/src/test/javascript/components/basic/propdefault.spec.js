import {expect} from 'chai'
import {
  createAndMountComponent, destroyComponent, getElement, onGwtReady,
  onNextTick
} from '../../vue-gwt-tests-utils'

describe('@PropDefault', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.propdefault.PropDefaultParentTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should be used when no value are passed', () => {
    const domValue = getElement(component,
        '#child-with-default-value').innerText;
    expect(domValue).to.equal('default value');
  });

  it('should not be used when a value are passed', () => {
    const domValue = getElement(component,
        '#child-with-parent-value').innerText;
    expect(domValue).to.equal('parent value');
  });
});