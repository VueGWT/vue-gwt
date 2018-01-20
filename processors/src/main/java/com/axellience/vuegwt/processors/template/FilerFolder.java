package com.axellience.vuegwt.processors.template;

import com.coveo.nashorn_modules.AbstractFolder;
import com.coveo.nashorn_modules.Folder;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

public class FilerFolder extends AbstractFolder
{
    private Filer filer;
    private String resourcePath;

    @Override
    public String getFile(String name)
    {
        FileObject fileObject;
        try
        {
            fileObject =
                filer.getResource(StandardLocation.CLASS_PATH, "", resourcePath + "/" + name);
            return fileObject.getCharContent(true).toString();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public Folder getFolder(String name)
    {
        return new FilerFolder(filer, resourcePath + "/" + name, this, getPath() + name + "/");
    }

    private FilerFolder(Filer filer, String resourcePath, Folder parent, String displayPath)
    {
        super(parent, displayPath);
        this.filer = filer;
        this.resourcePath = resourcePath;
    }

    public static FilerFolder create(Filer filer, String path)
    {
        return new FilerFolder(filer, path, null, "/");
    }
}
