import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Java Collections Observation', () => {
  let component;

  beforeEach(async () => {
    await onGwtReady();
    component = createAndMountComponent(
        'com.axellience.vuegwt.testsapp.client.components.collections.CollectionObservationComponent');
  });

  afterEach(() => {
    destroyComponent(component);
  });

  describe("@Data fields", () => {
    describe("ArrayList", () => {
      let collectionDiv;

      beforeEach(() => {
        collectionDiv = component.$el.querySelector("#arrayList");
      });

      it('should start empty', () => {
        expect(collectionDiv.childNodes.length).to.equal(0);
      });

      it('should should update when adding an item', async () => {
        component.addToArrayList("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(1);
        expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
      });

      it('should should update when removing an item', async () => {
        component.addToArrayList("Hello");

        await nextTick();
        component.removeFromArrayList("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(0);
      });
    });

    describe("LinkedList", () => {
      let collectionDiv;

      beforeEach(() => {
        collectionDiv = component.$el.querySelector("#linkedList");
      });

      it('should start empty', () => {
        expect(collectionDiv.childNodes.length).to.equal(0);
      });

      it('should should update when adding an item', async () => {
        component.addToLinkedList("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(1);
        expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
      });

      it('should should update when removing an item', async () => {
        component.addToLinkedList("Hello");

        await nextTick();
        component.removeFromLinkedList("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(0);
      });
    });

    describe("HashSet", () => {
      let collectionDiv;

      beforeEach(() => {
        collectionDiv = component.$el.querySelector("#hashSet");
      });

      it('should start empty', () => {
        expect(collectionDiv.childNodes.length).to.equal(0);
      });

      it('should should update when adding an item', async () => {
        component.addToHashSet("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(1);
        expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
      });

      it('should should update when removing an item', async () => {
        component.addToHashSet("Hello");

        await nextTick();
        component.removeFromHashSet("Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(0);
      });
    });

    describe("HashMap", () => {
      let collectionDiv;

      beforeEach(() => {
        collectionDiv = component.$el.querySelector("#hashMap");
      });

      it('should start empty', () => {
        expect(collectionDiv.childNodes.length).to.equal(0);
      });

      it('should should update when adding an item', async () => {
        component.addToHashMap("Key", "Hello");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(1);
        expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
      });

      it('should should update when removing an item', async () => {
        component.addToHashMap("Key", "Hello");

        await nextTick();
        component.removeFromHashMap("Key");

        await nextTick();
        expect(collectionDiv.childNodes.length).to.equal(0);
      });
    });
  });

  describe("@Prop fields", () => {
    describe("When changing from Parent", () => {
      describe("ArrayList", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#arrayListProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          component.addToArrayList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          component.addToArrayList("Hello");

          await nextTick();
          component.removeFromArrayList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("LinkedList", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#linkedListProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          component.addToLinkedList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          component.addToLinkedList("Hello");

          await nextTick();
          component.removeFromLinkedList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("HashSet", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#hashSetProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          component.addToHashSet("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          component.addToHashSet("Hello");

          await nextTick();
          component.removeFromHashSet("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("HashMap", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#hashMap");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          component.addToHashMap("Key", "Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          component.addToHashMap("Key", "Hello");

          await nextTick();
          component.removeFromHashMap("Key");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });
    });

    describe("When changing from Child", () => {
      let child;

      beforeEach(async () => {
        await onGwtReady();
        child = component.$refs.child;
      });

      describe("ArrayList", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#arrayListProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          child.addToArrayList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          child.addToArrayList("Hello");

          await nextTick();
          child.removeFromArrayList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("LinkedList", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#linkedListProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          child.addToLinkedList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          child.addToLinkedList("Hello");

          await nextTick();
          child.removeFromLinkedList("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("HashSet", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#hashSetProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          child.addToHashSet("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          child.addToHashSet("Hello");

          await nextTick();
          child.removeFromHashSet("Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });

      describe("HashMap", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#hashMap");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', async () => {
          child.addToHashMap("Key", "Hello");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });

        it('should should update when removing an item', async () => {
          child.addToHashMap("Key", "Hello");

          await nextTick();
          child.removeFromHashMap("Key");

          await nextTick();
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
      });
    });
  });
});