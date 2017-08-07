package com.axellience.vuegwtexamples.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.inject.Named;

public class ExampleGinModule extends AbstractGinModule
{
    @Override
    protected void configure()
    {
    }

    @Provides
    @Singleton
    @Named("apiUrl")
    protected String getApiUrl()
    {
        // TODO: change the URL in function of the current environment
        return "https://localhost:8443/gmmapi";
    }
}
