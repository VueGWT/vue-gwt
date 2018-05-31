import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady,
  nextTick
} from '../../vue-gwt-tests-utils'

describe('Class binding', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.style.classbinding.ClassBindingTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work with single map entry', () => {
    const classAElement = getElement(component, "#class-a");
    expect(classAElement.getAttribute('class')).to.be.empty;

    component.hasClassA = true;
    return nextTick().then(() => {
      expect(classAElement.getAttribute('class')).to.equal('class-a');
    });
  });

  it('should work with multiple map entries', () => {
    const classABElement = getElement(component, "#class-a-b");
    expect(classABElement.getAttribute('class')).to.be.empty;

    component.hasClassB = true;
    return nextTick()
    .then(() => {
      const classValue = classABElement.getAttribute('class');
      expect(classValue).to.equal('class-b');

      component.hasClassA = true;
      return nextTick();
    })
    .then(() => {
      const classValue = classABElement.getAttribute('class');
      expect(classValue).to.have.string('class-a');
      expect(classValue).to.have.string('class-b');
    });
  });

  it('should work with computed method', () => {
    const computedClassABElement = getElement(component,
        "#computed-class-a-b");
    expect(computedClassABElement.getAttribute('class')).to.be.empty;

    component.hasClassB = true;
    return nextTick()
    .then(() => {
      const classValue = computedClassABElement.getAttribute('class');
      expect(classValue).to.equal('class-b');

      component.hasClassA = true;
      return nextTick();
    })
    .then(() => {
      const classValue = computedClassABElement.getAttribute('class');
      expect(classValue).to.have.string('class-a');
      expect(classValue).to.have.string('class-b');
    });
  });

  it('should work with static class', () => {
    const staticClassElement = getElement(component, "#static-class");
    expect(staticClassElement.getAttribute('class')).to.equal('static-class');

    component.hasClassA = true;
    return nextTick().then(() => {
      const classValue = staticClassElement.getAttribute('class');
      expect(classValue).to.have.string('static-class');
      expect(classValue).to.have.string('class-a');
    });
  });

  it('should work with array syntax', () => {
    const arrayClassElement = getElement(component, "#array-class");
    expect(arrayClassElement.getAttribute('class')).to.equal('class-c');

    component.hasClassA = true;
    return nextTick().then(() => {
      const classValue = arrayClassElement.getAttribute('class');
      expect(classValue).to.have.string('class-c');
      expect(classValue).to.have.string('class-a');
    });
  });
});