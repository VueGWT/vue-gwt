package com.axellience.vuegwt.jsr69;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Adrien Baron
 */
public class GenerationUtil
{
    public static void toJavaFile(Filer filer, Builder classBuilder, String packageName,
        String className, TypeElement... originatingElement)
    {
        try
        {
            JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();

            JavaFileObject javaFileObject =
                filer.createSourceFile(packageName + "." + className, originatingElement);

            Writer writer = javaFileObject.openWriter();
            javaFile.writeTo(writer);
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
