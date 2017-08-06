package com.axellience.vuegwt.jsr69.component.factory;

import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Generate {@link VueFactory} from the user Vue Component classes annotated by {@link
 * JsComponent}.
 * @author Adrien Baron
 */
public class VueJsComponentFactoryGenerator extends AbstractVueComponentFactoryGenerator
{
    public VueJsComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);
    }

    @Override
    protected void createConstructor(TypeElement component, ClassName vueFactoryType,
        Builder vueFactoryBuilder)
    {
        JsType jsType = component.getAnnotation(JsType.class);
        if (jsType == null || !jsType.isNative())
            throw new RuntimeException(component.asType().toString()
                + " Js Component must have @JsType annotation with isNative to true.");

        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);

        String namespace = jsType.namespace();
        if (JsPackage.GLOBAL.equals(namespace))
            namespace = "";
        else if ("<auto>".equals(namespace))
            namespace = vueFactoryType.packageName() + ".";
        else
            namespace += ".";

        String name = jsType.name();
        if ("<auto>".equals(name))
            name = component.getSimpleName().toString();

        // Static init block
        constructorBuilder.addStatement("jsConstructor = ($T) $T.getDeepValue($T.getWindow(), $S)",
            ParameterizedTypeName.get(ClassName.get(VueJsConstructor.class),
                ClassName.get(component.asType())),
            JsTools.class,
            JsTools.class,
            namespace + name);

        vueFactoryBuilder.addMethod(constructorBuilder.build());
    }

    @Override
    protected void configureStaticInstance(TypeElement component, MethodSpec.Builder getBuilder)
    {

    }
}
