import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Slot Scope', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.slots.SlotScopeParentComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  describe('passing data', () => {
    it('should pass int variables', () => {
      const element = getElement(component, "#myInt");
      expect(element.innerText).to.be.equal("15");
    });

    it('should work with props binding in camelCase', () => {
      const element = getElement(component, "#myIntCamelCase");
      expect(element.innerText).to.be.equal("15");
    });

    it('should pass int literal values', () => {
      const element = getElement(component, "#myIntLiteral");
      expect(element.innerText).to.be.equal("20");
    });

    it('should pass Integer variables', () => {
      const element = getElement(component, "#myInteger");
      expect(element.innerText).to.be.equal("15");
    });

    it('should pass String variables', () => {
      const element = getElement(component, "#myString");
      expect(element.innerText).to.be.equal("MY_STRING_HELLO");
    });

    it('should pass String literal values', () => {
      const element = getElement(component, "#myStringLiteral");
      expect(element.innerText).to.be.equal("MY_STRING_LITERAL_HELLO");
    });

    it('should pass object values', () => {
      const element = getElement(component, "#mySimpleObject");
      expect(element.innerText).to.be.equal("MY_VALUE_HELLO");
    });

    it('should update when value updates', () => {
      const element = getElement(component, "#mySimpleObject");

      component.$refs["child"].changeSimpleObjectValue("NEW_VALUE");
      return nextTick()
      .then(() => expect(element.innerText).to.be.equal("NEW_VALUE_HELLO"));
    });
  });

  describe('named slots', () => {
    it('should work on named slots', () => {
      const element = getElement(component, "#nameSlotMyString");
      expect(element.innerText).to.be.equal("MY_STRING_HELLO");
    });
  });

  describe('destructuring Slot Scope Variable', () => {
    it('should destructure int variables', () => {
      const element = getElement(component, "#myIntD");
      expect(element.innerText).to.be.equal("15");
    });

    it('should destructure props binding in camelCase', () => {
      const element = getElement(component, "#myIntCamelCaseD");
      expect(element.innerText).to.be.equal("15");
    });

    it('should destructure byte variables', () => {
      const element = getElement(component, "#myByteD");
      expect(element.innerText).to.be.equal("1");
    });

    it('should destructure short variables', () => {
      const element = getElement(component, "#myShortD");
      expect(element.innerText).to.be.equal("1");
    });

    it('should destructure long variables', () => {
      const element = getElement(component, "#myLongD");
      expect(element.innerText).to.be.equal("1");
    });

    it('should destructure float variables', () => {
      const element = getElement(component, "#myFloatD");
      expect(element.innerText).to.be.equal("0.5");
    });

    it('should destructure double variables', () => {
      const element = getElement(component, "#myDoubleD");
      expect(element.innerText).to.be.equal("0.5");
    });

    it('should destructure boolean variables', () => {
      const element = getElement(component, "#myBooleanD");
      expect(element.innerText).to.be.equal("false");
    });

    it('should destructure char variables', () => {
      const element = getElement(component, "#myCharD");
      expect(element.innerText).to.be.equal("a");
    });

    it('should destructure int literal values', () => {
      const element = getElement(component, "#myIntLiteralD");
      expect(element.innerText).to.be.equal("20");
    });

    it('should destructure Integer variables', () => {
      const element = getElement(component, "#myIntegerD");
      expect(element.innerText).to.be.equal("15");
    });

    it('should destructure String variables', () => {
      const element = getElement(component, "#myStringD");
      expect(element.innerText).to.be.equal("MY_STRING_HELLO");
    });

    it('should destructure String literal values', () => {
      const element = getElement(component, "#myStringLiteralD");
      expect(element.innerText).to.be.equal("MY_STRING_LITERAL_HELLO");
    });

    it('should destructure object values', () => {
      const element = getElement(component, "#mySimpleObjectD");
      expect(element.innerText).to.be.equal("MY_VALUE_HELLO");
    });

    it('should update when value updates', () => {
      const element = getElement(component, "#mySimpleObjectD");

      component.$refs["child"].changeSimpleObjectValue("NEW_VALUE");
      return nextTick()
      .then(() => expect(element.innerText).to.be.equal("NEW_VALUE_HELLO"));
    });
  });

  describe('destructuring Slot Scope Variable with spaces', () => {
    it('should destructure int variables', () => {
      const element = getElement(component, "#myIntD");
      expect(element.innerText).to.be.equal("15");
    });

    it('should destructure Integer variables', () => {
      const element = getElement(component, "#myIntegerD");
      expect(element.innerText).to.be.equal("15");
    });
  });
});