import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Java Collections Observation', () => {
  let component;

  beforeEach(() => onGwtReady().then(() => {
    component = createAndMountComponent(
        'com.axellience.vuegwt.tests.client.components.collections.CollectionObservationComponent');
  }));

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

      it('should should update when adding an item', () => {
        component.addToArrayList("Hello");

        return nextTick().then(() => {
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });
      });

      it('should should update when removing an item', () => {
        component.addToArrayList("Hello");

        return nextTick()
        .then(() => {
          component.removeFromArrayList("Hello");
          return nextTick();
        })
        .then(() => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
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

      it('should should update when adding an item', () => {
        component.addToLinkedList("Hello");

        return nextTick().then(() => {
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });
      });

      it('should should update when removing an item', () => {
        component.addToLinkedList("Hello");

        return nextTick()
        .then(() => {
          component.removeFromLinkedList("Hello");
          return nextTick();
        })
        .then(() => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
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

      it('should should update when adding an item', () => {
        component.addToHashSet("Hello");

        return nextTick().then(() => {
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });
      });

      it('should should update when removing an item', () => {
        component.addToHashSet("Hello");

        return nextTick()
        .then(() => {
          component.removeFromHashSet("Hello");
          return nextTick();
        })
        .then(() => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
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

      it('should should update when adding an item', () => {
        component.addToHashMap("Key", "Hello");

        return nextTick().then(() => {
          expect(collectionDiv.childNodes.length).to.equal(1);
          expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
        });
      });

      it('should should update when removing an item', () => {
        component.addToHashMap("Key", "Hello");

        return nextTick()
        .then(() => {
          component.removeFromHashMap("Key");
          return nextTick();
        })
        .then(() => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });
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

        it('should should update when adding an item', () => {
          component.addToArrayList("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          component.addToArrayList("Hello");

          return nextTick()
          .then(() => {
            component.removeFromArrayList("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          component.addToLinkedList("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          component.addToLinkedList("Hello");

          return nextTick()
          .then(() => {
            component.removeFromLinkedList("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          component.addToHashSet("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          component.addToHashSet("Hello");

          return nextTick()
          .then(() => {
            component.removeFromHashSet("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          component.addToHashMap("Key", "Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          component.addToHashMap("Key", "Hello");

          return nextTick()
          .then(() => {
            component.removeFromHashMap("Key");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
        });
      });
    });

    describe("When changing from Child", () => {
      let child;

      beforeEach(() => onGwtReady().then(() => {
        child = component.$refs.child;
      }));

      describe("ArrayList", () => {
        let collectionDiv;

        beforeEach(() => {
          collectionDiv = component.$el.querySelector("#arrayListProp");
        });

        it('should start empty', () => {
          expect(collectionDiv.childNodes.length).to.equal(0);
        });

        it('should should update when adding an item', () => {
          child.addToArrayList("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          child.addToArrayList("Hello");

          return nextTick()
          .then(() => {
            child.removeFromArrayList("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          child.addToLinkedList("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          child.addToLinkedList("Hello");

          return nextTick()
          .then(() => {
            child.removeFromLinkedList("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          child.addToHashSet("Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          child.addToHashSet("Hello");

          return nextTick()
          .then(() => {
            child.removeFromHashSet("Hello");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
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

        it('should should update when adding an item', () => {
          child.addToHashMap("Key", "Hello");

          return nextTick().then(() => {
            expect(collectionDiv.childNodes.length).to.equal(1);
            expect(collectionDiv.childNodes[0].innerText).to.equal("Hello");
          });
        });

        it('should should update when removing an item', () => {
          child.addToHashMap("Key", "Hello");

          return nextTick()
          .then(() => {
            child.removeFromHashMap("Key");
            return nextTick();
          })
          .then(() => {
            expect(collectionDiv.childNodes.length).to.equal(0);
          });
        });
      });
    });
  });
});