import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Global Registration', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.globalregistration.UsingGloballyRegisteredComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  it('should mount components that are globally registered', () => {
    expect(component.$el.querySelector("#globallyRegistered").innerText).to.equal('Globally Registered');
  });

  it('should mount components that are globally registered with a "name" attribute on the @Component annotation', () => {
    expect(component.$el.querySelector("#globallyRegisteredWithName").innerText).to.equal('Globally Registered With Name');
  });

  it('should mount components that are globally registered with a custom name on registration', () => {
    expect(component.$el.querySelector("#globallyRegisteredWithNameOnRegistration").innerText).to.equal('Globally Registered With Name On Registration');
  });
});