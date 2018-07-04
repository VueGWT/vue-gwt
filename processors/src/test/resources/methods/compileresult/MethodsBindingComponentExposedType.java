package methods;

import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import elemental2.core.Function;

@Generated(
    value = "com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator",
    comments = "https://github.com/Axellience/vue-gwt"
)
@JsType(
    namespace = "VueGWTExposedTypesRepository",
    name = "methods_MethodsBindingComponent"
)
public class MethodsBindingComponentExposedType extends MethodsBindingComponent implements DataFieldsProvider {

  @JsMethod
  @SuppressWarnings("unusable-by-js")
  public String exp$0() {
    // MethodsBindingComponent.html, line 2
    return VueGWTTools.templateExpressionToString(simpleObject.getText());
  }

  @JsMethod
  @SuppressWarnings("unusable-by-js")
  public String exp$1() {
    // MethodsBindingComponent.html, line 3
    return VueGWTTools.templateExpressionToString(simpleObject.getText() + " Expression 2");
  }

  public VueComponentOptions<MethodsBindingComponent> getOptions() {
    VueComponentOptions<MethodsBindingComponent> options = new VueComponentOptions<MethodsBindingComponent>();
    Proto p = this.__proto__;
    options.setComponentExportedTypePrototype(VueGWT.getComponentExposedTypeConstructorFn(MethodsBindingComponent.class).getPrototype());
    options.addMethod("onClickMethod", p.onClickMethod);
    options.addHookMethod("created", p.vuegwt$created);
    options.initData(true, this.vuegwt$getDataFieldsName());
    options.registerTemplateMethods(p.exp$0, p.exp$1);
    options.initRenderFunctions(getRenderFunction(), getStaticRenderFunctions());
    return options;
  }

  @JsType(
      isNative = true,
      namespace = JsPackage.GLOBAL,
      name = "Object"
  )
  private static class Proto {
    public Function onClickMethod;

    public Function vuegwt$created;

    public Function exp$0;

    public Function exp$1;
  }
}