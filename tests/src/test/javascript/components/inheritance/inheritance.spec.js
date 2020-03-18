import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Component inheritance', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.inheritance.ChildComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('should init data correctly', () => {
    expect(getElement(component, "#childData").innerText).to.equal(
        'CHILD_INITIAL_DATA');
    expect(getElement(component, "#parentData").innerText).to.equal(
        'PARENT_INITIAL_DATA');
  });

  it('should react to child data field change', async () => {
    expect(getElement(component, "#childData").innerText).to.equal(
        'CHILD_INITIAL_DATA');

    component.setChildData("NEW_CHILD_DATA");

    await nextTick();
    expect(getElement(component, "#childData").innerText).to.equal(
        'NEW_CHILD_DATA');
  });

  it('should react to parent data field change', async () => {
    expect(getElement(component, "#parentData").innerText).to.equal(
        'PARENT_INITIAL_DATA');

    component.setParentData("NEW_PARENT_DATA");

    await nextTick();
    expect(getElement(component, "#parentData").innerText).to.equal(
        'NEW_PARENT_DATA');
  });

  it('should make Computed work correctly', () => {
    expect(getElement(component, "#childComputed").innerText).to.equal(
        'HELLO_FROM_CHILD');
    expect(getElement(component, "#parentComputed").innerText).to.equal(
        'HELLO_FROM_PARENT');
  });
});