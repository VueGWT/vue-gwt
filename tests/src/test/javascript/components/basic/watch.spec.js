import {expect} from 'chai'
import {
  createAndMountComponent,
  destroyComponent,
  nextTick,
  onGwtReady
} from '../../vue-gwt-tests-utils'

describe('Watch', () => {
  describe('Watch Data', () => {
    let component;

    beforeEach(() => onGwtReady().then(() => {
      component = createAndMountComponent(
          'com.axellience.vuegwt.tests.client.components.basic.watch.data.WatchDataTestComponent');
    }));

    afterEach(() => {
      destroyComponent(component);
    });

    describe('@Watch', () => {
      it('should be called when watching data and data changes', () => {
        component.changeWatchedDataAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValueAnnotation()).to.be.null;
          expect(component.getNewValueAnnotation()).to.be.not.null;
          expect(component.getNewValueAnnotation().getStringProperty()).to.equal(
              'value');

          component.changeWatchedDataAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValueAnnotation()).to.be.not.null;
          expect(component.getOldValueAnnotation().getStringProperty()).to.equal(
              'value');
          expect(component.getNewValueAnnotation()).to.be.null;
        })
      });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.null;
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('value');

              component.changeWatchedDataAnnotation(null);
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.not.null;
              expect(component.getOldValueAnnotationProperty()).to.equal('value');
              expect(component.getNewValueAnnotationProperty()).to.be.null;
            });
          });

      it('should be called when watching property on data and property changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.null;
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('value');

              component.getWatchedDataAnnotation().setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.not.null;
              expect(component.getOldValueAnnotationProperty()).to.equal('value');
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('newValue');
            })
          });

      it('should be called when watching expression on data and data changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationExpression()).to.be.null;
              expect(component.getNewValueAnnotationExpression()).to.be.not.null;
              expect(component.getNewValueAnnotationExpression()).to.equal('value');

              component.changeWatchedDataAnnotation(null);
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationExpression()).to.be.not.null;
              expect(component.getOldValueAnnotationExpression()).to.equal('value');
              expect(component.getNewValueAnnotationExpression()).to.be.null;
            });
          });

      it('should be called when watching expression on data and property changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationExpression()).to.be.null;
              expect(component.getNewValueAnnotationExpression()).to.be.not.null;
              expect(component.getNewValueAnnotationExpression()).to.equal('value');

              component.getWatchedDataAnnotation().setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationExpression()).to.be.not.null;
              expect(component.getOldValueAnnotationExpression()).to.equal('value');
              expect(component.getNewValueAnnotationExpression()).to.be.not.null;
              expect(component.getNewValueAnnotationExpression()).to.equal('newValue');
            })
          });

      it('should be called when watching data deep and data changes', () => {
        component.changeWatchedDataDeepAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValueAnnotationDeep()).to.be.null;
          expect(component.getNewValueAnnotationDeep()).to.be.not.null;
          expect(component.getNewValueAnnotationDeep()).to.equal('value');

          component.changeWatchedDataDeepAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValueAnnotationDeep()).to.be.not.null;
          expect(component.getOldValueAnnotationDeep()).to.equal('value');
          expect(component.getNewValueAnnotationDeep()).to.be.null;
        });
      });

      it('should be called when watching data deep and property on data changes',
          () => {
            component.changeWatchedDataDeepAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationDeep()).to.be.null;
              expect(component.getNewValueAnnotationDeep()).to.be.not.null;
              expect(component.getNewValueAnnotationDeep()).to.equal('value');

              component.getWatchedDataDeepAnnotation().setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationDeep()).to.be.not.null;
              // Same object is passed as old, as the props as changed on it we get "newValue" too in old value
              expect(component.getOldValueAnnotationDeep()).to.equal('newValue');
              expect(component.getNewValueAnnotationDeep()).to.be.not.null;
              expect(component.getNewValueAnnotationDeep()).to.equal('newValue');
            });
          });

      it('should be called on init when using immediate', () => {
        expect(component.getOldValueAnnotationImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationImmediate()).to.equal('initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(component.getOldValueAnnotationPropertyImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationPropertyImmediate()).to.equal('initialValue');
      });
    });

    describe('$watch with method', () => {
      it('should be called when watching data and data changes', () => {
        component.changeWatchedData$WatchMethod('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValue$WatchMethod()).to.be.null;
          expect(component.getNewValue$WatchMethod()).to.be.not.null;
          expect(component.getNewValue$WatchMethod().getStringProperty()).to.equal(
              'value');

          component.changeWatchedData$WatchMethod(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValue$WatchMethod()).to.be.not.null;
          expect(component.getOldValue$WatchMethod().getStringProperty()).to.equal(
              'value');
          expect(component.getNewValue$WatchMethod()).to.be.null;
        })
      });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedData$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.null;
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal('value');

              component.changeWatchedData$WatchMethod(null);
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getOldValue$WatchMethodProperty()).to.equal(
                  'value');
              expect(component.getNewValue$WatchMethodProperty()).to.be.null;
            });
          });

      it('should be called when watching property on data and property changes',
          () => {
            component.changeWatchedData$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.null;
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal('value');

              component.getWatchedData$WatchMethod().setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getOldValue$WatchMethodProperty()).to.equal('value');
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal(
                  'newValue');
            });
          });
    });
  });

  describe('Watch Prop', () => {
    let parentComponent;
    let component;

    beforeEach(() => onGwtReady().then(() => {
      parentComponent = createAndMountComponent(
          'com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropParentTestComponent');
      component = parentComponent.getWatchPropTestComponent();
    }));

    afterEach(() => {
      destroyComponent(parentComponent);
    });

    describe('@Watch', () => {
      it('should be called when watching @Prop and @Prop changes', () => {
        parentComponent.changeWatchedPropAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValueAnnotation()).to.be.null;
          expect(component.getNewValueAnnotation()).to.be.not.null;
          expect(component.getNewValueAnnotation().getStringProperty()).to.equal(
              'value');

          parentComponent.changeWatchedPropAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValueAnnotation()).to.be.not.null;
          expect(component.getOldValueAnnotation().getStringProperty()).to.equal(
              'value');
          expect(component.getNewValueAnnotation()).to.be.null;
        })
      });

      it('should be called when watching property on @Prop and @Prop changes',
          () => {
            parentComponent.changeWatchedPropAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.null;
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('value');

              parentComponent.changeWatchedPropAnnotation(null);
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.not.null;
              expect(component.getOldValueAnnotationProperty()).to.equal('value');
              expect(component.getNewValueAnnotationProperty()).to.be.null;
            });
          });

      it('should be called when watching property on @Prop and property changes',
          () => {
            parentComponent.changeWatchedPropAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.null;
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('value');

              parentComponent.getWatchedPropAnnotation().setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationProperty()).to.be.not.null;
              expect(component.getOldValueAnnotationProperty()).to.equal('value');
              expect(component.getNewValueAnnotationProperty()).to.be.not.null;
              expect(component.getNewValueAnnotationProperty()).to.equal('newValue');
            });
          });

      it('should be called when watching @Prop deep and @Prop changes', () => {
        parentComponent.changeWatchedPropDeepAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValueAnnotationDeep()).to.be.null;
          expect(component.getNewValueAnnotationDeep()).to.be.not.null;
          expect(component.getNewValueAnnotationDeep()).to.equal('value');

          parentComponent.changeWatchedPropDeepAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValueAnnotationDeep()).to.be.not.null;
          expect(component.getOldValueAnnotationDeep()).to.equal('value');
          expect(component.getNewValueAnnotationDeep()).to.be.null;
        });
      });

      it('should be called when watching @Prop deep and property on @Prop changes',
          () => {
            parentComponent.changeWatchedPropDeepAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValueAnnotationDeep()).to.be.null;
              expect(component.getNewValueAnnotationDeep()).to.be.not.null;
              expect(component.getNewValueAnnotationDeep()).to.equal('value');

              parentComponent.getWatchedPropDeepAnnotation().setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValueAnnotationDeep()).to.be.not.null;
              // Same object is passed as old, as the props as changed on it we get "newValue" too in old value
              expect(component.getOldValueAnnotationDeep()).to.equal('newValue');
              expect(component.getNewValueAnnotationDeep()).to.be.not.null;
              expect(component.getNewValueAnnotationDeep()).to.equal('newValue');
            });
          });
    });

    describe('$watch with method', () => {
      it('should be called when watching @Prop and @Prop changes', () => {
        parentComponent.changeWatchedProp$WatchMethod('value');

        return nextTick()
        .then(() => {
          expect(component.getOldValue$WatchMethod()).to.be.null;
          expect(component.getNewValue$WatchMethod()).to.be.not.null;
          expect(component.getNewValue$WatchMethod().getStringProperty()).to.equal(
              'value');

          parentComponent.changeWatchedProp$WatchMethod(null);
          return nextTick();
        })
        .then(() => {
          expect(component.getOldValue$WatchMethod()).to.be.not.null;
          expect(component.getOldValue$WatchMethod().getStringProperty()).to.equal(
              'value');
          expect(component.getNewValue$WatchMethod()).to.be.null;
        })
      });

      it('should be called when watching property on @Prop and @Prop changes',
          () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.null;
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal('value');

              parentComponent.changeWatchedProp$WatchMethod(null);
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getOldValue$WatchMethodProperty()).to.equal(
                  'value');
              expect(component.getNewValue$WatchMethodProperty()).to.be.null;
            });
          });

      it('should be called when watching property on @Prop and property changes',
          () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.null;
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal('value');

              parentComponent.getWatchedProp$WatchMethod().setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getOldValue$WatchMethodProperty()).to.equal('value');
              expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
              expect(component.getNewValue$WatchMethodProperty()).to.equal(
                  'newValue');
            });
          });

      it('should be called on init when using immediate', () => {
        expect(component.getOldValueAnnotationImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationImmediate()).to.equal('initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(component.getOldValueAnnotationPropertyImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationPropertyImmediate()).to.equal('initialValue');
      });
    });
  });
});