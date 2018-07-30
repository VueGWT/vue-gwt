package com.axellience.vuegwt.processors.component.template.parser.context.localcomponents;

import static com.axellience.vuegwt.processors.utils.GeneratorsNameUtil.propNameToAttributeName;
import static com.axellience.vuegwt.processors.utils.GeneratorsUtil.boundedAttributeToAttributeName;

import com.squareup.javapoet.TypeName;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.lang.model.type.TypeMirror;

public class LocalComponent {

  private final Map<String, LocalComponentProp> attributeNameToPropMap;
  private final Map<String, LocalComponentProp> propNameToPropMap;
  private final Set<LocalComponentProp> requiredProps;
  private final String componentTagName;
  private final TypeMirror componentType;

  LocalComponent(String componentTagName, TypeMirror componentType) {
    this.componentTagName = componentTagName;
    this.componentType = componentType;
    attributeNameToPropMap = new HashMap<>();
    propNameToPropMap = new HashMap<>();
    requiredProps = new HashSet<>();
  }

  public void addProp(String propName, TypeName propType, boolean isRequired) {
    String attributeName = propNameToAttributeName(propName);
    LocalComponentProp localComponentProp =
        new LocalComponentProp(propName, attributeName, propType, isRequired);
    attributeNameToPropMap.put(attributeName, localComponentProp);
    propNameToPropMap.put(propName, localComponentProp);

    if (isRequired) {
      requiredProps.add(localComponentProp);
    }
  }

  private Optional<LocalComponentProp> getProp(String attributeName) {
    if (attributeNameToPropMap.containsKey(attributeName)) {
      return Optional.of(attributeNameToPropMap.get(attributeName));
    }

    if (propNameToPropMap.containsKey(attributeName)) {
      return Optional.of(propNameToPropMap.get(attributeName));
    }

    return Optional.empty();
  }

  public Optional<LocalComponentProp> getPropForAttribute(String boundedAttributeName) {
    return getProp(boundedAttributeToAttributeName(boundedAttributeName));
  }

  public Set<LocalComponentProp> getRequiredProps() {
    return requiredProps;
  }

  public String getComponentTagName() {
    return componentTagName;
  }

  public TypeMirror getComponentType() {
    return componentType;
  }
}
