package com.axellience.vuegwt.processors.component.factory;

import com.axellience.vuegwt.core.annotations.component.JsComponent;
import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.tools.VueGWTTools;
import com.axellience.vuegwt.core.client.vue.VueComponentFactory;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import elemental2.dom.DomGlobal;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import java.util.LinkedList;
import java.util.List;

/**
 * Generate {@link VueComponentFactory} from the user {@link IsVueComponent} classes
 * annotated by {@link
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
                    + " @JsComponent must have a @JsType annotation with isNative to true");
            return null;
        }
        if (!JsPackage.GLOBAL.equals(jsType.namespace()))
        {
            messager.printMessage(Kind.ERROR,
                component.asType().toString()
                    + " @JsType annotation on @JsComponent must have namespace set to JsPackage.GLOBAL");
            return null;
        }

        JsComponent jsComponent = component.getAnnotation(JsComponent.class);

        MethodSpec.Builder initBuilder = MethodSpec
            .methodBuilder("init")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Inject.class);

        if ("Function".equals(jsType.name()))
        {
            initForComponentConstructor(jsComponent, initBuilder);
        }
        else if ("Object".equals(jsType.name()))
        {
            initForComponentOptions(jsComponent, initBuilder);
        }
        else
        {
            messager.printMessage(Kind.ERROR,
                component.asType().toString()
                    + " @JsType annotation on @JsComponent must have name set to either Function or Object.");
        }

        vueFactoryBuilder.addMethod(initBuilder.build());
        return new LinkedList<>();
    }

    private void initForComponentOptions(JsComponent jsComponent, MethodSpec.Builder initBuilder)
    {
        initBuilder.addStatement(
            "jsConstructor = $T.extend($T.cast($T.getDeepValue($T.window, $S)))",
            Vue.class,
            Js.class,
            VueGWTTools.class,
            DomGlobal.class,
            jsComponent.value());
    }

    private void initForComponentConstructor(JsComponent jsComponent,
        MethodSpec.Builder initBuilder)
    {
        initBuilder.addStatement("jsConstructor = $T.cast($T.getDeepValue($T.window, $S))",
            Js.class,
            VueGWTTools.class,
            DomGlobal.class,
            jsComponent.value());
    }
}
