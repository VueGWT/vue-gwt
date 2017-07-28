package com.axellience.vuegwt.template.compiler;

import com.coveo.nashorn_modules.Folder;
import com.google.gwt.dev.resource.Resource;
import com.google.gwt.dev.resource.ResourceOracle;
import com.google.gwt.dev.util.Util;

/**
 * @author Adrien Baron
 */
public class GwtResourceFolder implements Folder
{
    private final String path;
    private final ResourceOracle resourceOracle;

    private Folder parent;

    public GwtResourceFolder(ResourceOracle resourceOracle, String path)
    {
        this(resourceOracle, path, null);
    }

    private GwtResourceFolder(ResourceOracle resourceOracle, String path, Folder parent)
    {
        this.resourceOracle = resourceOracle;
        this.path = path;
        this.parent = parent;
    }

    private GwtResourceFolder(ResourceOracle resourceOracle, Folder parent, String name)
    {
        this(resourceOracle, parent.getPath() + "/" + name);
        this.parent = parent;
    }

    @Override
    public Folder getParent()
    {
        return parent;
    }

    @Override
    public String getPath()
    {
        return path;
    }

    @Override
    public String getFile(String name)
    {
        Resource resource = resourceOracle.getResource(getPath() + "/" + name);
        if (resource == null)
            return null;

        return Util.readURLAsString(resource.getURL());
    }

    @Override
    public Folder getFolder(String name)
    {
        return new GwtResourceFolder(resourceOracle, this, name);
    }
}
