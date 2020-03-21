package props;

import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import elemental2.core.Function;
import javax.annotation.Generated;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

@Generated(
    value = "com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator",
    comments = "https://github.com/Axellience/vue-gwt"
)
public class PropComponentExposedType extends PropComponent {

  public VueComponentOptions<PropComponent> getOptions() {
    VueComponentOptions<PropComponent> options = new VueComponentOptions<PropComponent>();
    Proto p = __proto__;
    options.setComponentExportedTypePrototype(p);
    options.addJavaProp("myProp", VueGWTTools.getFieldName(this, () -> this.myProp = 1), false, "Number");
    options.addHookMethod("created", p.vg$created);
    options.initData(true, VueGWTTools.getFieldsName(this, () -> {
    }));
    return options;
  }
}