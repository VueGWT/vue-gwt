import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Static imports', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.imports.StaticImportsComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should import static properties correctly', () => {
    const testStringDiv = getElement(component, "#testString");
    expect(testStringDiv.innerText).to.equal('HELLO_WORLD_FROM_COMPONENT');
  });

  it('should import static methods correctly', () => {
    const testMethodDiv = getElement(component, "#testMethod");
    expect(testMethodDiv.innerText).to.equal('HELLO_WORLD');
  });
});