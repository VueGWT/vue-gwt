import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent, nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Ref', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.basic.ref.RefParentTestComponent');
  }));

  afterEach(() => {
    destroyComponent(component);
  });

  it('should work with DOM elements', () => {
    expect(component.$refs.divElement).to.be.not.undefined;
    expect(component.$refs.divElement.innerText).to.equal("Hello World");
  });

  it('should work with DOM elements on v-for', () => {
    expect(component.$refs.divElements).to.be.not.undefined;
    expect(component.$refs.divElements.length).to.equal(2);
    expect(component.$refs.divElements[0].innerText).to.equal("Hello");
    expect(component.$refs.divElements[1].innerText).to.equal("World");
  });

  it('should work with Components', () => {
    const child = component.$refs.child;
    expect(child).to.be.not.undefined;
    expect(child.$el.innerText).to.equal("Hello");

    child.setData("New Data");

    return nextTick().then(() => {
      expect(child.$el.innerText).to.equal("New Data");
    });
  });

  it('should work with DOM elements on v-for', () => {
    const children = component.$refs.children;
    expect(children).to.be.not.undefined;
    expect(children.length).to.equal(2);
    expect(children[0].$el.innerText).to.equal("Hello");
    expect(children[1].$el.innerText).to.equal("Hello");

    children[0].setData("New Data Child 0");
    children[1].setData("New Data Child 1");

    return nextTick().then(() => {
      expect(children[0].$el.innerText).to.equal("New Data Child 0");
      expect(children[1].$el.innerText).to.equal("New Data Child 1");
    });
  });

  describe('@Ref and @RefArray', () => {
    it('should work with DOM elements', () => {
      expect(component.getDivElement()).to.be.not.undefined;
      expect(component.getDivElement().innerText).to.equal("Hello World");
    });

    it('should work with DOM elements on v-for', () => {
      expect(component.getDivElements()).to.be.not.undefined;
      expect(component.getDivElements().length).to.equal(2);
      expect(component.getDivElements()[0].innerText).to.equal("Hello");
      expect(component.getDivElements()[1].innerText).to.equal("World");
    });

    it('should work with Components', () => {
      const child = component.getChild();
      expect(child).to.be.not.undefined;
      expect(child.$el.innerText).to.equal("Hello");

      child.setData("New Data");

      return nextTick().then(() => {
        expect(child.$el.innerText).to.equal("New Data");
      });
    });

    it('should work with DOM elements on v-for', () => {
      const children = component.getChildren();
      expect(children).to.be.not.undefined;
      expect(children.length).to.equal(2);
      expect(children[0].$el.innerText).to.equal("Hello");
      expect(children[1].$el.innerText).to.equal("Hello");

      children[0].setData("New Data Child 0");
      children[1].setData("New Data Child 1");

      return nextTick().then(() => {
        expect(children[0].$el.innerText).to.equal("New Data Child 0");
        expect(children[1].$el.innerText).to.equal("New Data Child 1");
      });
    });
  })
});