package com.axellience.vuegwt.jsr69.component.constructor;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwt.jsr69.GenerationUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.JsComponent;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.function.Supplier;

/**
 * Abstract class to generate {@link VueConstructor} from the user Vue Component classes.
 * It is inherited by {@link VueComponentConstructorGenerator} which generate for {@link Component}
 * annotated class and {@link VueJsComponentConstructorGenerator} which generate for {@link
 * JsComponent} annotated class.
 * @author Adrien Baron
 */
public abstract class AbstractVueComponentConstructorGenerator
{
    public static String CONSTRUCTOR_SUFFIX = "Constructor";
    
    private static String INSTANCE_PROP = "constructor";
    private static String SUPPLIER_PROP = "supplier";

    private final Elements elementsUtils;
    private final Filer filer;

    AbstractVueComponentConstructorGenerator(ProcessingEnvironment processingEnv)
    {
        this.elementsUtils = processingEnv.getElementUtils();
        this.filer = processingEnv.getFiler();
    }

    /**
     * Generate our {@link VueConstructor} class.
     * @param component The {@link VueComponent} class to generate {@link VueComponentOptions} from
     */
    public void generate(TypeElement component)
    {
        String packageName = elementsUtils.getPackageOf(component).getQualifiedName().toString();
        String className = component.getSimpleName().toString();
        String vueConstructorClassName = className + CONSTRUCTOR_SUFFIX;
        TypeName vueConstructorType = ClassName.get(packageName, vueConstructorClassName);

        Builder vueConstructorBuilder =
            createConstructorBuilderClass(component, vueConstructorClassName);

        createProperties(vueConstructorType, vueConstructorBuilder);

        createGetSupplierMethod(vueConstructorType, vueConstructorBuilder);
        createGetMethod(vueConstructorType, vueConstructorBuilder);

        createCreateInstanceMethod(component,
            packageName,
            className,
            vueConstructorType,
            vueConstructorBuilder);

        // Build the ComponentOptions class
        GenerationUtil.toJavaFile(filer,
            vueConstructorBuilder,
            packageName,
            vueConstructorClassName,
            component);
    }

    /**
     * Create the builder to build our {@link VueConstructor} class.
     * @param component The Component we generate for
     * @param vueConstructorClassName The type name of our generated {@link VueConstructor}
     * @return A Class Builder
     */
    protected Builder createConstructorBuilderClass(TypeElement component,
        String vueConstructorClassName)
    {
        return TypeSpec
            .classBuilder(vueConstructorClassName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .superclass(ParameterizedTypeName.get(ClassName.get(VueConstructor.class),
                ClassName.get(component)))
            .addAnnotation(AnnotationSpec
                .builder(JsType.class)
                .addMember("isNative", "true", true)
                .addMember("namespace", "\"" + JsPackage.GLOBAL + "\"")
                .addMember("name", "\"Function\"")
                .build())
            .addJavadoc("VueConstructor for Component {@link $S}",
                component.getQualifiedName().toString());
    }

    /**
     * Create the createInstance method. This method will return a new instance of our generated
     * {@link VueConstructor} while making sure dependencies are injected.
     * @param component The Component we generate for
     * @param packageName The name of the package this Component is in
     * @param className The name of our Component class
     * @param vueConstructorType The generated type name for our {@link VueConstructor}
     * @param vueConstructorBuilder The builder to create our {@link VueConstructor} class
     */
    protected abstract void createCreateInstanceMethod(TypeElement component, String packageName,
        String className, TypeName vueConstructorType, Builder vueConstructorBuilder);

    /**
     * Create the static properties used in our {@link VueConstructor}.
     * @param vueConstructorType The generated type name for our {@link VueConstructor}
     * @param vueConstructorBuilder The builder to create our {@link VueConstructor} class
     */
    protected void createProperties(TypeName vueConstructorType, Builder vueConstructorBuilder)
    {
        vueConstructorBuilder.addField(FieldSpec
            .builder(ParameterizedTypeName.get(ClassName.get(Supplier.class), vueConstructorType),
                SUPPLIER_PROP,
                Modifier.PRIVATE,
                Modifier.STATIC)
            .addAnnotation(JsOverlay.class)
            .build());

        vueConstructorBuilder.addField(FieldSpec
            .builder(vueConstructorType, INSTANCE_PROP, Modifier.PRIVATE, Modifier.STATIC)
            .addAnnotation(JsOverlay.class)
            .build());
    }

    /**
     * Create the method that create the Supplier for our VueConstructor.
     * This Supplier is used to retrieve the instance of our VueConstructor.
     * It is registered in VueGWT on application start.
     * @param vueConstructorType The generated type name for our {@link VueConstructor}
     * @param vueConstructorBuilder The builder to create our {@link VueConstructor} class
     */
    private void createGetSupplierMethod(TypeName vueConstructorType, Builder vueConstructorBuilder)
    {
        MethodSpec.Builder getBuilder = MethodSpec
            .methodBuilder("getSupplier")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .addAnnotation(JsOverlay.class)
            .returns(ParameterizedTypeName.get(ClassName.get(Supplier.class), vueConstructorType))
            .beginControlFlow("if ($L == null)", SUPPLIER_PROP)
            .addStatement("$L = () -> get()", SUPPLIER_PROP)
            .endControlFlow()
            .addStatement("return $L", SUPPLIER_PROP);

        vueConstructorBuilder.addMethod(getBuilder.build());
    }

    private void createGetMethod(TypeName generatedTypeName, Builder vueConstructorClassBuilder)
    {
        MethodSpec.Builder getBuilder = MethodSpec
            .methodBuilder("get")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
            .addAnnotation(JsOverlay.class)
            .returns(generatedTypeName)
            .beginControlFlow("if ($L == null)", INSTANCE_PROP)
            .addStatement("$L = createInstance()", INSTANCE_PROP)
            .endControlFlow()
            .addStatement("return $L", INSTANCE_PROP);

        vueConstructorClassBuilder.addMethod(getBuilder.build());
    }
}
