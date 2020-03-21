package com.axellience.vuegwt.testsapp.j2cl;

import com.axellience.vuegwt.core.client.VueGWT;
import com.axellience.vuegwt.testsapp.client.VueGwtTestApp;

public class VueGwtTestsAppJ2CL {

  public void onModuleLoad() {
    VueGWT.init();
    VueGwtTestsApp.bootstrap();
  }
}
