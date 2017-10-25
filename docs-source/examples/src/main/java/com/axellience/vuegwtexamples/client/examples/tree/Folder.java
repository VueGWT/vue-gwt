package com.axellience.vuegwtexamples.client.examples.tree;

import elemental2.core.Array;

/**
 * @author Adrien Baron
 */
public class Folder
{
    private String name;
    private final Array<Folder> content;

    public Folder(String name)
    {
        this.name = name;
        this.content = new Array<>();
    }

    public boolean hasContent()
    {
        return content.length > 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Array<Folder> getContent()
    {
        return content;
    }
}
