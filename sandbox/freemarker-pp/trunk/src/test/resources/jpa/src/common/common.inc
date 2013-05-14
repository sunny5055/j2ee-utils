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


<#function getColumnName columnPrefix property>
  <#local columnName= "">
  <#if xml.existAttribute(property.@column)>
    <#local columnName= "${property.@column}">
  <#else>
    <#if property?node_name = "id">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
    <#elseif property?node_name = "column">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
    <#elseif property?node_name = "many-to-one">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name+'_id')}">
    </#if>
  </#if>
  <#return columnName>
</#function>

<#function getJoinColumnName entity property>
  <#local joinColumnName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#local prefix = "${entity.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local joinColumnName= "${toUnderscoreCase(prefix+'_'+entity.@name?lower_case+'_id')}">
  </#if>
  <#return joinColumnName>
</#function>

<#function getInverseJoinColumnName entity property>
  <#local inverseJoinColumnName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#local prefix = "${entity.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local inverseJoinColumnName= "${toUnderscoreCase(prefix+'_'+getClassName(property.@targetEntity)?lower_case+'_id')}">
  </#if>
  <#return inverseJoinColumnName>
</#function>

<#function getJoinTableName entity property>
  <#local joinTableName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#if xml.existAttribute(property.@joinTable)>
      <#local joinTableName= "${property.@joinTable}">
    <#else>
      <#local prefix = "${entity.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
      <#local joinTableName= "${toUnderscoreCase(prefix+'_'+entity.@name?lower_case+'_'+getClassName(property.@targetEntity)?lower_case)}">
    </#if>
  </#if>
  <#return joinTableName>
</#function>

<#function getCountQueryName propertyName singleResult entityName="">
	<#local queryName= "">
    <#if singleResult == false>
    	<#local queryName= "countFor${propertyName?cap_first}" />
    <#else>
    	<#local queryName= "countBy${propertyName?cap_first}" />
    </#if>
    <#if entityName?length gt 0>
  		<#local queryName = "${entityName?uncap_first}.${queryName}" />
  	</#if>
    <#return queryName />
</#function>


<#function getFindQueryName propertyName singleResult entityName="">
	<#local queryName= "">
    <#if singleResult == false>
    	<#local queryName= "findAllFor${propertyName?cap_first}" />
    <#else>
    	<#local queryName= "findBy${propertyName?cap_first}" />
    </#if>
    <#if entityName?length gt 0>
  		<#local queryName = "${entityName?uncap_first}.${queryName}" />
  	</#if>
    <#return queryName />
</#function>


<#function getEntityName value>
	<#local entityName>
		<@format format=config.entityFileName value=value/>
	</#local>

	<#return entityName?replace(".java", "") />
</#function>


<#function getDaoPackageName packageName>
    <#local daoPackageName = packageName>
	<#if config.entityPackage??>
		<#local daoPackageName = daoPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.daoPackage??>
		<#local daoPackageName = daoPackageName + config.daoPackage />
	</#if>

    <#return daoPackageName />
</#function>


<#function getDaoName value>
    <@format format=config.daoFileName value=value assignTo="daoName"/>
	<#return daoName?replace(".java", "") />
</#function>


<#function getDaoImplPackageName packageName>
    <#local daoImplPackageName = packageName>
	<#if config.entityPackage??>
		<#local daoImplPackageName = daoImplPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.daoImplPackage??>
		<#local daoImplPackageName = daoImplPackageName + config.daoImplPackage />
	</#if>

    <#return daoImplPackageName />
</#function>


<#function getDaoImplName value>
    <@format format=config.daoImplFileName value=value assignTo="daoImplName"/>
	<#return daoImplName?replace(".java", "") />
</#function>


<#function getServicePackageName packageName>
    <#local servicePackageName = packageName>
	<#if config.entityPackage??>
		<#local servicePackageName = servicePackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.servicePackage??>
		<#local servicePackageName = servicePackageName + config.servicePackage />
	</#if>

    <#return servicePackageName />
</#function>


<#function getServiceName value>
    <@format format=config.serviceFileName value=value assignTo="serviceName"/>
	<#return serviceName?replace(".java", "") />
</#function>


<#function getServiceImplPackageName packageName>
    <#local serviceImplPackageName = packageName>
	<#if config.entityPackage??>
		<#local serviceImplPackageName = serviceImplPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.serviceImplPackage??>
		<#local serviceImplPackageName = serviceImplPackageName + config.serviceImplPackage />
	</#if>

    <#return serviceImplPackageName />
</#function>


<#function getServiceImplName value>
    <@format format=config.serviceImplFileName value=value assignTo="serviceImplName"/>
	<#return serviceImplName?replace(".java", "") />
</#function>


<#function getEmbeddedIdPackageName packageName>
    <#local embeddedIdPackageName = packageName>
	<#if config.entityPackage??>
		<#local embeddedIdPackageName = embeddedIdPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.embeddedIdPackage??>
		<#local embeddedIdPackageName = embeddedIdPackageName + config.embeddedIdPackage />
	</#if>

    <#return embeddedIdPackageName />
</#function>


<#function getEmbeddedIdName value>
    <@format format=config.embeddedIdFileName value=value assignTo="embeddedIdName"/>
	<#return embeddedIdName?replace(".java", "") />
</#function>


<#function getTestServiceName value>
    <@format format=config.testServiceFileName value=value assignTo="testServiceName"/>
	<#return testServiceName?replace(".java", "") />
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

