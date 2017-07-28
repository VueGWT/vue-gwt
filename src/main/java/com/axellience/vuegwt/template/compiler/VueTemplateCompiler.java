package com.axellience.vuegwt.template.compiler;

import com.coveo.nashorn_modules.Folder;
import com.coveo.nashorn_modules.Require;
import com.coveo.nashorn_modules.ResourceFolder;
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
    public static VueTemplateCompiler TEMPLATE_COMPILER = new VueTemplateCompiler();

    private final NashornScriptEngine engine;

    private VueTemplateCompiler()
    {
        engine = (NashornScriptEngine) new ScriptEngineManager().getEngineByName("nashorn");

        Folder folder = ResourceFolder.create(getClass().getClassLoader(),
            "com/axellience/vuegwt/template/compiler",
            "UTF-8");
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
