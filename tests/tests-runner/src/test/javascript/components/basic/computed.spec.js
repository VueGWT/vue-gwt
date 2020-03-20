import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('@Computed', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.basic.computed.ComputedTestComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work correctly at start', () => {
    const computedPropertyEl = component.$el.firstElementChild;
    expect(computedPropertyEl.innerText).to.equal('');
    expect(computedPropertyEl.hasAttribute('data-value')).to.be.false;
  });

  it('should change its value when a depending value changes', async () => {
    const computedPropertyEl = component.$el.firstElementChild;
    component.setData('test value');

    await nextTick();

    expect(computedPropertyEl.innerText).to.equal('#test value#');
    expect(computedPropertyEl.getAttribute('data-value')).to.equal(
        '#test value#');
  });

  it('should work correctly at start for computed that are not getters', () => {
    const computedPropertyNoGetEl = component.$el.firstElementChild.nextElementSibling;
    expect(computedPropertyNoGetEl.innerText).to.equal('');
    expect(computedPropertyNoGetEl.hasAttribute('data-value')).to.be.false;
  });

  it('should change its value when a depending value changes for computed that are not getters',
      async () => {
        const computedPropertyNoGetEl = component.$el.firstElementChild.nextElementSibling;
        component.setData('test value');

        await nextTick();

        expect(computedPropertyNoGetEl.innerText).to.equal('!test value!');
        expect(computedPropertyNoGetEl.getAttribute('data-value')).to.equal(
            '!test value!');
      });
});