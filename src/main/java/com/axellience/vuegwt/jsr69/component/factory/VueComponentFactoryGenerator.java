package com.axellience.vuegwt.jsr69.component.factory;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.component.template.ComponentWithTemplate;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsAsyncProvider;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.axellience.vuegwt.client.tools.VueGWTTools.componentToTagName;
import static com.axellience.vuegwt.client.tools.VueGWTTools.directiveToTagName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentFactoryName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.componentWithTemplateName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.directiveOptionsName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.providerOf;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getComponentLocalComponents;

/**
 * Generate {@link VueFactory} from the user {@link VueComponent} classes annotated by {@link
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
    protected List<CodeBlock> createInitMethod(TypeElement component,
        Builder vueFactoryClassBuilder)
    {
        MethodSpec.Builder initBuilder = MethodSpec
            .methodBuilder("init")
            .addModifiers(Modifier.PRIVATE)
            .addAnnotation(Inject.class);

        List<CodeBlock> initParametersCall = new LinkedList<>();

        // Get options
        initBuilder.addStatement("$T<$T> componentOptions = $T.getOptions()",
            VueComponentOptions.class,
            component.asType(),
            componentWithTemplateName(component));

        // Extend the parent Component
        ClassName superFactoryType = getSuperFactoryType(component);
        if (superFactoryType != null)
        {
            initBuilder.addParameter(superFactoryType, "superFactory");
            initBuilder.addStatement(
                "jsConstructor = superFactory.getJsConstructor().extendJavaComponent($L)",
                "componentOptions");
            initParametersCall.add(CodeBlock.of("$T.get()", superFactoryType));
        }
        else
        {
            initBuilder.addStatement("jsConstructor = $T.extendJavaComponent($L)",
                Vue.class,
                "componentOptions");
        }

        Component componentAnnotation = component.getAnnotation(Component.class);
        registerProvider(component, initBuilder, initParametersCall);
        registerLocalComponents(component, initBuilder, initParametersCall);
        registerLocalDirectives(componentAnnotation, initBuilder);

        MethodSpec initMethod = initBuilder.build();
        vueFactoryClassBuilder.addMethod(initMethod);

        return initParametersCall;
    }

    /**
     * Register the {@link ComponentWithTemplate} provider.
     * <br>
     * For each instance of our VueComponent, we will create a Java instance using this provider.
     * <br>
     * We then use that instance to get the values of our Java properties whether they are injected
     * or created inline in the Java component.
     * @param component
     * @param initBuilder
     */
    private void registerProvider(TypeElement component, MethodSpec.Builder initBuilder,
        List<CodeBlock> staticInitParameters)
    {
        ClassName componentWithTemplate = componentWithTemplateName(component);

        initBuilder.addParameter(providerOf(componentWithTemplate),
            "componentWithTemplateProvider");
        staticInitParameters.add(CodeBlock.of("() -> new $T()", componentWithTemplate));

        initBuilder.addStatement(
            "jsConstructor.getOptions().addProvider($T.class, componentWithTemplateProvider)",
            component);

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
        MethodSpec.Builder injectDependenciesBuilder, List<CodeBlock> staticInitParameters)
    {
        List<TypeMirror> localComponents = getComponentLocalComponents(elements, component);

        if (localComponents.isEmpty())
            return;

        injectDependenciesBuilder.addStatement(
            "$T<$T> components = jsConstructor.getOptionsComponents()",
            JsObject.class,
            ParameterizedTypeName.get(VueJsAsyncProvider.class, VueJsConstructor.class));

        localComponents.forEach(localComponent -> {
            ClassName factory = componentFactoryName(localComponent);

            String parameterName = factory.reflectionName().replaceAll("\\.", "_");
            injectDependenciesBuilder.addParameter(providerOf(factory), parameterName);
            staticInitParameters.add(CodeBlock.of("() -> $T.get()", factory));

            String tagName = componentToTagName(((DeclaredType) localComponent)
                .asElement()
                .getSimpleName()
                .toString());
            injectDependenciesBuilder.addStatement(
                "components.set($S, render -> render.accept($L.get().getJsConstructor()))",
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

    private ClassName getSuperFactoryType(TypeElement component)
    {
        TypeMirror superType = component.getSuperclass();
        if (!TypeName.get(VueComponent.class).equals(TypeName.get(superType)))
            return componentFactoryName(superType);

        return null;
    }
}
