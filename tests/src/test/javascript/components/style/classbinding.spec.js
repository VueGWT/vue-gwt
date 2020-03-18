import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Class binding', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.style.classbinding.ClassBindingTestComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work with single map entry', async () => {
    const classAElement = getElement(component, "#class-a");
    expect(classAElement.getAttribute('class')).to.be.empty;

    component.setHasClassA(true);

    await nextTick();
    expect(classAElement.getAttribute('class')).to.equal('class-a');
  });

  it('should work with multiple map entries', async () => {
    const classABElement = getElement(component, "#class-a-b");
    expect(classABElement.getAttribute('class')).to.be.empty;

    component.setHasClassB(true);

    await nextTick();
    let classValue = classABElement.getAttribute('class');
    expect(classValue).to.equal('class-b');
    component.setHasClassA(true);

    await nextTick();
    classValue = classABElement.getAttribute('class');
    expect(classValue).to.have.string('class-a');
    expect(classValue).to.have.string('class-b');
  });

  it('should work with computed method', async () => {
    const computedClassABElement = getElement(component,
        "#computed-class-a-b");
    expect(computedClassABElement.getAttribute('class')).to.be.empty;

    component.setHasClassB(true);

    await nextTick();
    let classValue = computedClassABElement.getAttribute('class');
    expect(classValue).to.equal('class-b');
    component.setHasClassA(true);

    await nextTick();
    classValue = computedClassABElement.getAttribute('class');
    expect(classValue).to.have.string('class-a');
    expect(classValue).to.have.string('class-b');
  });

  it('should work with static class', async () => {
    const staticClassElement = getElement(component, "#static-class");
    expect(staticClassElement.getAttribute('class')).to.equal('static-class');

    component.setHasClassA(true);

    await nextTick();
    const classValue = staticClassElement.getAttribute('class');
    expect(classValue).to.have.string('static-class');
    expect(classValue).to.have.string('class-a');
  });

  it('should work with array syntax', async () => {
    const arrayClassElement = getElement(component, "#array-class");
    expect(arrayClassElement.getAttribute('class')).to.equal('class-c');

    component.setHasClassA(true);

    await nextTick();
    const classValue = arrayClassElement.getAttribute('class');
    expect(classValue).to.have.string('class-c');
    expect(classValue).to.have.string('class-a');
  });
});