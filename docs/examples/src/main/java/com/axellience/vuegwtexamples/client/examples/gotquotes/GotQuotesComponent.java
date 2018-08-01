package com.axellience.vuegwtexamples.client.examples.gotquotes;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.Data;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.hooks.HasCreated;
import javax.inject.Inject;
import jsinterop.annotations.JsMethod;

@Component
public class GotQuotesComponent implements IsVueComponent, HasCreated {

  @Data
  GotQuote quote;

  @Inject
  GotQuotesService gotQuotesService;

  @Override
  public void created() {
    changeQuote();
  }

  @JsMethod
  protected void changeQuote() {
    quote = gotQuotesService.getRandomQuote();
  }
}
