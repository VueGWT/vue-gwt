package templateparser;

@Generated(
    value = "com.axellience.vuegwt.processors.component.ComponentExposedTypeGenerator",
    comments = "https://github.com/Axellience/vue-gwt"
)
@JsType(
    namespace = "VueGWTExposedTypesRepository",
    name = "templateparser_MustacheExpressionComponent"
)
public class MustacheExpressionComponentExposedType extends MustacheExpressionComponent {

  @JsMethod
  @SuppressWarnings("unusable-by-js")
  public String exp$0() {
    // MustacheExpressionComponent.html, line 1
    return VueGWTTools.templateExpressionToString(simpleObject.getText());
  }
}