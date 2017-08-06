package com.axellience.vuegwt.jsr69.component;

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
 * @author Adrien Baron
 */
public class ComponentGenerationUtil
{
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