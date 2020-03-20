package com.axellience.vuegwt.testsappgwt2.client;

import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.testsapp.client.VueGwtTestApp;
import com.google.gwt.core.client.EntryPoint;

public class VueGwtTestsAppGwt2 implements EntryPoint {

  @Override
  public void onModuleLoad() {
    VueGWT.init();
    VueGwtTestApp.bootstrap();
  }

}
