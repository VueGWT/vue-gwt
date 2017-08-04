package com.axellience.vuegwt.jsr69.component.constructor;

import com.axellience.vuegwt.client.tools.JsTools;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Generate {@link VueConstructor} from the user Vue Component classes annotated by {@link
 * JsComponent}.
 * @author Adrien Baron
 */
public class VueJsComponentConstructorGenerator extends AbstractVueComponentConstructorGenerator
{
    public VueJsComponentConstructorGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);
    }

    @Override
    protected void createCreateInstanceMethod(TypeElement component,
        String packageName, String className, TypeName vueConstructorType,
        Builder vueConstructorBuilder)
    {
        JsType jsType = component.getAnnotation(JsType.class);
        if (jsType == null || !jsType.isNative())
            throw new RuntimeException(component.asType().toString()
                + " Js Component must have @JsType annotation with isNative to true.");

        MethodSpec.Builder createInstanceBuilder = MethodSpec
            .methodBuilder("createInstance")
            .addAnnotation(JsOverlay.class)
            .addModifiers(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL)
            .returns(vueConstructorType);

        String namespace = jsType.namespace();
        if (JsPackage.GLOBAL.equals(namespace))
            namespace = "";
        else if ("<auto>".equals(namespace))
            namespace = packageName + ".";
        else
            namespace += ".";

        String name = jsType.name();
        if ("<auto>".equals(name))
            name = component.getSimpleName().toString();

        // Static init block
        createInstanceBuilder.addStatement("return ($T) $T.getDeepValue($T.getWindow(), $S)",
            vueConstructorType,
            JsTools.class,
            JsTools.class,
            namespace + name);

        vueConstructorBuilder.addMethod(createInstanceBuilder.build());
    }
}
