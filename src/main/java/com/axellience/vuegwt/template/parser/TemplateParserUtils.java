package com.axellience.vuegwt.template.parser;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Adrien Baron
 */
public class TemplateParserUtils
{
    private static Set<String> WRAPPED_PRIMITIVE_TYPES;

    static
    {
        WRAPPED_PRIMITIVE_TYPES = new HashSet<>();
        WRAPPED_PRIMITIVE_TYPES.add(Integer.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Integer.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Short.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Short.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Long.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Long.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Float.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Float.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Double.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Double.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Boolean.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Boolean.class.getCanonicalName());
        WRAPPED_PRIMITIVE_TYPES.add(Character.class.getSimpleName());
        WRAPPED_PRIMITIVE_TYPES.add(Character.class.getCanonicalName());
    }

    static boolean isWrappedPrimitiveType(String type)
    {
        return WRAPPED_PRIMITIVE_TYPES.contains(type);
    }
}
