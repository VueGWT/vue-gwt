package com.axellience.vuegwt.tests.client;

import static jsinterop.base.Js.cast;

import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwt.tests.client.components.basic.computed.ComputedTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.computed.ComputedTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.data.DataTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.data.DataTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.imports.StaticImportsComponent;
import com.axellience.vuegwt.tests.client.components.basic.imports.StaticImportsComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.prop.PropParentTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.prop.PropParentTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.prop.PropTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.prop.PropTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.propdefault.PropDefaultParentTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.propdefault.PropDefaultParentTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.propdefault.PropDefaultTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.propdefault.PropDefaultTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.propvalidator.PropValidatorParentTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.propvalidator.PropValidatorParentTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.propvalidator.PropValidatorTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.propvalidator.PropValidatorTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.watch.data.WatchDataTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.watch.data.WatchDataTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropParentTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropParentTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropTestComponent;
import com.axellience.vuegwt.tests.client.components.basic.watch.prop.WatchPropTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.collections.CollectionObservationComponent;
import com.axellience.vuegwt.tests.client.components.collections.CollectionObservationComponentFactory;
import com.axellience.vuegwt.tests.client.components.conditionalrendering.vif.VIfTestComponent;
import com.axellience.vuegwt.tests.client.components.conditionalrendering.vif.VIfTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.conditionalrendering.vshow.VShowTestComponent;
import com.axellience.vuegwt.tests.client.components.conditionalrendering.vshow.VShowTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.events.emitannotation.EmitAnnotationParent;
import com.axellience.vuegwt.tests.client.components.events.emitannotation.EmitAnnotationParentFactory;
import com.axellience.vuegwt.tests.client.components.events.types.EmitTypesChildComponent;
import com.axellience.vuegwt.tests.client.components.events.types.EmitTypesChildComponentFactory;
import com.axellience.vuegwt.tests.client.components.events.types.EmitTypesParentComponent;
import com.axellience.vuegwt.tests.client.components.events.types.EmitTypesParentComponentFactory;
import com.axellience.vuegwt.tests.client.components.globalregistration.GloballyRegisteredComponentFactory;
import com.axellience.vuegwt.tests.client.components.globalregistration.GloballyRegisteredWithNameComponentFactory;
import com.axellience.vuegwt.tests.client.components.globalregistration.GloballyRegisteredWithNameOnRegistrationComponentFactory;
import com.axellience.vuegwt.tests.client.components.globalregistration.UsingGloballyRegisteredComponent;
import com.axellience.vuegwt.tests.client.components.globalregistration.UsingGloballyRegisteredComponentFactory;
import com.axellience.vuegwt.tests.client.components.inheritance.ChildComponent;
import com.axellience.vuegwt.tests.client.components.inheritance.ChildComponentFactory;
import com.axellience.vuegwt.tests.client.components.jsinterop.ComputedOverrideComponent;
import com.axellience.vuegwt.tests.client.components.jsinterop.ComputedOverrideComponentFactory;
import com.axellience.vuegwt.tests.client.components.slots.SlotScopeParentComponent;
import com.axellience.vuegwt.tests.client.components.slots.SlotScopeParentComponentFactory;
import com.axellience.vuegwt.tests.client.components.style.classbinding.ClassBindingTestComponent;
import com.axellience.vuegwt.tests.client.components.style.classbinding.ClassBindingTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.style.inlinestylebinding.InlineStyleBindingTestComponent;
import com.axellience.vuegwt.tests.client.components.style.inlinestylebinding.InlineStyleBindingTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.style.scoped.ScopedTestComponent;
import com.axellience.vuegwt.tests.client.components.style.scoped.ScopedTestComponentFactory;
import com.axellience.vuegwt.tests.client.components.vmodel.VModelComponent;
import com.axellience.vuegwt.tests.client.components.vmodel.VModelComponentFactory;
import com.google.gwt.core.client.EntryPoint;
import elemental2.core.Function;
import elemental2.core.JsArray;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

public class VueGwtTestsApp implements EntryPoint {

  public void onModuleLoad() {
    VueGWT.init();
    registerTestComponent(ComputedTestComponent.class, ComputedTestComponentFactory.get());
    registerTestComponent(DataTestComponent.class, DataTestComponentFactory.get());
    registerTestComponent(PropParentTestComponent.class, PropParentTestComponentFactory.get());
    registerTestComponent(PropTestComponent.class, PropTestComponentFactory.get());
    registerTestComponent(
        PropDefaultParentTestComponent.class,
        PropDefaultParentTestComponentFactory.get()
    );
    registerTestComponent(PropDefaultTestComponent.class, PropDefaultTestComponentFactory.get());
    registerTestComponent(
        PropValidatorParentTestComponent.class,
        PropValidatorParentTestComponentFactory.get()
    );
    registerTestComponent(
        PropValidatorTestComponent.class,
        PropValidatorTestComponentFactory.get()
    );
    registerTestComponent(WatchDataTestComponent.class, WatchDataTestComponentFactory.get());
    registerTestComponent(
        WatchPropParentTestComponent.class,
        WatchPropParentTestComponentFactory.get()
    );
    registerTestComponent(WatchPropTestComponent.class, WatchPropTestComponentFactory.get());
    registerTestComponent(
        CollectionObservationComponent.class,
        CollectionObservationComponentFactory.get()
    );
    registerTestComponent(VIfTestComponent.class, VIfTestComponentFactory.get());
    registerTestComponent(VShowTestComponent.class, VShowTestComponentFactory.get());
    registerTestComponent(EmitTypesChildComponent.class, EmitTypesChildComponentFactory.get());
    registerTestComponent(EmitTypesParentComponent.class, EmitTypesParentComponentFactory.get());
    registerTestComponent(EmitTypesParentComponent.class, EmitTypesParentComponentFactory.get());
    registerTestComponent(
        ClassBindingTestComponent.class,
        ClassBindingTestComponentFactory.get()
    );
    registerTestComponent(
        InlineStyleBindingTestComponent.class,
        InlineStyleBindingTestComponentFactory.get()
    );
    registerTestComponent(ScopedTestComponent.class, ScopedTestComponentFactory.get());
    registerTestComponent(VModelComponent.class, VModelComponentFactory.get());
    registerTestComponent(SlotScopeParentComponent.class, SlotScopeParentComponentFactory.get());

    Vue.component(GloballyRegisteredComponentFactory.get());
    Vue.component(GloballyRegisteredWithNameComponentFactory.get());
    Vue.component(
        "globally-registered-this-is-name-on-registration",
        GloballyRegisteredWithNameOnRegistrationComponentFactory.get()
    );
    registerTestComponent(
        UsingGloballyRegisteredComponent.class,
        UsingGloballyRegisteredComponentFactory.get()
    );
    registerTestComponent(StaticImportsComponent.class, StaticImportsComponentFactory.get());
    registerTestComponent(EmitAnnotationParent.class, EmitAnnotationParentFactory.get());
    registerTestComponent(ChildComponent.class, ChildComponentFactory.get());
    registerTestComponent(ComputedOverrideComponent.class, ComputedOverrideComponentFactory.get());

    Window.onVueGwtTestsReady.forEach((f, i, a) -> f.call(i, a));
  }

  private static <T extends IsVueComponent> void registerTestComponent(Class<T> componentClass,
      VueComponentFactory<T> componentFactory) {
    if (Window.vueGwtTestComponents == null) {
      Window.vueGwtTestComponents = cast(JsPropertyMap.of());
    }

    Window.vueGwtTestComponents.set(componentClass.getCanonicalName(),
        (VueJsConstructor<IsVueComponent>) componentFactory.getJsConstructor());
  }

  @JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "window")
  private static class Window {

    static JsArray<Function> onVueGwtTestsReady;
    static JsPropertyMap<VueJsConstructor<IsVueComponent>> vueGwtTestComponents;
  }
}
