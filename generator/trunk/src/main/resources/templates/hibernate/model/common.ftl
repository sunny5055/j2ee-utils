<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#function getHibernateAnnotation class property>
  <#local hibernateAnnotation= "">
  <#if property?node_name = "id">
    <#local hibernateAnnotation= "@Id">
    <#local hibernateAnnotation= hibernateAnnotation + " @GeneratedValue(strategy = GenerationType.AUTO)">
    <#local hibernateAnnotation= hibernateAnnotation + " @Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(class.@columnPrefix, property)}\"">
    <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "embedded-id">
    <#local hibernateAnnotation= "@EmbeddedId">
  <#elseif property?node_name = "column">
    <#local hibernateAnnotation= "@Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(class.@columnPrefix, property)}\"">
    <#if xml.getAttribute(property.@unique) == "true">
      <#local hibernateAnnotation= hibernateAnnotation + ", unique = true">
    </#if>
    <#if xml.getAttribute(property.@nullable) == "false">
      <#local hibernateAnnotation= hibernateAnnotation + ", nullable = false">
    </#if>
    <#if getType(property.@type) == "String" && xml.getAttribute(property.@length) != "">
      <#local hibernateAnnotation= hibernateAnnotation + ", length = ${xml.getAttribute(property.@length)}">
    </#if>
    <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "many-to-one">
      <#local hibernateAnnotation= "@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY">
      <#if xml.getAttribute(property.@nullable) == "false">
      <#local hibernateAnnotation= hibernateAnnotation + ", optional = false)">
    </#if>
      <#local hibernateAnnotation= hibernateAnnotation + ")">
      <#local hibernateAnnotation= hibernateAnnotation + " @JoinColumn(name = \"${getColumnName(class.@columnPrefix, property)}\")">
  <#elseif property?node_name = "one-to-many">
    <#local hibernateAnnotation= "@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(class, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "many-to-many">
    <#local hibernateAnnotation= "@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(class, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  </#if>
  <#return hibernateAnnotation>
</#function>


<#macro getProperty property>
  <#local visibility= xml.getAttribute(property.@visibility, "private")>
  <#if property?node_name = "id">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.getProperty visibility=visibility type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getProperty visibility=visibility type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro initProperties property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
  	<@java.initProperties type=property.@listType name=property.@name value=xml.getAttribute(property.@targetEntity) />
  </#if>
</#macro>


<#macro getterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  <@java.getter type=type name=property.@name methodName="getPrimaryKey" />
</#macro>

<#macro setterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  <@java.setter type=type name=property.@name methodName="setPrimaryKey" argName="primaryKey"/>
</#macro>

<#macro getter property>
  <#if property?node_name = "id">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.getter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro setter property>
  <#if property?node_name = "id">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.setter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.setter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.setter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.setter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro addMethod property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local type = "${getClassName(property.@targetEntity)}">
    <@java.addListMethod type=type name=property.@name />
  </#if>
</#macro>

<#macro removeMethod property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local type = "${getClassName(property.@targetEntity)}">
    <@java.removeListMethod type=type name=property.@name />
  </#if>
</#macro>

<#macro constructor className constructor>
  <#assign parameters = constructor["h:parameters/*"]>
  <#local visibility= xml.getAttribute(constructor.@visibility, "public")>
  <#if constructor["h:content"]?is_node>
    <@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) content=constructor["h:content"] />
  <#else>
	<@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>


<#macro interfaceOperation operation>
  <#assign parameters = operation["h:parameters/*"]>
  <@java.interfaceOperation modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
</#macro>


<#macro operation operation>
  <#assign parameters = operation["h:parameters/*"]>
  <#local visibility= xml.getAttribute(operation.@visibility, "public")>
  <#if operation["h:content"]?is_node>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) content=operation["h:content"] />
  <#else>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>

