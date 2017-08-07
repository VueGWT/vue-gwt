package com.axellience.vuegwt.jsr69.component;

import com.axellience.vuegwt.client.component.VueComponent;
import com.axellience.vuegwt.jsr69.component.annotations.Component;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilities methods to manipulate the {@link Component} annotation
 * @author Adrien Baron
 */
public class ComponentGenerationUtil
{
    /**
     * Return the list of {@link VueComponent} dependencies listed on {@link Component#components()}.
     * When retrieving this list, it can produce a {@link MirroredTypesException}.
     * The case is managed here and we always returns {@link TypeMirror} of the dependencies.
     * @param elementsUtil The Element utils provided by the annotation processor environement.
     * @param component The {@link Component} annotation to process
     * @return The list of TypeMirror of the {@link VueComponent} the {@link Component} depends on
     */
    public static List<TypeMirror> getComponentLocalComponents(Elements elementsUtil,
        TypeElement component)
    {
        Component componentAnnotation = component.getAnnotation(Component.class);

        try
        {
            Class<?>[] componentsClass = componentAnnotation.components();

            return Stream
                .of(componentsClass)
                .map(Class::getCanonicalName)
                .map(elementsUtil::getTypeElement)
                .map(TypeElement::asType)
                .collect(Collectors.toList());
        }
        catch (MirroredTypesException mte)
        {
            return new LinkedList<>(mte.getTypeMirrors());
        }
    }
}