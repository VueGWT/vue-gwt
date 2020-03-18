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

    beforeEach(async () => {
      await onGwtReady();
      component = createAndMountComponent(
          'com.axellience.vuegwt.tests.client.components.basic.watch.data.WatchDataTestComponent');
    });

    afterEach(() => {
      destroyComponent(component);
    });

    describe('@Watch', () => {
      it('should be called when watching data and data changes', async () => {
        component.changeWatchedDataAnnotation('value');

        await nextTick();
        expect(component.getOldValueAnnotation()).to.be.null;
        expect(component.getNewValueAnnotation()).to.be.not.null;
        expect(component.getNewValueAnnotation().getStringProperty()).to.equal(
            'value');
        component.changeWatchedDataAnnotation(null);

        await nextTick();
        expect(component.getOldValueAnnotation()).to.be.not.null;
        expect(component.getOldValueAnnotation().getStringProperty()).to.equal(
            'value');
        expect(component.getNewValueAnnotation()).to.be.null;
      });

      it('should be called when watching property on data and data changes',
          async () => {
            component.changeWatchedDataAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.null;
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal('value');
            component.changeWatchedDataAnnotation(null);

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.not.null;
            expect(component.getOldValueAnnotationProperty()).to.equal('value');
            expect(component.getNewValueAnnotationProperty()).to.be.null;
          });

      it('should be called when watching property on data and property changes',
          async () => {
            component.changeWatchedDataAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.null;
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal('value');
            component.getWatchedDataAnnotation().setStringProperty('newValue');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.not.null;
            expect(component.getOldValueAnnotationProperty()).to.equal('value');
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal(
                'newValue');
          });

      it('should be called when watching expression on data and data changes',
          async () => {
            component.changeWatchedDataAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationExpression()).to.be.null;
            expect(component.getNewValueAnnotationExpression()).to.be.not.null;
            expect(component.getNewValueAnnotationExpression()).to.equal(
                'value');
            component.changeWatchedDataAnnotation(null);

            await nextTick();
            expect(component.getOldValueAnnotationExpression()).to.be.not.null;
            expect(component.getOldValueAnnotationExpression()).to.equal(
                'value');
            expect(component.getNewValueAnnotationExpression()).to.be.null;
          });

      it('should be called when watching expression on data and property changes',
          async () => {
            component.changeWatchedDataAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationExpression()).to.be.null;
            expect(component.getNewValueAnnotationExpression()).to.be.not.null;
            expect(component.getNewValueAnnotationExpression()).to.equal(
                'value');
            component.getWatchedDataAnnotation().setStringProperty('newValue');

            await nextTick();
            expect(component.getOldValueAnnotationExpression()).to.be.not.null;
            expect(component.getOldValueAnnotationExpression()).to.equal(
                'value');
            expect(component.getNewValueAnnotationExpression()).to.be.not.null;
            expect(component.getNewValueAnnotationExpression()).to.equal(
                'newValue');
          });

      it('should be called when watching data deep and data changes',
          async () => {
            component.changeWatchedDataDeepAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.null;
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('value');
            component.changeWatchedDataDeepAnnotation(null);

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.not.null;
            expect(component.getOldValueAnnotationDeep()).to.equal('value');
            expect(component.getNewValueAnnotationDeep()).to.be.null;
          });

      it('should be called when watching data deep and property on data changes',
          async () => {
            component.changeWatchedDataDeepAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.null;
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('value');
            component.getWatchedDataDeepAnnotation().setStringProperty(
                'newValue');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.not.null;
            expect(component.getOldValueAnnotationDeep()).to.equal('newValue');
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('newValue');
          });

      it('should be called on init when using immediate', () => {
        expect(component.getOldValueAnnotationImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationImmediate()).to.equal(
            'initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(
            component.getOldValueAnnotationPropertyImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationPropertyImmediate()).to.equal(
            'initialValue');
      });
    });

    describe('$watch with method', () => {
      it('should be called when watching data and data changes', async () => {
        component.changeWatchedData$WatchMethod('value');

        await nextTick();
        expect(component.getOldValue$WatchMethod()).to.be.null;
        expect(component.getNewValue$WatchMethod()).to.be.not.null;
        expect(
            component.getNewValue$WatchMethod().getStringProperty()).to.equal(
            'value');
        component.changeWatchedData$WatchMethod(null);

        await nextTick();
        expect(component.getOldValue$WatchMethod()).to.be.not.null;
        expect(
            component.getOldValue$WatchMethod().getStringProperty()).to.equal(
            'value');
        expect(component.getNewValue$WatchMethod()).to.be.null;
      });

      it('should be called when watching property on data and data changes',
          async () => {
            component.changeWatchedData$WatchMethod('value');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.null;
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'value');
            component.changeWatchedData$WatchMethod(null);

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getOldValue$WatchMethodProperty()).to.equal(
                'value');
            expect(component.getNewValue$WatchMethodProperty()).to.be.null;
          });

      it('should be called when watching property on data and property changes',
          async () => {
            component.changeWatchedData$WatchMethod('value');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.null;
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'value');
            component.getWatchedData$WatchMethod().setStringProperty(
                'newValue');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getOldValue$WatchMethodProperty()).to.equal(
                'value');
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'newValue');
          });
    });
  });

  describe('Watch Prop', () => {
    let parentComponent;
    let component;

    beforeEach(async () => {
      await onGwtReady();
      parentComponent = createAndMountComponent(
          'com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropParentTestComponent');
      component = parentComponent.getWatchPropTestComponent();
    });

    afterEach(() => {
      destroyComponent(parentComponent);
    });

    describe('@Watch', () => {
      it('should be called when watching @Prop and @Prop changes', async () => {
        parentComponent.changeWatchedPropAnnotation('value');

        await nextTick();
        expect(component.getOldValueAnnotation()).to.be.null;
        expect(component.getNewValueAnnotation()).to.be.not.null;
        expect(component.getNewValueAnnotation().getStringProperty()).to.equal(
            'value');
        parentComponent.changeWatchedPropAnnotation(null);

        await nextTick();
        expect(component.getOldValueAnnotation()).to.be.not.null;
        expect(component.getOldValueAnnotation().getStringProperty()).to.equal(
            'value');
        expect(component.getNewValueAnnotation()).to.be.null;
      });

      it('should be called when watching property on @Prop and @Prop changes',
          async () => {
            parentComponent.changeWatchedPropAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.null;
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal('value');
            parentComponent.changeWatchedPropAnnotation(null);

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.not.null;
            expect(component.getOldValueAnnotationProperty()).to.equal('value');
            expect(component.getNewValueAnnotationProperty()).to.be.null;
          });

      it('should be called when watching property on @Prop and property changes',
          async () => {
            parentComponent.changeWatchedPropAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.null;
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal(
                'value');
            parentComponent.getWatchedPropAnnotation().setStringProperty(
                'newValue');

            await nextTick();
            expect(component.getOldValueAnnotationProperty()).to.be.not.null;
            expect(component.getOldValueAnnotationProperty()).to.equal(
                'value');
            expect(component.getNewValueAnnotationProperty()).to.be.not.null;
            expect(component.getNewValueAnnotationProperty()).to.equal(
                'newValue');
          });

      it('should be called when watching @Prop deep and @Prop changes',
          async () => {
            parentComponent.changeWatchedPropDeepAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.null;
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('value');
            parentComponent.changeWatchedPropDeepAnnotation(null);

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.not.null;
            expect(component.getOldValueAnnotationDeep()).to.equal('value');
            expect(component.getNewValueAnnotationDeep()).to.be.null;
          });

      it('should be called when watching @Prop deep and property on @Prop changes',
          async () => {
            parentComponent.changeWatchedPropDeepAnnotation('value');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.null;
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('value');
            parentComponent.getWatchedPropDeepAnnotation().setStringProperty(
                'newValue');

            await nextTick();
            expect(component.getOldValueAnnotationDeep()).to.be.not.null;
            expect(component.getOldValueAnnotationDeep()).to.equal('newValue');
            expect(component.getNewValueAnnotationDeep()).to.be.not.null;
            expect(component.getNewValueAnnotationDeep()).to.equal('newValue');
          });
    });

    describe('$watch with method', () => {
      it('should be called when watching @Prop and @Prop changes', async () => {
        parentComponent.changeWatchedProp$WatchMethod('value');

        await nextTick();
        expect(component.getOldValue$WatchMethod()).to.be.null;
        expect(component.getNewValue$WatchMethod()).to.be.not.null;
        expect(
            component.getNewValue$WatchMethod().getStringProperty()).to.equal(
            'value');
        parentComponent.changeWatchedProp$WatchMethod(null);

        await nextTick();
        expect(component.getOldValue$WatchMethod()).to.be.not.null;
        expect(
            component.getOldValue$WatchMethod().getStringProperty()).to.equal(
            'value');
        expect(component.getNewValue$WatchMethod()).to.be.null;
      });

      it('should be called when watching property on @Prop and @Prop changes',
          async () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.null;
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'value');
            parentComponent.changeWatchedProp$WatchMethod(null);

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getOldValue$WatchMethodProperty()).to.equal(
                'value');
            expect(component.getNewValue$WatchMethodProperty()).to.be.null;
          });

      it('should be called when watching property on @Prop and property changes',
          async () => {
            parentComponent.changeWatchedProp$WatchMethod('value');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.null;
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'value');
            parentComponent.getWatchedProp$WatchMethod().setStringProperty(
                'newValue');

            await nextTick();
            expect(component.getOldValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getOldValue$WatchMethodProperty()).to.equal(
                'value');
            expect(component.getNewValue$WatchMethodProperty()).to.be.not.null;
            expect(component.getNewValue$WatchMethodProperty()).to.equal(
                'newValue');
          });

      it('should be called on init when using immediate', () => {
        expect(component.getOldValueAnnotationImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationImmediate()).to.equal(
            'initialValue');
      });

      it('should be called on init when using immediate on property', () => {
        expect(
            component.getOldValueAnnotationPropertyImmediate()).to.be.undefined;
        expect(component.getNewValueAnnotationPropertyImmediate()).to.equal(
            'initialValue');
      });
    });
  });
});