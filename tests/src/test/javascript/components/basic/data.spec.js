import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Data', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.data.DataTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  describe('byteData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#byte-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setByteData(127);
      return nextTick().then(() => {
        const domValue = getElement(component, '#byte-data').innerText;
        expect(domValue).to.equal('127');
      })
    });
  });

  describe('shortData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#short-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setShortData(6);
      return nextTick().then(() => {
        const domValue = getElement(component, '#short-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('intData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#int-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setIntData(6);
      return nextTick().then(() => {
        const domValue = getElement(component, '#int-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('longData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#long-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setLongData(6);
      return nextTick().then(() => {
        const domValue = getElement(component, '#long-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('floatData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#float-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setFloatData(6.6);
      return nextTick().then(() => {
        const domValue = getElement(component, '#float-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('doubleData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#double-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setDoubleData(6.6);
      return nextTick().then(() => {
        const domValue = getElement(component, '#double-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('booleanData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#boolean-data').innerText;
      expect(domValue).to.equal('false');
    });

    it('should update its DOM element when it changes', () => {
      component.setBooleanData(true);
      return nextTick().then(() => {
        const domValue = getElement(component, '#boolean-data').innerText;
        expect(domValue).to.equal('true');
      });
    });
  });

  describe('charData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#char-data').innerText;
      expect(domValue).to.equal('a');
    });

    it('should update its DOM element when it changes', () => {
      component.assignVToCharData();
      return nextTick().then(() => {
        const domValue = getElement(component, '#char-data').innerText;
        expect(domValue).to.equal('v');
      });
    });
  });

  describe('charArrayData', () => {
    it('should be displayed as string in the DOM', () => {
      const domValue = getElement(component, '#char-array-data').innerText;
      expect(domValue).to.equal('abc');
    });
  });

  describe('byteObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#byte-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setByteObjectData(127);

      return nextTick().then(() => {
        const domValue = getElement(component, '#byte-object-data').innerText;
        expect(domValue).to.equal('127');
      });
    });
  });

  describe('shortObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#short-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setShortObjectData(6);

      return nextTick().then(() => {
        const domValue = getElement(component, '#short-object-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('integerData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#integer-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setIntegerData(6);

      return nextTick().then(() => {
        const domValue = getElement(component, '#integer-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('longObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#long-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setLongObjectData(6);

      return nextTick().then(() => {
        const domValue = getElement(component, '#long-object-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('floatObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#float-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setFloatObjectData(6.6);

      return nextTick().then(() => {
        const domValue = getElement(component, '#float-object-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('doubleObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#double-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      component.setDoubleObjectData(6.6);

      return nextTick().then(() => {
        const domValue = getElement(component, '#double-object-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('characterData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = getElement(component, '#character-data').innerText;
      expect(domValue).to.equal('a');
    });

    it('should update its DOM element when it changes', () => {
      component.setCharacterData('v');

      return nextTick().then(() => {
        const domValue = getElement(component, '#character-data').innerText;
        expect(domValue).to.equal('v');
      });
    });
  });

  describe('stringData', () => {
    it('should be displayed as empty string when its value is null', () => {
      const domValue = getElement(component, '#string-data').innerText;
      expect(domValue).to.equal('');
    });

    it('should update its DOM element when it changes', () => {
      component.setStringData('a value');

      return nextTick().then(() => {
        const domValue = getElement(component, '#string-data').innerText;
        expect(domValue).to.equal('a value');
      });
    });
  });

  describe('simpleObjectData', () => {
    it('should be displayed as empty string when its value is null', () => {
      const domValue = getElement(component, '#simple-object-data').innerText;
      expect(domValue).to.equal('');
    });

    it('should be displayed as empty string when its property is null', () => {
      component.initSimpleObject();

      return nextTick().then(() => {
        const domValue = getElement(component, '#simple-object-data').innerText;
        expect(domValue).to.equal('');
      });
    });

    it('should be displayed the value when it has a value', () => {
      component.initSimpleObject();
      component.getSimpleObjectData().setStringProperty('a value');

      return nextTick().then(() => {
        const domValue = getElement(component, '#simple-object-data').innerText;
        expect(domValue).to.equal('a value');
      });
    });
  });

  describe('attributeValueData', () => {
    it('should not be displayed in the DOM if its value is null', () => {
      const element = getElement(component, '#data-attribute-element');
      expect(element.hasAttribute('data-value')).to.be.false;
    });

    it('should be displayed in the DOM if its value is not null', () => {
      component.setAttributeValueData('a value');

      return nextTick().then(() => {
        const element = getElement(component, '#data-attribute-element');
        expect(element.getAttribute('data-value')).to.equal('a value');
      });
    });
  });

  describe('dataWithWhiteSpaces', () => {
    it('should ignore white spaces', () => {
      component.getDataWithLineBreaks().setStringProperty('a value');

      return nextTick().then(() => {
        const domValue = getElement(component,
            '#data-with-line-breaks').innerText;
        expect(domValue).to.equals('a value');
      });
    });
  });

});