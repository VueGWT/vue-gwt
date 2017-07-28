package com.axellience.vuegwt.template.compiler;

import com.coveo.nashorn_modules.Folder;
import com.coveo.nashorn_modules.Require;
import com.google.gwt.dev.resource.ResourceOracle;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Compile an HTML Vue template to JS render function using Nashorn and the vue-template-compiler.
 * @author Adrien Baron
 */
public class VueTemplateCompiler
{
    private static NashornScriptEngine engine;

    public VueTemplateCompiler(ResourceOracle resourceOracle)
    {
        // Engine is cached between instance to avoid creating at each compilation
        if (engine == null)
        {
            initEngine(resourceOracle);
        }
    }

    /**
     * Init the Nashorn engine and load the Vue compiler in it.
     * @param resourceOracle The GWT resource Oracle to access JS files
     */
    private void initEngine(ResourceOracle resourceOracle)
    {
        engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

        // Resources are in the "client" folder to be included during GWT compilation
        Folder folder =
            new GwtResourceFolder(resourceOracle, "com/axellience/vuegwt/client/template/compiler");
        try
        {
            Require.enable(engine, folder);
            engine.eval(folder.getFile("index.js"));
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Compile the given HTML template to JS functions using vue-template-compiler.
     * @param htmlTemplate The HTML Component template to compile
     * @return An object containing the render functions
     * @throws VueTemplateCompilerException If the compilation fails
     */
    public VueTemplateCompilerResult compile(String htmlTemplate)
    throws VueTemplateCompilerException
    {
        ScriptObjectMirror templateCompilerResult;
        try
        {
            templateCompilerResult =
                (ScriptObjectMirror) engine.invokeFunction("compile", htmlTemplate);
        }
        catch (ScriptException | NoSuchMethodException e)
        {
            e.printStackTrace();
            throw new VueTemplateCompilerException(
                "An error occurred while compiling the template: " + htmlTemplate);
        }

        String renderFunction = (String) templateCompilerResult.get("render");
        String[] staticRenderFunctions =
            ((ScriptObjectMirror) templateCompilerResult.get("staticRenderFns")).to(String[].class);

        return new VueTemplateCompilerResult(renderFunction, staticRenderFunctions);
    }
}
