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
          expect(component.oldValueAnnotation).to.be.null;
          expect(component.newValueAnnotation).to.be.not.null;
          expect(component.newValueAnnotation.getStringProperty()).to.equal(
              'value');

          component.changeWatchedDataAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValueAnnotation).to.be.not.null;
          expect(component.oldValueAnnotation.getStringProperty()).to.equal(
              'value');
          expect(component.newValueAnnotation).to.be.null;
        })
      });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.undefined;
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('value');

              component.changeWatchedDataAnnotation(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.not.null;
              expect(component.oldValueAnnotationProperty).to.equal('value');
              expect(component.newValueAnnotationProperty).to.be.undefined;
            });
          });

      it('should be called when watching property on data and property changes',
          () => {
            component.changeWatchedDataAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.undefined;
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('value');

              component.watchedDataAnnotation.setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.not.null;
              expect(component.oldValueAnnotationProperty).to.equal('value');
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('newValue');
            })
          });

      it('should be called when watching data deep and data changes', () => {
        component.changeWatchedDataDeepAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.oldValueAnnotationDeep).to.be.null;
          expect(component.newValueAnnotationDeep).to.be.not.null;
          expect(component.newValueAnnotationDeep).to.equal('value');

          component.changeWatchedDataDeepAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValueAnnotationDeep).to.be.not.null;
          expect(component.oldValueAnnotationDeep).to.equal('value');
          expect(component.newValueAnnotationDeep).to.be.null;
        });
      });

      it('should be called when watching data deep and property on data changes',
          () => {
            component.changeWatchedDataDeepAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationDeep).to.be.null;
              expect(component.newValueAnnotationDeep).to.be.not.null;
              expect(component.newValueAnnotationDeep).to.equal('value');

              component.watchedDataDeepAnnotation.setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationDeep).to.be.not.null;
              // Same object is passed as old, as the props as changed on it we get "newValue" too in old value
              expect(component.oldValueAnnotationDeep).to.equal('newValue');
              expect(component.newValueAnnotationDeep).to.be.not.null;
              expect(component.newValueAnnotationDeep).to.equal('newValue');
            });
          });

      it('should be called on init when using immediate', () => {
          expect(component.oldValueAnnotationImmediate).to.be.null;
          expect(component.newValueAnnotationImmediate).to.equal('initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(component.oldValueAnnotationPropertyImmediate).to.be.undefined;
        expect(component.newValueAnnotationPropertyImmediate).to.equal('initialValue');
      });
    });

    describe('$watch with string', () => {
      it('should be called when watching data and data changes', () => {
        component.changeWatchedData$WatchString('value');

        return nextTick()
        .then(() => {
          expect(component.oldValue$WatchString).to.be.null;
          expect(component.newValue$WatchString).to.be.not.null;
          expect(component.newValue$WatchString.getStringProperty()).to.equal(
              'value');

          component.changeWatchedData$WatchString(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValue$WatchString).to.be.not.null;
          expect(component.oldValue$WatchString.getStringProperty()).to.equal(
              'value');
          expect(component.newValue$WatchString).to.be.null;
        })
      });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedData$WatchString('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.undefined;
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal('value');

              component.changeWatchedData$WatchString(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.not.null;
              expect(component.oldValue$WatchStringProperty).to.equal(
                  'value');
              expect(component.newValue$WatchStringProperty).to.be.undefined;
            });
          });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedData$WatchString('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.undefined;
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal('value');

              component.watchedData$WatchString.setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.not.null;
              expect(component.oldValue$WatchStringProperty).to.equal('value');
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal(
                  'newValue');
            });
          });
    });

    describe('$watch with method', () => {
      it('should be called when watching data and data changes', () => {
        component.changeWatchedData$WatchMethod('value');

        return nextTick()
        .then(() => {
          expect(component.oldValue$WatchMethod).to.be.null;
          expect(component.newValue$WatchMethod).to.be.not.null;
          expect(component.newValue$WatchMethod.getStringProperty()).to.equal(
              'value');

          component.changeWatchedData$WatchMethod(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValue$WatchMethod).to.be.not.null;
          expect(component.oldValue$WatchMethod.getStringProperty()).to.equal(
              'value');
          expect(component.newValue$WatchMethod).to.be.null;
        })
      });

      it('should be called when watching property on data and data changes',
          () => {
            component.changeWatchedData$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.null;
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal('value');

              component.changeWatchedData$WatchMethod(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.not.null;
              expect(component.oldValue$WatchMethodProperty).to.equal(
                  'value');
              expect(component.newValue$WatchMethodProperty).to.be.null;
            });
          });

      it('should be called when watching property on data and property changes',
          () => {
            component.changeWatchedData$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.null;
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal('value');

              component.watchedData$WatchMethod.setStringProperty('newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.not.null;
              expect(component.oldValue$WatchMethodProperty).to.equal('value');
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal(
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
          expect(component.oldValueAnnotation).to.be.null;
          expect(component.newValueAnnotation).to.be.not.null;
          expect(component.newValueAnnotation.getStringProperty()).to.equal(
              'value');

          parentComponent.changeWatchedPropAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValueAnnotation).to.be.not.null;
          expect(component.oldValueAnnotation.getStringProperty()).to.equal(
              'value');
          expect(component.newValueAnnotation).to.be.null;
        })
      });

      it('should be called when watching property on @Prop and @Prop changes',
          () => {
            parentComponent.changeWatchedPropAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.undefined;
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('value');

              parentComponent.changeWatchedPropAnnotation(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.not.null;
              expect(component.oldValueAnnotationProperty).to.equal('value');
              expect(component.newValueAnnotationProperty).to.be.undefined;
            });
          });

      it('should be called when watching property on @Prop and property changes',
          () => {
            parentComponent.changeWatchedPropAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.undefined;
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('value');

              parentComponent.watchedPropAnnotation.setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationProperty).to.be.not.null;
              expect(component.oldValueAnnotationProperty).to.equal('value');
              expect(component.newValueAnnotationProperty).to.be.not.null;
              expect(component.newValueAnnotationProperty).to.equal('newValue');
            });
          });

      it('should be called when watching @Prop deep and @Prop changes', () => {
        parentComponent.changeWatchedPropDeepAnnotation('value');

        return nextTick()
        .then(() => {
          expect(component.oldValueAnnotationDeep).to.be.null;
          expect(component.newValueAnnotationDeep).to.be.not.null;
          expect(component.newValueAnnotationDeep).to.equal('value');

          parentComponent.changeWatchedPropDeepAnnotation(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValueAnnotationDeep).to.be.not.null;
          expect(component.oldValueAnnotationDeep).to.equal('value');
          expect(component.newValueAnnotationDeep).to.be.null;
        });
      });

      it('should be called when watching @Prop deep and property on @Prop changes',
          () => {
            parentComponent.changeWatchedPropDeepAnnotation('value');

            return nextTick()
            .then(() => {
              expect(component.oldValueAnnotationDeep).to.be.null;
              expect(component.newValueAnnotationDeep).to.be.not.null;
              expect(component.newValueAnnotationDeep).to.equal('value');

              parentComponent.watchedPropDeepAnnotation.setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValueAnnotationDeep).to.be.not.null;
              // Same object is passed as old, as the props as changed on it we get "newValue" too in old value
              expect(component.oldValueAnnotationDeep).to.equal('newValue');
              expect(component.newValueAnnotationDeep).to.be.not.null;
              expect(component.newValueAnnotationDeep).to.equal('newValue');
            });
          });
    });

    describe('$watch with string', () => {
      it('should be called when watching @Prop and @Prop changes', () => {
        parentComponent.changeWatchedProp$WatchString('value');

        return nextTick()
        .then(() => {
          expect(component.oldValue$WatchString).to.be.null;
          expect(component.newValue$WatchString).to.be.not.null;
          expect(component.newValue$WatchString.getStringProperty()).to.equal(
              'value');

          parentComponent.changeWatchedProp$WatchString(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValue$WatchString).to.be.not.null;
          expect(component.oldValue$WatchString.getStringProperty()).to.equal(
              'value');
          expect(component.newValue$WatchString).to.be.null;
        })
      });

      it('should be called when watching property on @Prop and @Prop changes',
          () => {
            parentComponent.changeWatchedProp$WatchString('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.undefined;
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal('value');

              parentComponent.changeWatchedProp$WatchString(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.not.null;
              expect(component.oldValue$WatchStringProperty).to.equal(
                  'value');
              expect(component.newValue$WatchStringProperty).to.be.undefined;
            });
          });

      it('should be called when watching property on @Prop and property changes',
          () => {
            parentComponent.changeWatchedProp$WatchString('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.undefined;
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal('value');

              parentComponent.watchedProp$WatchString.setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchStringProperty).to.be.not.null;
              expect(component.oldValue$WatchStringProperty).to.equal('value');
              expect(component.newValue$WatchStringProperty).to.be.not.null;
              expect(component.newValue$WatchStringProperty).to.equal(
                  'newValue');
            });
          });
    });

    describe('$watch with method', () => {
      it('should be called when watching @Prop and @Prop changes', () => {
        parentComponent.changeWatchedProp$WatchMethod('value');

        return nextTick()
        .then(() => {
          expect(component.oldValue$WatchMethod).to.be.null;
          expect(component.newValue$WatchMethod).to.be.not.null;
          expect(component.newValue$WatchMethod.getStringProperty()).to.equal(
              'value');

          parentComponent.changeWatchedProp$WatchMethod(null);
          return nextTick();
        })
        .then(() => {
          expect(component.oldValue$WatchMethod).to.be.not.null;
          expect(component.oldValue$WatchMethod.getStringProperty()).to.equal(
              'value');
          expect(component.newValue$WatchMethod).to.be.null;
        })
      });

      it('should be called when watching property on @Prop and @Prop changes',
          () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.null;
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal('value');

              parentComponent.changeWatchedProp$WatchMethod(null);
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.not.null;
              expect(component.oldValue$WatchMethodProperty).to.equal(
                  'value');
              expect(component.newValue$WatchMethodProperty).to.be.null;
            });
          });

      it('should be called when watching property on @Prop and property changes',
          () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            return nextTick()
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.null;
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal('value');

              parentComponent.watchedProp$WatchMethod.setStringProperty(
                  'newValue');
              return nextTick();
            })
            .then(() => {
              expect(component.oldValue$WatchMethodProperty).to.be.not.null;
              expect(component.oldValue$WatchMethodProperty).to.equal('value');
              expect(component.newValue$WatchMethodProperty).to.be.not.null;
              expect(component.newValue$WatchMethodProperty).to.equal(
                  'newValue');
            });
          });

      it('should be called on init when using immediate', () => {
        expect(component.oldValueAnnotationImmediate).to.be.undefined;
        expect(component.newValueAnnotationImmediate).to.equal('initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(component.oldValueAnnotationPropertyImmediate).to.be.undefined;
        expect(component.newValueAnnotationPropertyImmediate).to.equal('initialValue');
      });
    });
  });
});