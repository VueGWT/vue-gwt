package com.axellience.vuegwt.jsr69.component.constructor.jstype;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Generate {@link VueConstructor} from the user Vue Component classes annotated by {@link
 * Component}.
 * @author Adrien Baron
 */
public class VueComponentJsTypeGenerator
{
    private final String JS_TYPE_SUFFIX = "JsType";
    private final Elements elementsUtils;
    private final Filer filer;

    public VueComponentJsTypeGenerator(ProcessingEnvironment processingEnv)
    {
        this.elementsUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate a class extending our {@link VueComponent} class.
     * This will allow us to expose it's constructor to be used.
     * @param componentTypeElement The {@link VueComponent} to extend
     */
    public void generate(TypeElement componentTypeElement)
    {
        String packageName =
            elementsUtils.getPackageOf(componentTypeElement).getQualifiedName().toString();
        String className = componentTypeElement.getSimpleName().toString();
        String generatedClassName = className + JS_TYPE_SUFFIX;

        Builder vueComponentJsTypeClassBuilder = TypeSpec
            .classBuilder(generatedClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ClassName.get(componentTypeElement))
            .addAnnotation(AnnotationSpec
                .builder(JsType.class)
                .addMember("namespace", "\"VueGWT.javaComponentConstructors\"")
                .addMember("name",
                    "\""
                        + componentTypeElement.getQualifiedName().toString().replaceAll("\\.", "_")
                        + "\"")
                .build());

        vueComponentJsTypeClassBuilder.addMethod(MethodSpec
            .constructorBuilder()
            .addStatement("super()")
            .addModifiers(Modifier.PUBLIC)
            .build());

        GenerationUtil.toJavaFile(filer,
            vueComponentJsTypeClassBuilder,
            packageName,
            generatedClassName,
            componentTypeElement);
    }
}
