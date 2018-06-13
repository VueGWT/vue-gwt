package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwtexamples.client.examples.gotquotes.GotQuotesComponentFactory;
import dagger.Component;
import javax.inject.Singleton;

@Component
@Singleton
public interface ExampleInjector {

  GotQuotesComponentFactory gotQuoteComponentFactory();
}