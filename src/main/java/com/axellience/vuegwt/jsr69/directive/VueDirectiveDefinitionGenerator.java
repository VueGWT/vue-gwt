package com.axellience.vuegwt.jsr69.directive;

import com.axellience.vuegwt.client.definitions.VueDefinitionCache;
import com.axellience.vuegwt.client.definitions.VueDirectiveDefinition;
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
 * Generate VueComponentDefinitions from the user VueComponent classes
 * @author Adrien Baron
 */
public class VueDirectiveDefinitionGenerator
{
    private static String DIRECTIVE_DEFINITION_SUFFIX = "_DirectiveDefinition";
    private static String JDI = "vuegwt$javaDirectiveInstance";

    private final Elements elementsUtils;
    private final Filer filer;

    public VueDirectiveDefinitionGenerator(ProcessingEnvironment processingEnv)
    {
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * Generate and save the Java file for the typeElement passed to the constructor
     */
    public void generate(TypeElement directiveTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(directiveTypeElement).getQualifiedName().toString();
        String typeName = directiveTypeElement.getSimpleName().toString();
        String generatedTypeName = typeName + DIRECTIVE_DEFINITION_SUFFIX;

        Builder componentClassBuilder = TypeSpec
            .classBuilder(generatedTypeName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(VueDirectiveDefinition.class)
            .addAnnotation(JsType.class)
            .addJavadoc("Vue Directive Definition for directive {@link $S}",
                directiveTypeElement.getQualifiedName().toString());

        // Static init block
        componentClassBuilder.addStaticBlock(CodeBlock.of(
            "$T.registerDirective($T.class, new $L());",
            VueDefinitionCache.class,
            TypeName.get(directiveTypeElement.asType()),
            generatedTypeName));

        // Initialize constructor
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add the Java Component Instance initialization
        constructorBuilder.addStatement("this.$L = new $T()", JDI,
            TypeName.get(directiveTypeElement.asType()));

        // Call the method to copy hooks functions
        constructorBuilder.addStatement("this.copyHooks()");

        // Finish building the constructor and add to the component definition
        componentClassBuilder.addMethod(constructorBuilder.build());

        // Build the component definition class
        GenerationUtil.toJavaFile(filer,
            componentClassBuilder,
            packageName,
            generatedTypeName,
            directiveTypeElement);
    }
}
