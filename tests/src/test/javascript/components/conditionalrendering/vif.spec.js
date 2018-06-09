import {expect} from 'chai'
import {
  createAndMountComponent, destroyComponent, getElement, onGwtReady,
  onNextTick
} from '../../vue-gwt-tests-utils'

describe('v-if', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.conditionalrendering.vif.VIfTestComponent'
    );
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work without v-else-if or v-else', () => {
    expect(getElement(component, '#if-element')).to.be.null;

    component.ifCondition = true;
    return onNextTick(() => {
      expect(getElement(component, '#if-element')).to.exist;
    });
  });

  it('should work with v-else', () => {
    expect(getElement(component, '#if-else--if-element')).to.be.null;
    expect(getElement(component, '#if-else--else-element')).to.exist;

    component.ifCondition = true;
    return onNextTick(() => {
      expect(getElement(component, '#if-else--if-element')).to.exist;
      expect(getElement(component, '#if-else--else-element')).to.be.null;
    });
  });

  it('should work with v-else-if', () => {
    expect(getElement(component, '#if-else-if--if-element')).to.be.null;
    expect(getElement(component, '#if-else-if--else-if-element')).to.be.null;
    expect(getElement(component, '#if-else-if--else-element')).to.exist;

    component.ifCondition = true;
    component.elseIfCondition = true;
    return onNextTick(() => {
      expect(getElement(component, '#if-else-if--if-element')).to.exist;
      expect(getElement(component, '#if-else-if--else-if-element')).to.be.null;
      expect(getElement(component, '#if-else-if--else-element')).to.be.null;
    }).then(() => {
      component.ifCondition = false;
      return onNextTick(() => {
        expect(getElement(component, '#if-else-if--if-element')).to.be.null;
        expect(getElement(component, '#if-else-if--else-if-element')).to.exist;
        expect(getElement(component, '#if-else-if--else-element')).to.be.null;
      });
    });
  });
});