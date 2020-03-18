import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Scoped styling', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.style.scoped.ScopedTestComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('check non-boiling label has proper styling', async () => {
    component.celsius = '22';

    await nextTick();
    const element = getElement(component, '.relax');
    expect(element).to.exist;
    const c = window.getComputedStyle(element, null).getPropertyValue(
        'color');
    expect(c).to.equal('rgb(0, 128, 0)');
    const b = window.getComputedStyle(element, null).getPropertyValue(
        'border-radius');
    expect(b).to.equal('8px');
  });

  it('check boiling label has proper styling', async () => {
    component.celsius = '101';

    await nextTick();
    const element = getElement(component, '.attention');
    expect(element).to.exist;
    const c = window.getComputedStyle(element, null).getPropertyValue(
        'color');
    expect(c).to.equal('rgb(255, 0, 0)');
  });
});
