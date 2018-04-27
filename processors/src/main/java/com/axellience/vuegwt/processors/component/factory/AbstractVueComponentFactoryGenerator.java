package com.axellience.vuegwt.processors.component.factory;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.annotations.component.JsComponent;
import com.axellience.vuegwt.core.client.component.IsVueComponent;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.vue.VueFactory;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwt.processors.utils.GeneratorsUtil;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Date;
import java.util.List;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.componentFactoryName;

/**
 * Abstract class to generate {@link VueFactory} from the user {@link IsVueComponent} classes.
 * It is inherited by {@link VueComponentFactoryGenerator} which generate for {@link Component}
 * annotated class and {@link VueJsComponentFactoryGenerator} which generate for {@link
 * JsComponent} annotated class.
 * @author Adrien Baron
 */
public abstract class AbstractVueComponentFactoryGenerator
{
    private static final String INSTANCE_PROP = "INSTANCE";

    private final Filer filer;
    final Messager messager;

    AbstractVueComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    /**
     * Generate our {@link VueFactory} class.
     * @param component The {@link IsVueComponent} class to generate {@link VueComponentOptions} from
     */
    public void generate(TypeElement component)
    {
        ClassName vueFactoryClassName = componentFactoryName(component);

        Builder vueFactoryBuilder = createFactoryBuilderClass(component, vueFactoryClassName);

        createProperties(vueFactoryClassName, vueFactoryBuilder);
        List<CodeBlock> staticInitParameters = createInitMethod(component, vueFactoryBuilder);
        createStaticGetMethod(vueFactoryClassName, vueFactoryBuilder, staticInitParameters);

        vueFactoryBuilder.addMethod(MethodSpec
            .constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Inject.class)
            .build());

        // Build the ComponentOptions class
        GeneratorsUtil.toJavaFile(filer, vueFactoryBuilder, vueFactoryClassName, component);
    }

    /**
     * Create the builder to build our {@link VueFactory} class.
     * @param component The Component we generate for
     * @param vueFactoryClassName The type name of our generated {@link VueFactory}
     * @return A Class Builder
     */
    private Builder createFactoryBuilderClass(TypeElement component, ClassName vueFactoryClassName)
    {
        return TypeSpec
            .classBuilder(vueFactoryClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(VueFactory.class),
                ClassName.get(component)))
            .addAnnotation(Singleton.class)
            .addJavadoc("VueFactory for Component {@link $L}",
                component.getQualifiedName().toString())
            .addAnnotation(AnnotationSpec
                .builder(Generated.class)
                .addMember("value", "$S", this.getClass().getCanonicalName())
                .addMember("date", "$S", new Date().toString())
                .addMember("comments", "$S", "https://github.com/Axellience/vue-gwt")
                .build());
    }

    /**
     * Create the method that init the wrapped {@link VueJsConstructor}
     * @param component The Component we generate for
     * @param vueFactoryClassBuilder The builder of the VueFactory we are generating
     * @return The list of parameters call to pass when call the created init method from the static
     * get method (when the Factory is not injected)
     */
    protected abstract List<CodeBlock> createInitMethod(TypeElement component,
        Builder vueFactoryClassBuilder);

    /**
     * Create the static properties used in our {@link VueFactory}.
     * @param vueFactoryType The generated type name for our {@link VueFactory}
     * @param vueFactoryBuilder The builder to create our {@link VueFactory} class
     */
    private void createProperties(ClassName vueFactoryType, Builder vueFactoryBuilder)
    {
        vueFactoryBuilder.addField(FieldSpec
            .builder(vueFactoryType, INSTANCE_PROP, Modifier.PRIVATE, Modifier.STATIC)
            .build());
    }

    /**
     * Create a static get method that can be used to retrieve an instance of our Factory.
     * Factory retrieved using this method do NOT support injection.
     * @param vueFactoryClassName The type name of our generated {@link VueFactory}
     * @param vueFactoryBuilder The builder to create our {@link VueFactory} class
     * @param staticInitParameters Parameters from the init method when called on static
     * initialization (when the factory is not injected)
     */
    private void createStaticGetMethod(ClassName vueFactoryClassName, Builder vueFactoryBuilder,
        List<CodeBlock> staticInitParameters)
    {
        MethodSpec.Builder getBuilder = MethodSpec
            .methodBuilder("get")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .returns(vueFactoryClassName);

        getBuilder.beginControlFlow("if ($L == null)", INSTANCE_PROP);

        getBuilder.addStatement("$L = new $T()", INSTANCE_PROP, vueFactoryClassName);
        getBuilder.addCode("$L.init(", INSTANCE_PROP);
        boolean isFirst = true;
        for (CodeBlock staticInitParameter : staticInitParameters)
        {
            if (!isFirst)
                getBuilder.addCode(", ");

            getBuilder.addCode(staticInitParameter);
            isFirst = false;
        }
        getBuilder.addCode(");");
        getBuilder.endControlFlow();

        getBuilder.addStatement("return $L", INSTANCE_PROP);

        vueFactoryBuilder.addMethod(getBuilder.build());
    }
}
