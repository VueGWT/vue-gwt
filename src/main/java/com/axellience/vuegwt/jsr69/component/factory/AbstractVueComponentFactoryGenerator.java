package com.axellience.vuegwt.jsr69.component.factory;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.jsr69.GenerationNameUtil;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Abstract class to generate {@link VueFactory} from the user Vue Component classes.
 * It is inherited by {@link VueComponentFactoryGenerator} which generate for {@link Component}
 * annotated class and {@link VueJsComponentFactoryGenerator} which generate for {@link
 * JsComponent} annotated class.
 * @author Adrien Baron
 */
public abstract class AbstractVueComponentFactoryGenerator
{
    protected static String INSTANCE_PROP = "INSTANCE";

    private final Filer filer;

    AbstractVueComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate our {@link VueFactory} class.
     * @param component The {@link VueComponent} class to generate {@link VueComponentOptions} from
     */
    public void generate(TypeElement component)
    {
        ClassName vueFactoryClassName = GenerationNameUtil.componentFactoryName(component);

        Builder vueFactoryBuilder = createFactoryBuilderClass(component, vueFactoryClassName);

        createProperties(vueFactoryClassName, vueFactoryBuilder);
        createConstructor(component, vueFactoryClassName, vueFactoryBuilder);
        createGetMethod(component, vueFactoryClassName, vueFactoryBuilder);

        // Build the ComponentOptions class
        GenerationUtil.toJavaFile(filer, vueFactoryBuilder, vueFactoryClassName, component);
    }

    /**
     * Create the builder to build our {@link VueFactory} class.
     * @param component The Component we generate for
     * @param vueFactoryClassName The type name of our generated {@link VueFactory}
     * @return A Class Builder
     */
    protected Builder createFactoryBuilderClass(TypeElement component,
        ClassName vueFactoryClassName)
    {
        return TypeSpec
            .classBuilder(vueFactoryClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(VueFactory.class),
                ClassName.get(component)))
            .addAnnotation(Singleton.class)
            .addJavadoc("VueFactory for Component {@link $S}",
                component.getQualifiedName().toString());
    }

    /**
     * Create the createInstance method. This method will return a new instance of our generated
     * {@link VueFactory} while making sure dependencies are injected.
     * @param component The Component we generate for
     * @param vueFactoryType The generated type name for our {@link VueFactory}
     * @param vueFactoryBuilder The builder to create our {@link VueFactory} class
     */
    protected abstract void createConstructor(TypeElement component, ClassName vueFactoryType,
        Builder vueFactoryBuilder);

    /**
     * Should be called to customize the static instance once created
     */
    protected abstract void configureStaticInstance(TypeElement component,
        MethodSpec.Builder getBuilder);

    /**
     * Create the static properties used in our {@link VueFactory}.
     * @param vueFactoryType The generated type name for our {@link VueFactory}
     * @param vueFactoryBuilder The builder to create our {@link VueFactory} class
     */
    protected void createProperties(ClassName vueFactoryType, Builder vueFactoryBuilder)
    {
        vueFactoryBuilder.addField(FieldSpec
            .builder(vueFactoryType, INSTANCE_PROP, Modifier.PRIVATE, Modifier.STATIC)
            .build());
    }

    private void createGetMethod(TypeElement component, ClassName vueFactoryClassName,
        Builder vueFactoryClassBuilder)
    {
        MethodSpec.Builder getBuilder = MethodSpec
            .methodBuilder("get")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .returns(vueFactoryClassName)
            .beginControlFlow("if ($L == null)", INSTANCE_PROP)
            .addStatement("$L = new $T()", INSTANCE_PROP, vueFactoryClassName);

        configureStaticInstance(component, getBuilder);

        getBuilder.endControlFlow().addStatement("return $L", INSTANCE_PROP);

        vueFactoryClassBuilder.addMethod(getBuilder.build());
    }
}
