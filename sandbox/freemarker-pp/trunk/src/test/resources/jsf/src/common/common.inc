<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/xml.inc" as xml>
<#import "/common/java.inc" as java>
<#import "/common/directives.inc" as dir>


<#function getImportsFor node>
  <#assign imports = [] />
  <#if node?node_name = "property" || node?node_name = "type" || node?node_name = "parameter">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "property-list" || node?node_name = "type-list" || node?node_name = "parameter-list">
      <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
  <#elseif node?node_name = "property-map" || node?node_name = "type-map" || node?node_name = "parameter-map">
      <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@key) />
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
  <#elseif node?node_name = "column">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "many-to-one">
      <@addTo assignTo="imports" element=node.@targetEntity />
  <#elseif node?node_name = "one-to-many" || node?node_name = "many-to-many">
      <@addTo assignTo="imports" element=xml.getAttribute(node.@listType) />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@targetEntity) />
  </#if>
  <#return imports>
</#function>


<#function getPrimaryKey entity>
  <#if entity["j:id"]?is_node>
    <#local primaryKey = entity["j:id"]>
  <#else>
    <#local primaryKey = entity["j:embedded-id"]>
  </#if>
  <#return primaryKey>
</#function>


<#function getPrimaryKeyType entity>
  <#local primaryKeyType= "">
  <#if entity["j:id"]?is_node>
    <#local primaryKeyType = getType(entity["j:id/@type"])>
  <#else>
    <#local primaryKeyType = getEmbeddedIdName(getType(entity["j:embedded-id/@targetEntity"])) >
  </#if>
  <#return primaryKeyType>
</#function>


<#function getParametersDeclaration parameters>
  <@myList list=parameters var="parameter" assignTo="parametersDeclaration">
  ${getType(parameter.@type, xml.getAttribute(parameter.@value))} ${parameter.@name}
  </@myList>
  <#return parametersDeclaration>
</#function>


<#function getReturnType operation>
  <#local returnType= "">
  <#if operation["j:return/j:type"]?is_node>
    <#local returnType = getType(operation["j:return/j:type/@type"])>
  <#else>
    <#local typeList = operation["j:return/j:type-list"]>
    <#local returnType = getType(typeList.@type, xml.getAttribute(typeList.@value))>
  </#if>
  <#return returnType>
</#function>

<#function getModifiersFrom node>
  <#return getModifiers(xml.getAttribute(node.@abstract), xml.getAttribute(node.@static), xml.getAttribute(node.@final))>
</#function>


<#function getClassPathResource path fileName>
	<#local classPathResource = ""/>

	<#local classPathResource =  path />
	<#local classPathResource =  classPathResource?replace("{moduleApi}", "") />
	<#local classPathResource =  classPathResource?replace("{moduleService}", "") />
	<#local classPathResource =  classPathResource?replace("{moduleWeb}", "") />

	<#local classPathResource =  classPathResource?replace("{javaPath}", "") />
	<#local classPathResource =  classPathResource?replace("{resourcesPath}", "") />
	<#local classPathResource =  classPathResource?replace("{testJavaPath}", "") />
	<#local classPathResource =  classPathResource?replace("{testResourcesPath}", "") />

	<#list 1..100000000 as x>
		<#if classPathResource?starts_with("/")>
			<#local classPathResource =  classPathResource?substring(1) />
		<#else>
			<#break>
		</#if>
	</#list>
	<#list 1..100000000 as x>
		<#if classPathResource?starts_with("\\")>
			<#local classPathResource =  classPathResource?substring(2) />
		<#else>
			<#break>
		</#if>
	</#list>

	<#list 1..100000000 as x>
		<#if classPathResource?ends_with("/")>
			<#local classPathResource =  classPathResource?substring(0, classpathResource?length-1) />
		<#else>
			<#break>
		</#if>
	</#list>
	<#list 1..100000000 as x>
		<#if classPathResource?ends_with("\\")>
			<#local classPathResource =  classPathResource?substring(0, classpathResource?length-2) />
		<#else>
			<#break>
		</#if>
	</#list>

	<#local classPathResource =  classPathResource + "/" + fileName />

	<#return classPathResource/>
</#function>


<#function getEntityPackageNames xml>
	<#local entityPackageNames = [] />
	<#local entities = xml["//j:entity"]/>
	<#list entities as entity>
		<#local entityPackageName = entity["ancestor::p:package/@name"] />
		<#local entityPackageNames = dir.addToList(entityPackageNames, entityPackageName, true)  />
	</#list>

	<#return entityPackageNames />
</#function>