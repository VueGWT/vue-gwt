package com.axellience.vuegwt.processors.component.factory;

import com.axellience.vuegwt.core.annotations.component.Component;
import com.axellience.vuegwt.core.client.Vue;
import com.axellience.vuegwt.core.client.component.VueComponent;
import com.axellience.vuegwt.core.client.component.options.CustomizeOptions;
import com.axellience.vuegwt.core.client.component.options.VueComponentOptions;
import com.axellience.vuegwt.core.client.directive.options.VueDirectiveOptions;
import com.axellience.vuegwt.core.client.vue.VueFactory;
import com.axellience.vuegwt.core.client.vue.VueJsAsyncProvider;
import com.axellience.vuegwt.core.client.vue.VueJsConstructor;
import com.axellience.vuegwt.processors.utils.GeneratorsNameUtil;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import jsinterop.base.JsPropertyMap;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getComponentCustomizeOptions;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getComponentLocalComponents;
import static com.axellience.vuegwt.processors.utils.ComponentGeneratorsUtil.getSuperComponentType;
import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.*;

/**
 * Generate {@link VueFactory} from the user {@link VueComponent} classes annotated by {@link
 * Component}.
 * @author Adrien Baron
 */
public class VueComponentFactoryGenerator extends AbstractVueComponentFactoryGenerator
{
    private final Elements elements;
    private boolean hasInjectedDependencies;

    public VueComponentFactoryGenerator(ProcessingEnvironment processingEnv)
    {
        super(processingEnv);

        elements = processingEnv.getElementUtils();
    }

    public void generate(TypeElement component,
        boolean hasInjectedDependencies)
    {
        this.hasInjectedDependencies = hasInjectedDependencies;
        super.generate(component);
    }

    @Override
    protected List<CodeBlock> createInitMethod(TypeElement component,
        Builder vueFactoryClassBuilder)
    {
        MethodSpec.Builder initBuilder = MethodSpec
            .methodBuilder("init")
            .addModifiers(Modifier.PROTECTED)
            .addAnnotation(Inject.class);

        List<CodeBlock> initParametersCall = new LinkedList<>();

        // Get options
        initBuilder.addStatement("$T<$T> componentOptions = $T.getOptions()",
            VueComponentOptions.class,
            component.asType(),
            componentJsTypeName(component));
        processCustomizeOptions(component, initBuilder, initParametersCall);

        // Extend the parent Component
        Optional<ClassName> superFactoryType =
            getSuperComponentType(component).map(GeneratorsNameUtil::componentFactoryName);

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

        if (hasInjectedDependencies)
            registerDependenciesProvider(component, initBuilder, initParametersCall);

        registerLocalComponents(component, initBuilder, initParametersCall);
        registerLocalDirectives(componentAnnotation, initBuilder);

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
     * @param component The Component we generate for
     * @param initBuilder The builder of the init method
     * @param staticInitParameters Parameters of the init method
     */
    private void registerDependenciesProvider(TypeElement component, MethodSpec.Builder initBuilder,
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
     * @param initBuilder The builder for the init method
     * @param staticInitParameters The parameters of the static init function
     */
    private void registerLocalComponents(TypeElement component, MethodSpec.Builder initBuilder,
        List<CodeBlock> staticInitParameters)
    {
        List<TypeMirror> localComponents = getComponentLocalComponents(elements, component);

        if (localComponents.isEmpty())
            return;

        initBuilder.addStatement("$T<$T> components = jsConstructor.getOptionsComponents()",
            JsPropertyMap.class,
            ParameterizedTypeName.get(VueJsAsyncProvider.class, VueJsConstructor.class));

        localComponents.forEach(localComponent -> {
            ClassName factory = componentFactoryName(localComponent);

            String parameterName = factory.reflectionName().replaceAll("\\.", "_");
            initBuilder.addParameter(providerOf(factory), parameterName);
            staticInitParameters.add(CodeBlock.of("() -> $T.get()", factory));

            Element localComponentElement = ((DeclaredType) localComponent).asElement();
            String tagName = componentToTagName(localComponentElement.getSimpleName().toString(),
                localComponentElement.getAnnotation(Component.class));
            initBuilder.addStatement(
                "components.set($S, render -> render.accept($L.get().getJsConstructor()))",
                tagName,
                parameterName);
        });
    }

    /**
     * Register directives passed to the annotation.
     * @param annotation The Component annotation on the Component we generate for
     * @param initBuilder The builder for the init method
     */
    private void registerLocalDirectives(Component annotation, MethodSpec.Builder initBuilder)
    {
        try
        {
            Class<?>[] componentsClass = annotation.directives();

            if (componentsClass.length > 0)
                addGetDirectivesStatement(initBuilder);

            Stream
                .of(componentsClass)
                .forEach(clazz -> initBuilder.addStatement("directives.set($S, new $T())",
                    directiveToTagName(clazz.getName()),
                    directiveOptionsName(clazz)));
        }
        catch (MirroredTypesException mte)
        {
            List<DeclaredType> classTypeMirrors = (List<DeclaredType>) mte.getTypeMirrors();

            if (!classTypeMirrors.isEmpty())
                addGetDirectivesStatement(initBuilder);

            classTypeMirrors.forEach(classTypeMirror -> {
                TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
                initBuilder.addStatement("directives.set($S, new $T())",
                    directiveToTagName(classTypeElement.getSimpleName().toString()),
                    directiveOptionsName(classTypeElement));
            });
        }
    }

    private void addGetDirectivesStatement(MethodSpec.Builder injectDependenciesBuilder)
    {
        injectDependenciesBuilder.addStatement(
            "$T<$T> directives = jsConstructor.getOptionsDirectives()",
            JsPropertyMap.class,
            VueDirectiveOptions.class);
    }

    /**
     * Process all the {@link CustomizeOptions} from the {@link Component} annotation.
     * @param component The {@link VueComponent} we generate for
     * @param initBuilder The builder for our {@link VueFactory} init method
     * @param staticInitParameters The list of static parameters to pass when calling the init
     * method from a static context
     */
    private void processCustomizeOptions(TypeElement component, MethodSpec.Builder initBuilder,
        List<CodeBlock> staticInitParameters)
    {
        getComponentCustomizeOptions(elements,
            component).forEach(customizeOptions -> this.processCustomizeOptions(customizeOptions,
            initBuilder,
            staticInitParameters));
    }

    /**
     * Process the {@link CustomizeOptions} from the {@link Component} annotation. An instance
     * of this class should be created with our factory and used to customize our
     * {@link VueComponentOptions} before passing them to Vue.
     * @param customizeOptions The {@link CustomizeOptions} we are generating for
     * @param initBuilder The builder for our {@link VueFactory} init method
     * @param staticInitParameters The list of static parameters to pass when calling the init
     * method from a static context
     */
    private void processCustomizeOptions(TypeMirror customizeOptions,
        MethodSpec.Builder initBuilder, List<CodeBlock> staticInitParameters)
    {
        ClassName customizeOptionsClassName = ((ClassName) ClassName.get(customizeOptions));
        char c[] = customizeOptionsClassName.simpleName().toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        String parameterName = new String(c);

        initBuilder.addParameter(customizeOptionsClassName, parameterName);
        staticInitParameters.add(CodeBlock.of("new $T()", customizeOptionsClassName));

        initBuilder.addStatement("$L.$L($L)",
            parameterName,
            "customizeOptions",
            "componentOptions");
    }
}
