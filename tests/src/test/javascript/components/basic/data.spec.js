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
      component.byteData = 127;
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
      component.shortData = 6;
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
      component.intData = 6;
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
      component.longData = 6;
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
      component.floatData = 6.6;
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
      component.doubleData = 6.6;
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
      component.booleanData = true;
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
      component.byteObjectData = 127;

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
      component.shortObjectData = 6;

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
      component.integerData = 6;

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
      component.longObjectData = 6;

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
      component.floatObjectData = 6.6;

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
      component.doubleObjectData = 6.6;

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
      component.characterData = 'v';

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
      component.stringData = 'a value';

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

    it('should be displayed as empty string when its property is null', () => {
      component.initSimpleObject();
      component.simpleObjectData.setStringProperty('a value');

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
      component.attributeValueData = 'a value';

      return nextTick().then(() => {
        const element = getElement(component, '#data-attribute-element');
        expect(element.getAttribute('data-value')).to.equal('a value');
      });
    });
  });

  describe('dataWithWhiteSpaces', () => {
    it('should ignore white spaces', () => {
      component.dataWithLineBreaks.setStringProperty('a value');

      return nextTick().then(() => {
        const domValue = getElement(component,
            '#data-with-line-breaks').innerText;
        expect(domValue).to.equals('a value');
      });
    });
  });

});