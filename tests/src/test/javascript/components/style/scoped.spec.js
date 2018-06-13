import {expect} from 'chai'
import {
  createAndMountComponent, destroyComponent, getElement, onGwtReady,
  onNextTick
} from '../../vue-gwt-tests-utils'

describe('Scoped styling', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.style.scoped.ScopedTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('check non-boiling label has proper styling', () => {
    component.celsius = '22';
    return onNextTick(() => {
      const element = getElement(component, '.relax');
      expect(element).to.exist;
	  const c = window.getComputedStyle(element, null).getPropertyValue('color');
      expect(c).to.equal('rgb(0, 128, 0)'); // green
      const b = window.getComputedStyle(element, null).getPropertyValue('border-radius');
	  expect(b).to.equal('8px'); // 0.5em
    });
  });

  it('check boiling label has proper styling', () => {
    component.celsius = '101';
    return onNextTick(() => {
      const element = getElement(component, '.attention');	  
	  expect(element).to.exist;
	  const c = window.getComputedStyle(element, null).getPropertyValue('color');
      expect(c).to.equal('rgb(255, 0, 0)'); // red
    });
  });
});
