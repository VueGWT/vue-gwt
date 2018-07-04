import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('v-show', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.conditionalrendering.vshow.VShowTestComponent'
    );
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should hide its element if its condition is false', () => {
    const showElement = getElement(component, '#show-element');
    expect(showElement.style.display).to.equals('none');
  });

  it('should show its element if its condition is true', () => {
    const showElement = getElement(component, '#show-element');
    component.setShowCondition(true);
    return nextTick().then(() => {
      expect(showElement.style.display).to.be.empty;
    });
  });
});