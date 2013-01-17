<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "../common/xml.ftl" as xml>
<#import "../common/java.ftl" as java>
<#import "../common/directives.ftl" as dir>

<#function getImportsFor node>
  <#assign imports = [] />
  <#if node?node_name = "property" || node?node_name = "type" || node?node_name = "parameter">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "property-list" || node?node_name = "type-list" || node?node_name = "parameter-list">
      <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
      <#if xml.getAttribute(node.@value) == "String">
        <@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
      </#if>
  <#elseif node?node_name = "property-map" || node?node_name = "type-map" || node?node_name = "parameter-map">
    <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@key) />
      <#if xml.getAttribute(node.@value) == "String" || xml.getAttribute(node.@key) == "String">
        <@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
      </#if>
  <#elseif node?node_name = "constructor">
    <#assign parameters = node["b:parameters/*"]>
    <#list parameters as parameter>
      <@addTo assignTo="imports" element=getImportsFor(parameter) />
    </#list>
  <#elseif node?node_name = "operation">
    <#if node["b:return/b:type"]?is_node>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type"]) />
    <#elseif node["b:return/b:type-list"]?is_node>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type-list"]) />
    <#else>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type-map"]) />
    </#if>

    <#assign parameters = node["b:parameters/*"]>
    <#list parameters as parameter>
      <@addTo assignTo="imports" element=getImportsFor(parameter) />
    </#list>
  </#if>

  <#return imports>
</#function>

<#function getParametersDeclaration parameters>
  <@myList list=parameters var="parameter" assignTo="parametersDeclaration">
  ${getType(parameter.@type, xml.getAttribute(parameter.@value))} ${parameter.@name}
  </@myList>
  <#return parametersDeclaration>
</#function>


<#function getReturnType operation>
  <#local returnType= "">
  <#if operation["b:return/b:type"]?is_node>
    <#local returnType = getType(operation["b:return/b:type/@type"])>
  <#else>
    <#local typeList = operation["b:return/b:type-list"]>
    <#local returnType = getType(typeList.@type, xml.getAttribute(typeList.@value))>
  </#if>
  <#return returnType>
</#function>

<#function getModifiersFrom node>
  <#return getModifiers(xml.getAttribute(node.@abstract), xml.getAttribute(node.@static), xml.getAttribute(node.@final))>
</#function>


<#macro getInterfaceProperty property>
  <#local type = getType(property.@type)>
  <@java.getInterfaceProperty type=type name=property.@name value=property.@value />
</#macro>


<#macro getProperty property>
  <#local visibility= xml.getAttribute(property.@visibility, "private")>
  <@java.getProperty visibility=visibility type=getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro initProperties property>
  <@java.initProperties type=property.@type name=property.@name value=xml.getAttribute(property.@value) key=xml.getAttribute(property.@key) />
</#macro>


<#macro getter property>
  <@java.getter type=getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro setter property>
  <@java.setter type=getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro addMethod property>
  <#if property?node_name = "property-list">
    <#local type= "${getClassName(property.@value)}">
    <@java.addListMethod type=type name=property.@name />
  <#elseif property?node_name = "property-map">
    <#local keyType= "${getClassName(property.@key)}">
    <#local valueType= "${getClassName(property.@value)}">
	<@java.addMapMethod name=property.@name keyType=keyType valueType=valueType />
  </#if>
</#macro>

<#macro removeMethod property>
  <#if property?node_name = "property-list">
    <#local type= "${getClassName(property.@value)}">
    <@java.removeListMethod type=type name=property.@name />
  <#elseif property?node_name = "property-map">
    <#local keyType= "${getClassName(property.@key)}">
    <@java.removeMapMethod name=property.@name keyType=keyType />
  </#if>
</#macro>

<#macro constructor className constructor>
  <#assign parameters = constructor["b:parameters/*"]>
  <#local visibility= xml.getAttribute(constructor.@visibility, "public")>
  <#if constructor["b:content"]?is_node>
    <@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) content=constructor["b:content"] />
  <#else>
	<@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>


<#macro interfaceOperation operation>
  <#assign parameters = operation["b:parameters/*"]>
  <@java.interfaceOperation modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
</#macro>


<#macro operation operation>
  <#assign parameters = operation["b:parameters/*"]>
  <#local visibility= xml.getAttribute(operation.@visibility, "public")>
  <#if operation["b:content"]?is_node>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) content=operation["b:content"] />
  <#else>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>


