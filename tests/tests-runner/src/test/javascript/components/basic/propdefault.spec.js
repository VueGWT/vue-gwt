import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('@PropDefault', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.basic.propdefault.PropDefaultParentTestComponent');
  });

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