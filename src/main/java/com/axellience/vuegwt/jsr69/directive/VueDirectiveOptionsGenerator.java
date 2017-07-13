package com.axellience.vuegwt.jsr69.directive;

import com.axellience.vuegwt.client.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.options.VueOptionsCache;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Generate VueDirectiveOptions from the user VueDirective classes
 * @author Adrien Baron
 */
public class VueDirectiveOptionsGenerator
{
    private static String DIRECTIVE_OPTIONS_SUFFIX = "_Options";
    private static String JDI = "vuegwt$javaDirectiveInstance";

    private final Elements elementsUtils;
    private final Filer filer;

    public VueDirectiveOptionsGenerator(ProcessingEnvironment processingEnv)
    {
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * Generate and save the Java file for the typeElement passed to the constructor
     * @param directiveTypeElement The {@link com.axellience.vuegwt.client.VueDirective} class to
     * generate {@link VueDirectiveOptions} from
     */
    public void generate(TypeElement directiveTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(directiveTypeElement).getQualifiedName().toString();
        String typeName = directiveTypeElement.getSimpleName().toString();
        String generatedTypeName = typeName + DIRECTIVE_OPTIONS_SUFFIX;

        Builder componentClassBuilder = TypeSpec
            .classBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(VueDirectiveOptions.class)
            .addAnnotation(JsType.class)
            .addJavadoc("Vue Directive Options for directive {@link $S}",
                directiveTypeElement.getQualifiedName().toString());

        // Static init block
        componentClassBuilder.addStaticBlock(CodeBlock.of(
            "$T.registerDirectiveOptions($T.class, new $L());",
            VueOptionsCache.class,
            TypeName.get(directiveTypeElement.asType()),
            generatedTypeName));

        // Initialize constructor
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add the Java Component Instance initialization
        constructorBuilder.addStatement("this.$L = new $T()",
            JDI,
            TypeName.get(directiveTypeElement.asType()));

        // Call the method to copy hooks functions
        constructorBuilder.addStatement("this.copyHooks()");

        // Finish building the constructor
        componentClassBuilder.addMethod(constructorBuilder.build());

        // Build the DirectiveOptions class
        GenerationUtil.toJavaFile(filer,
            componentClassBuilder,
            packageName,
            generatedTypeName,
            directiveTypeElement);
    }
}
