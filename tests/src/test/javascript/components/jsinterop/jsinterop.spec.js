import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe("JsInterop", () => {
  let component;

  describe("computed", () => {
    beforeEach(() => onGwtReady().then(() => {
      component = createAndMountComponent(
          'com.axellience.vuegwt.tests.client.components.jsinterop.ComputedOverrideComponent');
    }));

    afterEach(() => {
      destroyComponent(component);
    });

    it('should not override existing properties', () => {
      expect(getElement(component, "#data").innerText).to.equal('DATA');
      expect(getElement(component, "#computed").innerText).to.equal('COMPUTED');
    });
  });
});