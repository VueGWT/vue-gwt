package com.axellience.vuegwt.processors.utils;

import javax.inject.Inject;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.util.List;
import java.util.stream.Collectors;

public class InjectedDependenciesUtil
{
    public static List<VariableElement> getInjectedFields(TypeElement component)
    {
        return ElementFilter
            .fieldsIn(component.getEnclosedElements())
            .stream()
            .filter(InjectedDependenciesUtil::hasInjectAnnotation)
            .collect(Collectors.toList());
    }

    public static List<ExecutableElement> getInjectedMethods(TypeElement component)
    {
        return ElementFilter
            .methodsIn(component.getEnclosedElements())
            .stream()
            .filter(InjectedDependenciesUtil::hasInjectAnnotation)
            .collect(Collectors.toList());
    }

    /**
     * Check if the given element has an Inject annotation. Either the one from Google Gin, or the
     * javax one. We don't want to depend on Gin, so we check the google one based on qualifiedName
     * @param element The element we want to check
     * @return True if has an Inject annotation, false otherwise
     */
    public static boolean hasInjectAnnotation(Element element)
    {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors())
        {
            String annotationQualifiedName = annotationMirror.getAnnotationType().toString();
            if (annotationQualifiedName.equals(Inject.class.getCanonicalName()))
                return true;
            if (annotationQualifiedName.equals("com.google.inject.Inject"))
                return true;
        }
        return false;
    }
}
