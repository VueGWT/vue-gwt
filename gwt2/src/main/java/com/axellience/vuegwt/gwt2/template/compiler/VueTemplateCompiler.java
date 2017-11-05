package com.axellience.vuegwt.gwt2.template.compiler;

import com.coveo.nashorn_modules.Folder;
import com.coveo.nashorn_modules.Require;
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

    /**
     * @param templateCompilerResourceFolder A Folder containing the Vue template compiler script
     */
    public VueTemplateCompiler(Folder templateCompilerResourceFolder)
    {
        // Engine is cached between instance to avoid creating at each compilation
        if (engine == null)
        {
            initEngine(templateCompilerResourceFolder);
        }
    }

    /**
     * Init the Nashorn engine and load the Vue compiler in it.
     * @param templateCompilerResourceFolder A Folder containing the Vue template compiler script
     */
    private void initEngine(Folder templateCompilerResourceFolder)
    {
        engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

        try
        {
            Require.enable(engine, templateCompilerResourceFolder);
            engine.eval(templateCompilerResourceFolder.getFile("index.js"));
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
