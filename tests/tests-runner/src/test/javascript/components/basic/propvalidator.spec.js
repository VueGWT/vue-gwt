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

  beforeEach(async () => {
    await onGwtReady();
    chai.use(spies);
    chai.spy.on(console, 'error');
    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.basic.propvalidator.PropValidatorParentTestComponent');
  });

  afterEach(() => {
    chai.spy.restore(console);
    destroyComponent(component);
  });

  it('should not fire an error if the value is correct', async () => {
    component.setValidatedPropParent(6);

    await nextTick();
    expect(console.error).to.not.have.been.called();
  });

  it('should fire an error if the value is incorrect in dev mode', async () => {
    if (Vue.config.productionTip === true) {
      component.setValidatedPropParent(106);

      await nextTick();
      expect(console.error).to.have.been.called.once;
    }
  });

  it('should not fire an error if the value is incorrect in production mode',
      async () => {
        if (Vue.config.productionTip === false) {
          component.setValidatedPropParent(106);

          await nextTick();
          expect(console.error).to.not.have.been.called();
        }
      });
});