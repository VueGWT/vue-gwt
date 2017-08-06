package com.axellience.vuegwt.jsr69.component.factory;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.jsr69.GenerationNameUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.axellience.vuegwt.client.tools.VueGWTTools.componentToTagName;
import static com.axellience.vuegwt.client.tools.VueGWTTools.directiveToTagName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentFactoryName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentWithTemplateName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.directiveOptionsName;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getComponentLocalComponents;

/**
 * Generate {@link VueFactory} from the user Vue Component classes annotated by {@link
 * Component}.
 * @author Adrien Baron
 */
public class VueComponentFactoryGenerator extends AbstractVueComponentFactoryGenerator
{
    private final Elements elements;

    public VueComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);

        elements = processingEnv.getElementUtils();
    }

    @Override
    protected void createConstructor(TypeElement component, ClassName vueFactoryType,
        Builder vueFactoryBuilder)
    {
        MethodSpec.Builder constructorBuilder =
            MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE);

        constructorBuilder.addStatement("$T<$T> componentOptions = $T.getOptions()",
            VueComponentOptions.class,
            component.asType(),
            componentWithTemplateName(component));

        // Set parent
        TypeMirror superType = component.getSuperclass();
        if (!TypeName.get(VueComponent.class).equals(TypeName.get(superType)))
        {
            constructorBuilder.addStatement(
                "jsConstructor = $T.get().getJsConstructor().extendJavaComponent($L)",
                componentFactoryName(superType),
                "componentOptions");
        }
        else
        {
            constructorBuilder.addStatement("jsConstructor = $T.extendJavaComponent($L)",
                Vue.class,
                "componentOptions");
        }

        vueFactoryBuilder.addMethod(constructorBuilder.build());

        createInjectDependenciesMethod(component, vueFactoryBuilder);
    }

    @Override
    protected void configureStaticInstance(TypeElement component, MethodSpec.Builder getBuilder)
    {
        List<TypeMirror> localComponents = getComponentLocalComponents(elements, component);

        String parameters = localComponents
            .stream()
            .map(GenerationNameUtil::componentFactoryName)
            .map(componentFactoryName -> componentFactoryName.reflectionName() + ".get()")
            .collect(Collectors.joining(", "));

        // Inject dependencies
        getBuilder.addStatement("$L.injectDependencies($L)", INSTANCE_PROP, parameters);
    }

    /**
     * Create the method that will inject dependencies.
     * By dependencies we mean locally declared Components and Directives.
     * @param component The Component we generate for
     * @param vueFactoryClassBuilder The builder of the VueFactory we are generating
     */
    private void createInjectDependenciesMethod(TypeElement component,
        Builder vueFactoryClassBuilder)
    {
        MethodSpec.Builder injectDependenciesBuilder = MethodSpec
            .methodBuilder("injectDependencies")
            .addModifiers(Modifier.PRIVATE, Modifier.FINAL)
            .addAnnotation(Inject.class);

        Component componentAnnotation = component.getAnnotation(Component.class);
        registerLocalComponents(component, injectDependenciesBuilder);
        registerLocalDirectives(componentAnnotation, injectDependenciesBuilder);

        vueFactoryClassBuilder.addMethod(injectDependenciesBuilder.build());
    }

    /**
     * Register components passed to the annotation.
     * The parameters of the generated method are the factories for the local components we depend
     * on.
     * Their values are either injected, or pass directly when using the "get()" static accessor.
     * @param component The Component we generate for
     * @param injectDependenciesBuilder The builder for the injectDependencies method
     */
    private void registerLocalComponents(TypeElement component,
        MethodSpec.Builder injectDependenciesBuilder)
    {
        List<TypeMirror> localComponents = getComponentLocalComponents(elements, component);

        if (localComponents.isEmpty())
            return;

        injectDependenciesBuilder.addStatement(
            "$T<$T> components = jsConstructor.getOptionsComponents()",
            JsObject.class,
            VueJsConstructor.class);

        localComponents.forEach(localComponent -> {
            ClassName factoryClassName = componentFactoryName(localComponent);

            String parameterName = factoryClassName.reflectionName().replaceAll("\\.", "_");
            injectDependenciesBuilder.addParameter(factoryClassName, parameterName);

            String tagName = componentToTagName(((DeclaredType) localComponent)
                .asElement()
                .getSimpleName()
                .toString());
            injectDependenciesBuilder.addStatement("components.set($S, $L.getJsConstructor())",
                tagName,
                parameterName);
        });
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
                    directiveToTagName(clazz.getName()),
                    directiveOptionsName(clazz)));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();

            if (!classTypeMirrors.isEmpty())
                addGetDirectivesStatement(injectDependenciesBuilder);

            classTypeMirrors.forEach(classTypeMirror -> {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                injectDependenciesBuilder.addStatement("directives.set($S, new $T())",
                    directiveToTagName(classTypeElement.getSimpleName().toString()),
                    directiveOptionsName(classTypeElement));
            });
        }
    }

    private void addGetDirectivesStatement(MethodSpec.Builder injectDependenciesBuilder)
    {
        injectDependenciesBuilder.addStatement(
            "$T<$T> directives = jsConstructor.getOptionsDirectives()",
            JsObject.class,
            VueDirectiveOptions.class);
    }
}
