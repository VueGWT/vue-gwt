package com.axellience.vuegwt.processors.component.factory;

import com.axellience.vuegwt.core.annotations.component.JsComponent;
import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.client.vue.VueFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.util.LinkedList;
import java.util.List;

import static com.axellience.vuegwt.core.generation.GenerationNameUtil.getPackage;

/**
 * Generate {@link VueFactory} from the user {@link VueComponent} classes annotated by {@link
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
    protected List<CodeBlock> createInitMethod(TypeElement component, Builder vueFactoryBuilder)
    {
        JsType jsType = component.getAnnotation(JsType.class);
        if (jsType == null || !jsType.isNative())
        {
            messager.printMessage(Kind.ERROR,
                component.asType().toString()
                    + " Js Component must have @JsType annotation with isNative to true.");
        }

        MethodSpec.Builder initBuilder =
            MethodSpec.methodBuilder("init").addModifiers(Modifier.PRIVATE);

        String namespace = jsType.namespace();
        if (JsPackage.GLOBAL.equals(namespace))
            namespace = "";
        else if ("<auto>".equals(namespace))
            namespace = getPackage(component) + ".";
        else
            namespace += ".";

        String name = jsType.name();
        if ("<auto>".equals(name))
            name = component.getSimpleName().toString();

        // Static init block
        initBuilder.addStatement("jsConstructor = ($T) $T.getDeepValue($T.window, $S)",
            ParameterizedTypeName.get(ClassName.get(VueJsConstructor.class),
                ClassName.get(component.asType())),
            VueGWTTools.class,
            DomGlobal.class,
            namespace + name);

        vueFactoryBuilder.addMethod(initBuilder.build());
        return new LinkedList<>();
    }
}
