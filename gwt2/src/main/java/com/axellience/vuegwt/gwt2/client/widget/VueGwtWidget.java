package com.axellience.vuegwt.gwt2.client.widget;

import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import jsinterop.base.Js;

/**
 * Wraps a {@link IsVueComponent} instance in a GWT Widget.
 */
public class VueGwtWidget<T extends IsVueComponent> extends Widget {

  private final T vueComponentInstance;

  public VueGwtWidget(VueJsConstructor<T> vueJsConstructor) {
    super();
    setElement(Document.get().createDivElement());
    vueComponentInstance = vueJsConstructor.instantiate();
  }

  public VueGwtWidget(VueComponentFactory<T> vueFactory) {
    this(vueFactory.getJsConstructor());
  }

  /**
   * Returns the instance of the vue component wrapped in this widget.
   *
   * @return the component instance.
   */
  public T getComponent() {
    return vueComponentInstance;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    if (!isMounted()) {
      mountVueComponent();
    }
  }

  private boolean isMounted() {
    return getElement().hasChildNodes();
  }

  private void mountVueComponent() {
    HTMLDivElement vueElement = (HTMLDivElement) DomGlobal.document.createElement("div");
    getElement().appendChild(Js.cast(vueElement));
    vueComponentInstance.vue().$mount(vueElement);
  }
}
