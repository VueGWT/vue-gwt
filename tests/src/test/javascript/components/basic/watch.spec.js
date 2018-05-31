import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  onGwtReady,
  nextTick
} from '../../vue-gwt-tests-utils'

describe('@Watch', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.watch.WatchTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should be called with correct parameters when the data change', () => {
    component.changeWatchedData('value');

    return nextTick()
    .then(() => {
      expect(component.oldValue).to.be.null;
      expect(component.newValue).to.be.not.null;
      expect(component.newValue.getStringProperty()).to.equal('value');

      component.changeWatchedData(null);
      return nextTick();
    })
    .then(() => {
      expect(component.oldValue).to.be.not.null;
      expect(component.oldValue.getStringProperty()).to.equal('value');
      expect(component.newValue).to.be.null;
    })
  });
});