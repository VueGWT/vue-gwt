import chai, {expect} from 'chai'
import spies from 'chai-spies'
import {
  createAndMountComponent,
  destroyComponent,
  getElement,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('@Prop', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    chai.use(spies);
    chai.spy.on(console, 'error');

    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.basic.prop.PropParentTestComponent');
  });

  afterEach(() => {
    chai.spy.restore(console);
    destroyComponent(component);
  });

  it('should have correct value at start', () => {
    const optionalPropDomValue = getElement(component,
        '#optional-prop').innerText;
    const requiredPropDomValue = getElement(component,
        '#required-prop').innerText;

    expect(optionalPropDomValue).to.equal('6');
    expect(requiredPropDomValue).to.equal('');
  });

  it('should have correct value when its passed value changes', async () => {
    component.setOptionalPropParent(16);
    component.getRequiredPropParent().setStringProperty('value');

    await nextTick();
    const optionalPropDomValue = getElement(component,
        '#optional-prop').innerText;
    const requiredPropDomValue = getElement(component,
        '#required-prop').innerText;
    expect(optionalPropDomValue).to.equal('16');
    expect(requiredPropDomValue).to.equal('value');
  });

  it('should not be observed if original value wasn\'t', () => {
    expect(component.getNonObservedObject().__ob__).to.be.undefined;
  });

  it('should pass boolean props to true when defined', async () => {
    const booleanPropTrue = getElement(component,
        '#boolean-prop-true').innerText;
    expect(booleanPropTrue).to.equal('true');
  });

  it('should pass boolean props to false when not defined', async () => {
    const booleanPropFalse = getElement(component,
        '#boolean-prop-false').innerText;
    expect(booleanPropFalse).to.equal('false');
  });

  it('should work with boolean object props', async () => {
    const booleanPropObject = getElement(component,
        '#boolean-object-prop-true').innerText;
    expect(booleanPropObject).to.equal('true');
    expect(console.error).to.not.have.been.called();
  });

  it('should initialize boolean object prop to null', async () => {
    const booleanPropObject = getElement(component,
        '#boolean-object-prop-null').innerText;
    expect(booleanPropObject).is.empty;
  });
});