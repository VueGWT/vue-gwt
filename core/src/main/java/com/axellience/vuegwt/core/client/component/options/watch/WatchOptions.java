package com.axellience.vuegwt.core.client.component.options.watch;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.JsPropertyMap;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "Object")
public class WatchOptions implements JsPropertyMap<Boolean> {

  @JsOverlay
  public static WatchOptions of(boolean isDeep, boolean isImmediate) {
    WatchOptions watchOptions = new WatchOptions();
    watchOptions.setIsDeep(isDeep);
    watchOptions.setIsImmediate(isImmediate);
    return watchOptions;
  }

  @JsOverlay
  public final boolean isDeep() {
    return get("deep");
  }

  @JsOverlay
  public final boolean isImmediate() {
    return get("immediate");
  }

  @JsOverlay
  public final void setIsDeep(boolean isDeep) {
    set("deep", isDeep);
  }

  @JsOverlay
  public final void setIsImmediate(boolean isImmediate) {
    set("immediate", isImmediate);
  }
}
