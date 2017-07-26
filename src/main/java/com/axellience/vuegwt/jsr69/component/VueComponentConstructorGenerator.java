package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.VueGWT;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.tools.VueGWTTools;
import com.axellience.vuegwt.client.vue.VueConstructor;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.annotations.JsOverlay;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import java.util.List;
import java.util.stream.Stream;

import static com.axellience.vuegwt.jsr69.component.VueComponentOptionsGenerator.COMPONENT_OPTIONS_SUFFIX;
import static com.axellience.vuegwt.jsr69.directive.VueDirectiveOptionsGenerator.DIRECTIVE_OPTIONS_SUFFIX;

/**
 * Generate {@link VueConstructor} from the user Vue Component classes annotated by {@link
 * Component}.
 * @author Adrien Baron
 */
public class VueComponentConstructorGenerator extends AbstractVueComponentConstructorGenerator
{
    public VueComponentConstructorGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);
    }

    @Override
    protected void createStaticRegistration(TypeElement componentTypeElement,
        TypeName generatedTypeName, Builder vueConstructorClassBuilder)
    {
        vueConstructorClassBuilder.addStaticBlock(CodeBlock
            .builder()
            .addStatement("$T.register($S, $T.get())",
                VueGWT.class,
                componentTypeElement.getQualifiedName().toString(),
                generatedTypeName)
            .build());
    }

    @Override
    protected void createCreateInstanceMethod(TypeElement componentTypeElement, String packageName,
        String className, TypeName generatedTypeName, Builder vueConstructorClassBuilder)
    {
        MethodSpec.Builder createInstanceBuilder = MethodSpec
            .methodBuilder("createInstance")
            .addModifiers(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL)
            .addAnnotation(JsOverlay.class)
            .returns(generatedTypeName);

        TypeName optionsTypeName = ClassName.get(packageName, className + COMPONENT_OPTIONS_SUFFIX);
        createInstanceBuilder.addStatement("$T componentOptions = new $T()",
            optionsTypeName,
            optionsTypeName);

        // Set parent
        TypeName superClass = TypeName.get(componentTypeElement.getSuperclass());
        if (!TypeName.get(VueComponent.class).equals(superClass))
        {
            TypeName superConstructor =
                ClassName.bestGuess(superClass.toString() + CONSTRUCTOR_SUFFIX);
            createInstanceBuilder.addStatement("$L = ($T) $T.get().extend($L)",
                INSTANCE_PROP,
                generatedTypeName,
                superConstructor,
                "componentOptions");
        }
        else
        {
            createInstanceBuilder.addStatement("$L = ($T) $T.extend($L)",
                INSTANCE_PROP,
                generatedTypeName,
                Vue.class,
                "componentOptions");
        }

        // Inject dependencies
        createInstanceBuilder
            .beginControlFlow("if (!$L)", DEPENDENCIES_INJECTED_PROP)
            .addStatement("$L = true", DEPENDENCIES_INJECTED_PROP)
            .addStatement("injectDependencies()")
            .endControlFlow();

        createInstanceBuilder.addStatement("return $L", INSTANCE_PROP);
        vueConstructorClassBuilder.addMethod(createInstanceBuilder.build());

        createInjectDependenciesMethod(componentTypeElement, vueConstructorClassBuilder);
    }

    /**
     * Create the method that will inject dependencies.
     * By dependencies we mean locally declared Components and Directives.
     * @param componentTypeElement The Component we generate for
     * @param vueConstructorClassBuilder The builder of the VueConstructor we are generating
     */
    private void createInjectDependenciesMethod(TypeElement componentTypeElement,
        Builder vueConstructorClassBuilder)
    {
        MethodSpec.Builder injectDependenciesBuilder = MethodSpec
            .methodBuilder("injectDependencies")
            .addAnnotation(JsOverlay.class)
            .addModifiers(Modifier.STATIC, Modifier.PRIVATE, Modifier.FINAL);

        Component componentAnnotation = componentTypeElement.getAnnotation(Component.class);
        registerLocalComponents(componentAnnotation, injectDependenciesBuilder);
        registerLocalDirectives(componentAnnotation, injectDependenciesBuilder);

        vueConstructorClassBuilder.addMethod(injectDependenciesBuilder.build());
    }

    /**
     * Register components passed to the annotation.
     * @param annotation The Component annotation on the Component we generate for
     * @param injectDependenciesBuilder The builder for the injectDependencies method
     */
    private void registerLocalComponents(Component annotation,
        MethodSpec.Builder injectDependenciesBuilder)
    {
        try
        {
            Class<?>[] componentsClass = annotation.components();

            if (componentsClass.length > 0)
                addGetComponentsStatement(injectDependenciesBuilder);

            Stream
                .of(componentsClass)
                .forEach(clazz -> injectDependenciesBuilder.addStatement(
                    "components.set($S, $T.get())",
                    VueGWTTools.componentToTagName(clazz.getName()),
                    ClassName.bestGuess(clazz.getCanonicalName() + CONSTRUCTOR_SUFFIX)));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();

            if (!classTypeMirrors.isEmpty())
                addGetComponentsStatement(injectDependenciesBuilder);

            classTypeMirrors.forEach(classTypeMirror -> {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                injectDependenciesBuilder.addStatement("components.set($S, $T.get())",
                    VueGWTTools.componentToTagName(classTypeElement.getSimpleName().toString()),
                    ClassName.bestGuess(classTypeElement.getQualifiedName() + CONSTRUCTOR_SUFFIX));
            });
        }
    }

    private void addGetComponentsStatement(MethodSpec.Builder injectDependenciesBuilder)
    {
        injectDependenciesBuilder.addStatement("$T<$T> components = $L.getOptionsComponents()",
            JsObject.class,
            VueConstructor.class,
            INSTANCE_PROP);
    }

    /**
     * Register directives passed to the annotation.
     * @param annotation The Component annotation on the Component we generate for
     * @param injectDependenciesBuilder The builder for the injectDependencies method
     */
    private void registerLocalDirectives(Component annotation,
        MethodSpec.Builder injectDependenciesBuilder)
    {
        try
        {
            Class<?>[] componentsClass = annotation.directives();

            if (componentsClass.length > 0)
                addGetDirectivesStatement(injectDependenciesBuilder);

            Stream
                .of(componentsClass)
                .forEach(clazz -> injectDependenciesBuilder.addStatement(
                    "directives.set($S, new $T())",
                    VueGWTTools.directiveToTagName(clazz.getName()),
                    ClassName.bestGuess(clazz.getCanonicalName() + DIRECTIVE_OPTIONS_SUFFIX)));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();

            if (!classTypeMirrors.isEmpty())
                addGetDirectivesStatement(injectDependenciesBuilder);

            classTypeMirrors.forEach(classTypeMirror -> {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                injectDependenciesBuilder.addStatement("directives.set($S, new $T())",
                    VueGWTTools.directiveToTagName(classTypeElement.getSimpleName().toString()),
                    ClassName.bestGuess(classTypeElement.getQualifiedName()
                        + DIRECTIVE_OPTIONS_SUFFIX));
            });
        }
    }

    private void addGetDirectivesStatement(MethodSpec.Builder injectDependenciesBuilder)
    {
        injectDependenciesBuilder.addStatement("$T<$T> directives = $L.getOptionsDirectives()",
            JsObject.class,
            VueDirectiveOptions.class,
            INSTANCE_PROP);
    }
}
