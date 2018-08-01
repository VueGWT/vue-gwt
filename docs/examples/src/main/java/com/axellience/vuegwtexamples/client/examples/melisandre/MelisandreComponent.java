package com.axellience.vuegwtexamples.client.examples.melisandre;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Computed;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import jsinterop.annotations.JsMethod;

/**
 * @author Adrien Baron
 */
@Component
public class MelisandreComponent implements IsVueComponent {

  @Data
  boolean isRed = true;

  @Computed
  public MelisandreComponentStyle getMyStyle() {
    return MelisandreComponentClientBundle.INSTANCE.melisandreComponentStyle();
  }

  @JsMethod
  public void setRed(boolean red) {
    isRed = red;
  }
}