package com.axellience.vuegwt.jsr69.component.factory;

import com.axellience.vuegwt.client.Vue;
import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.client.jsnative.jstypes.JsObject;
import com.axellience.vuegwt.client.vue.VueFactory;
import com.axellience.vuegwt.client.vue.VueJsAsyncProvider;
import com.axellience.vuegwt.client.vue.VueJsConstructor;
import com.axellience.vuegwt.jsr69.GenerationNameUtil;
import com.axellience.vuegwt.jsr69.component.annotations.Component;
import com.axellience.vuegwt.jsr69.component.annotations.VueCustomizeOptions;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.axellience.vuegwt.client.tools.VueGWTTools.componentToTagName;
import static com.axellience.vuegwt.client.tools.VueGWTTools.directiveToTagName;
import static com.axellience.vuegwt.jsr69.GenerationNameUtil.*;
import static com.axellience.vuegwt.jsr69.GenerationUtil.hasAnnotation;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getComponentLocalComponents;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.resolveVariableTypeName;
import static com.axellience.vuegwt.jsr69.component.ComponentGenerationUtil.getSuperComponentType;

/**
 * Generate {@link VueFactory} from the user {@link VueComponent} classes annotated by {@link
 * Component}.
 * @author Adrien Baron
 */
public class VueComponentFactoryGenerator extends AbstractVueComponentFactoryGenerator
{
    private final Elements elements;
    private final Types types;

    public VueComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);

        elements = processingEnv.getElementUtils();
        types = processingEnv.getTypeUtils();
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
            componentJsTypeName(component));

        // Extend the parent Component
        Optional<ClassName> superFactoryType = getSuperFactoryType(component);
        if (superFactoryType.isPresent())
        {
            initBuilder.addParameter(superFactoryType.get(), "superFactory");
            initBuilder.addStatement(
                "jsConstructor = superFactory.getJsConstructor().extendJavaComponent($L)",
                "componentOptions");
            initParametersCall.add(CodeBlock.of("$T.get()", superFactoryType.get()));
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
        processCustomizeOptionsMethods(component, initBuilder, initParametersCall);

        MethodSpec initMethod = initBuilder.build();
        vueFactoryClassBuilder.addMethod(initMethod);

        return initParametersCall;
    }

    /**
     * Register the {@link VueComponent} dependencies provider.
     * <br>
     * For each instance of our VueComponent, we will create a Java instance of dependencies using
     * this provider.
     * <br>
     * We then use that instance to get the values of our Java properties if they are injected.
     * @param component
     * @param initBuilder
     */
    private void registerProvider(TypeElement component, MethodSpec.Builder initBuilder,
        List<CodeBlock> staticInitParameters)
    {
        ClassName componentDependencies = componentInjectedDependenciesName(component);

        initBuilder.addParameter(providerOf(componentDependencies),
            "componentDependenciesProvider");
        staticInitParameters.add(CodeBlock.of("() -> new $T()", componentDependencies));

        initBuilder.addStatement(
            "jsConstructor.getOptions().addProvider($T.class, componentDependenciesProvider)",
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

    private void processCustomizeOptionsMethods(TypeElement component,
        MethodSpec.Builder initBuilder, List<CodeBlock> staticInitParameters)
    {
        List<ExecutableElement> customizeOptionsMethods = ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(method -> hasAnnotation(method, VueCustomizeOptions.class))
            .peek(this::validateCustomizeOptionsMethod)
            .collect(Collectors.toList());

        customizeOptionsMethods.forEach(customizeOptionsMethod -> this.processCustomizeOptionsMethod(
            customizeOptionsMethod,
            initBuilder,
            component,
            staticInitParameters));
    }

    private void processCustomizeOptionsMethod(ExecutableElement customizeOptionsMethod,
        MethodSpec.Builder initBuilder, TypeElement component, List<CodeBlock> staticInitParameters)
    {
        // Check if has at least one parameter to inject
        String methodName = customizeOptionsMethod.getSimpleName().toString();

        List<String> parameters = new LinkedList<>();
        parameters.add("jsConstructor.getOptions()");

        if (customizeOptionsMethod.getParameters().size() > 1)
        {
            for (int i = 1; i < customizeOptionsMethod.getParameters().size(); i++)
            {
                VariableElement parameter = customizeOptionsMethod.getParameters().get(i);

                String parameterName = methodName + "_" + parameter.getSimpleName().toString();
                parameters.add(parameterName);
                initBuilder.addParameter(resolveVariableTypeName(parameter, messager), parameterName);
                staticInitParameters.add(CodeBlock.of("null"));
            }
        }

        initBuilder.addStatement("$T.$L($L)",
            component.asType(),
            methodName,
            String.join(", ", parameters));
    }

    private void validateCustomizeOptionsMethod(ExecutableElement customizeOptionsMethod)
    {
        if (customizeOptionsMethod.getParameters().isEmpty())
        {
            messager.printMessage(Kind.ERROR,
                getCustomizeOptionsErrorPrefix(customizeOptionsMethod)
                    + " should have one parameter of type VueComponentOptions");
            return;
        }

        VariableElement parameter = customizeOptionsMethod.getParameters().get(0);
        if (!types.isAssignable(parameter.asType(),
            elements.getTypeElement(VueComponentOptions.class.getCanonicalName()).asType()))
        {
            messager.printMessage(Kind.ERROR,
                getCustomizeOptionsErrorPrefix(customizeOptionsMethod)
                    + " VueComponentOptions should be your first parameter");
        }
    }

    private String getCustomizeOptionsErrorPrefix(ExecutableElement customizeOptionsMethod)
    {
        return "Customize options method "
            + customizeOptionsMethod.toString()
            + " in component "
            + customizeOptionsMethod.getEnclosingElement().getSimpleName().toString();
    }

    private void addGetDirectivesStatement(MethodSpec.Builder injectDependenciesBuilder)
    {
        injectDependenciesBuilder.addStatement(
            "$T<$T> directives = jsConstructor.getOptionsDirectives()",
            JsObject.class,
            VueDirectiveOptions.class);
    }

    private Optional<ClassName> getSuperFactoryType(TypeElement component)
    {
        return getSuperComponentType(component).map(GenerationNameUtil::componentFactoryName);
    }
}
