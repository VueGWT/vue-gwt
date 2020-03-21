package com.axellience.vuegwt.testsapp.gwt2.client;

import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.testsapp.client.VueGwtTestsApp;
import com.google.gwt.core.client.EntryPoint;

public class VueGwtTestsAppGwt2 implements EntryPoint {

  @Override
  public void onModuleLoad() {
    VueGWT.init();
    VueGwtTestsApp.bootstrap();
  }

}
