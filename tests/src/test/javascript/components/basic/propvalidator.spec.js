import chai, {expect} from 'chai'
import spies from 'chai-spies'

import {
  createAndMountComponent,
  destroyComponent,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('@PropValidator', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    chai.use(spies);
    chai.spy.on(console, 'error');

    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.propvalidator.PropValidatorParentTestComponent');
  }));

  afterEach(() => {
    chai.spy.restore(console);
    destroyComponent(component);
  });

  it('should not fire an error if the value is correct', () => {
    component.validatedPropParent = 6;
    return nextTick().then(() => {
      expect(console.error).to.not.have.been.called();
    });
  });

  it('should fire an error if the value is incorrect in dev mode', () => {
    if (Vue.config.productionTip === true) {
      component.validatedPropParent = 106;
      return nextTick().then(() => {
        expect(console.error).to.have.been.called.once;
      });
    }
  });

  it('should not fire an error if the value is incorrect in production mode',
      () => {
        if (Vue.config.productionTip === false) {
          component.validatedPropParent = 106;
          return nextTick().then(() => {
            expect(console.error).to.not.have.been.called();
          });
        }
      });
});