package com.axellience.vuegwtexamples.client;

import com.axellience.vuegwtexamples.client.examples.recursive.RecursiveComponentFactory;
import com.axellience.vuegwtexamples.client.examples.tree.TreeComponentFactory;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(ExampleGinModule.class)
public interface ExampleInjector extends Ginjector
{
    RecursiveComponentFactory recursiveComponentFactory();
    TreeComponentFactory treeComponentFactory();
}