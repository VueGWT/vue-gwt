package com.axellience.vuegwt.jsr69.directive;

import com.axellience.vuegwt.client.directive.VueDirective;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.jsr69.GenerationNameUtil;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.squareup.javapoet.ClassName;
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
 * Generate {@link VueDirectiveOptions} from the user {@link VueDirective} classes
 * @author Adrien Baron
 */
public class VueDirectiveOptionsGenerator
{
    private final Elements elementsUtils;
    private final Filer filer;

    public VueDirectiveOptionsGenerator(ProcessingEnvironment processingEnv)
    {
        elementsUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    /**
     * Generate and save the Java file for the typeElement passed to the constructor
     * @param directiveTypeElement The {@link VueDirective} class to
     * generate {@link VueDirectiveOptions} from
     */
    public void generate(TypeElement directiveTypeElement)
    {
        ClassName optionsClassName = GenerationNameUtil.directiveOptionsName(directiveTypeElement);

        Builder componentClassBuilder = TypeSpec
            .classBuilder(optionsClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(VueDirectiveOptions.class)
            .addAnnotation(JsType.class)
            .addJavadoc("VueComponent Directive Options for directive {@link $S}",
                directiveTypeElement.getQualifiedName().toString());

        // Initialize constructor
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC);

        // Add the Java Component Instance initialization
        constructorBuilder.addStatement("this.$L = new $T()",
            "vuegwt$javaDirectiveInstance",
            TypeName.get(directiveTypeElement.asType()));

        // Call the method to copy hooks functions
        constructorBuilder.addStatement("this.copyHooks()");

        // Finish building the constructor
        componentClassBuilder.addMethod(constructorBuilder.build());

        // Build the DirectiveOptions class
        GenerationUtil.toJavaFile(filer,
            componentClassBuilder,
            optionsClassName,
            directiveTypeElement);
    }
}
