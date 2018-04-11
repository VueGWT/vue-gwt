import {expect} from 'chai'
import {
  createAndMountComponent, destroyComponent, onGwtReady,
  onNextTick
} from '../../vue-gwt-tests-utils'

describe('Data.spec', () => {
  let vm;

  beforeEach(() => onGwtReady().then(() => {
    vm = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.data.DataTestComponent');
  }));

  afterEach(() => {
    destroyComponent(vm);
  });

  describe('byteData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#byte-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.byteData = 127;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#byte-data').innerText;
        expect(domValue).to.equal('127');
      })
    });
  });

  describe('shortData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#short-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.shortData = 6;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#short-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('intData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#int-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.intData = 6;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#int-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('longData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#long-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.longData = 6;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#long-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('floatData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#float-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.floatData = 6.6;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#float-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('doubleData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#double-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.doubleData = 6.6;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#double-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('booleanData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#boolean-data').innerText;
      expect(domValue).to.equal('false');
    });

    it('should update its DOM element when it changes', () => {
      vm.booleanData = true;
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#boolean-data').innerText;
        expect(domValue).to.equal('true');
      });
    });
  });

  describe('charData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#char-data').innerText;
      expect(domValue).to.equal('a');
    });

    it('should update its DOM element when it changes', () => {
      vm.charData = 'v';
      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#char-data').innerText;
        expect(domValue).to.equal('v');
      });
    });
  });

  describe('charArrayData', () => {
    it('should be displayed as string in the DOM', () => {
      const domValue = vm.$el.querySelector('#char-array-data').innerText;
      expect(domValue).to.equal('abc');
    });
  });

  describe('byteObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#byte-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.byteObjectData = 127;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#byte-object-data').innerText;
        expect(domValue).to.equal('127');
      });
    });
  });

  describe('shortObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#short-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.shortObjectData = 6;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#short-object-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('integerData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#integer-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.integerData = 6;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#integer-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('longObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#long-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.longObjectData = 6;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#long-object-data').innerText;
        expect(domValue).to.equal('6');
      });
    });
  });

  describe('floatObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#float-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.floatObjectData = 6.6;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#float-object-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('doubleObjectData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#double-object-data').innerText;
      expect(domValue).to.equal('0');
    });

    it('should update its DOM element when it changes', () => {
      vm.doubleObjectData = 6.6;

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#double-object-data').innerText;
        expect(domValue).to.equal('6.6');
      });
    });
  });

  describe('characterData', () => {
    it('should have its default value in the DOM at start', () => {
      const domValue = vm.$el.querySelector('#character-data').innerText;
      expect(domValue).to.equal('a');
    });

    it('should update its DOM element when it changes', () => {
      vm.characterData = 'v';

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#character-data').innerText;
        expect(domValue).to.equal('v');
      });
    });
  });

  describe('stringData', () => {
    it('should be displayed as empty string when its value is null', () => {
      const domValue = vm.$el.querySelector('#string-data').innerText;
      expect(domValue).to.equal('');
    });

    it('should update its DOM element when it changes', () => {
      vm.stringData = 'a value';

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#string-data').innerText;
        expect(domValue).to.equal('a value');
      });
    });
  });

  describe('simpleObjectData', () => {
    it('should be displayed as empty string when its value is null', () => {
      const domValue = vm.$el.querySelector('#simple-object-data').innerText;
      expect(domValue).to.equal('');
    });

    it('should be displayed as empty string when its property is null', () => {
      vm.initSimpleObject();

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#simple-object-data').innerText;
        expect(domValue).to.equal('');
      });
    });

    it('should be displayed as empty string when its property is null', () => {
      vm.initSimpleObject();
      vm.simpleObjectData.setStringProperty("a value");

      return onNextTick(() => {
        const domValue = vm.$el.querySelector('#simple-object-data').innerText;
        expect(domValue).to.equal('a value');
      });
    });
  });

  describe('attributeValueData', () => {
    it('should not be displayed in the DOM if its value is null', () => {
      const element = vm.$el.querySelector('#data-attribute-element');
      expect(element.hasAttribute('data-value')).to.be.false;
    });

    it('should be displayed in the DOM if its value is not null', () => {
      vm.attributeValueData = 'a value';

      return onNextTick(() => {
        const element = vm.$el.querySelector('#data-attribute-element');
        expect(element.getAttribute('data-value')).to.equal('a value');
      });
    });
  });

});