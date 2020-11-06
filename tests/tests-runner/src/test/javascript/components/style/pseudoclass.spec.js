import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Pseudo class', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.style.pseudoclass.PseudoClassComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('should scope CSS class with pseudo element', async () => {
    const element = getElement(component, '.simple-pseudo-class');
    const content = window.getComputedStyle(element,
        ':before').getPropertyValue('content');
    expect(content).to.equal('"before"');
  });

  it('should scope CSS class with multiple pseudo classes', async () => {
    const element = getElement(component, '#second-li');
    const content = window.getComputedStyle(element).getPropertyValue('color');
    expect(content).to.equal('rgb(255, 0, 0)');
  });
});
