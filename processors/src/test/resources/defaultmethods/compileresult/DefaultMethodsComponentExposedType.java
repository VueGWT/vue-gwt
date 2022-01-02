package defaultmethods;

import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.tools.FieldsExposer;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import elemental2.core.Function;
import java.lang.Object;
import java.lang.String;
import java.lang.SuppressWarnings;
import javax.annotation.Generated;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

@Generated(
    value = "com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator",
    comments = "https://github.com/Axellience/vue-gwt"
)
public class DefaultMethodsComponentExposedType extends DefaultMethodsComponent {
  @JsProperty
  public Proto __proto__;

  @JsProperty
  public boolean vg$hrc_0;

  @JsMethod
  public void vg$created() {
    if (vg$hrc_0) return;
    vg$hrc_0 = true;
    vue().$options().proxyFields(this);
    VueGWTTools.initComponentInstanceFields(this, new DefaultMethodsComponent());
    Proto p = Js.cast(vue().$options().getComponentExportedTypePrototype());
    super.created();
  }

  public static String getScopedCss() {
    return null;
  }

  private Function getRenderFunction() {
    String renderFunctionString = "with(this){return _c('div',[_c('div',[_v(_s(exp$0()))]),_v(\" \"),_c('div',[_v(_s(exp$1()))]),_v(\" \"),_c('div',{on:{\"click\":onClickMethod}})])}";
    return new Function(renderFunctionString);
  }

  private Function[] getStaticRenderFunctions() {
    return new Function[] {  };
  }

  @JsMethod
  @SuppressWarnings("unusable-by-js")
  public String exp$0() {
    // DefaultMethodsComponent.html, line 2
    return VueGWTTools.templateExpressionToString(simpleObject.getDefaultText());
  }

  @JsMethod
  @SuppressWarnings("unusable-by-js")
  public String exp$1() {
    // DefaultMethodsComponent.html, line 3
    return VueGWTTools.templateExpressionToString(simpleObject.getDefaultText() + " Expression 2");
  }

  public VueComponentOptions<DefaultMethodsComponent> getOptions() {
    VueComponentOptions<DefaultMethodsComponent> options = new VueComponentOptions<DefaultMethodsComponent>();
    Proto p = __proto__;
    options.setComponentExportedTypePrototype(p);
    options.addMethod("onClickMethod", p.onClickMethod);
    options.addHookMethod("created", p.vg$created);
    options.initData(true, VueGWTTools.getFieldsName(this, () -> {
      this.simpleObject = null;
    } ));
    options.registerTemplateMethods(p.exp$0, p.exp$1);
    options.initRenderFunctions(getRenderFunction(), getStaticRenderFunctions());
    return options;
  }

  @JsMethod
  void vg$ef() {
    this.simpleObject = FieldsExposer.v();
    FieldsExposer.e(simpleObject);
  }

  @JsType(
      isNative = true,
      namespace = JsPackage.GLOBAL,
      name = "Object"
  )
  private static class Proto implements JsPropertyMap<Object> {
    public Function onClickMethod;

    public Function vg$created;

    public Function exp$0;

    public Function exp$1;
  }
}