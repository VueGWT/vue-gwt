import {expect} from 'chai'
import {
  createAndMountComponent, destroyComponent, onGwtReady,
  onNextTick
} from '../../vue-gwt-tests-utils'

describe('@Computed', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.computed.ComputedTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work correctly at start', () => {
    expect(component.$el.innerText).to.equal('');
    expect(component.$el.hasAttribute('data-value')).to.be.false;
  });

  it('should change its value when a depending value changes', () => {
    component.data = 'test value';

    return onNextTick(() => {
      expect(component.$el.innerText).to.equal('#test value#');
      expect(component.$el.getAttribute('data-value')).to.equal('#test value#')
    });
  });
});