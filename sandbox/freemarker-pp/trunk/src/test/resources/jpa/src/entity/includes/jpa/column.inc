<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getColumnAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "column">
    <#local jpaAnnotation= "@Column(">
    <#local jpaAnnotation= jpaAnnotation + "name = \"${getColumnName(entity.@columnPrefix, property)}\"">
    <#if xml.getAttribute(property.@unique) == "true">
      <#local jpaAnnotation= jpaAnnotation + ", unique = true">
    </#if>
    <#if xml.getAttribute(property.@nullable) == "false">
      <#local jpaAnnotation= jpaAnnotation + ", nullable = false">
    </#if>
    <#if getType(property.@type) == "String" && xml.getAttribute(property.@length) != "">
      <#local jpaAnnotation= jpaAnnotation + ", length = ${xml.getAttribute(property.@length)}">
    </#if>
    <#local jpaAnnotation= jpaAnnotation + ")">
  </#if>
  <#return jpaAnnotation>
</#function>

