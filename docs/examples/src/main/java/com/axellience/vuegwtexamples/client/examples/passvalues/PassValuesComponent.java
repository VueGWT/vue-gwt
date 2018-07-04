package com.axellience.vuegwtexamples.client.examples.passvalues;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Prop;
import com.axellience.vuegwt.core.client.component.IsVueComponent;

@Component
public class PassValuesComponent implements IsVueComponent {

  @Prop
  int myInt;

  @Prop
  boolean myBool;

  @Prop
  double myDouble;

  @Prop
  float myFloat;

  @Prop
  long myLong;

  @Prop
  Integer wrappedInteger;

  @Prop
  Boolean wrappedBoolean;

  @Prop
  Double wrappedDouble;

  @Prop
  Float wrappedFloat;

  @Prop
  Long wrappedLong;
}
