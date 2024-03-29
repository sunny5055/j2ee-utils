<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/xml.inc" as xml>
<#import "/common/java.inc" as java>
<#import "/common/directives.inc" as dir>


<#function getImportsFor node jpa=false>
  <#assign imports = [] />
  <#if node?node_name = "column">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "many-to-one">
  	  <#local targetPackage = getPackage(node.@targetEntity)/>
  	  <#local targetName = getClassName(node.@targetEntity) />
      <#if targetPackage?length == 0>
      	<#local targetPackage = node["ancestor::p:package/@name"] />
      </#if>

      <#if jpa == true>
      	<@addTo assignTo="imports" element=targetPackage+"."+getEntityName(targetName) />
      <#else>
      	<@addTo assignTo="imports" element=getModelPackageName(targetPackage)+"."+getModelName(targetName) />
  	  </#if>
  <#elseif node?node_name = "one-to-many" || node?node_name = "many-to-many">
      <@addTo assignTo="imports" element=xml.getAttribute(node.@listType) />
      <#local targetPackage = getPackage(node.@targetEntity)/>
  	  <#local targetName = getClassName(node.@targetEntity) />
      <#if targetPackage?length == 0>
      	<#local targetPackage = node["ancestor::p:package/@name"] />
      </#if>

      <#if jpa == true>
      	<@addTo assignTo="imports" element=targetPackage+"."+getEntityName(targetName) />
      <#else>
      	<@addTo assignTo="imports" element=getModelPackageName(targetPackage)+"."+getModelName(targetName) />
  	  </#if>
  </#if>
  <#return imports>
</#function>



<#function getPrimaryKey entity>
	<#return  entity["j:id"]>
</#function>


<#function getPrimaryKeyType entity>
  <#return getType(entity["j:id/@type"])>
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
  <#if property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local prefix = "${entity.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local joinColumnName= "${toUnderscoreCase(prefix+'_'+entity.@name?lower_case+'_id')}">
  </#if>
  <#return joinColumnName>
</#function>

<#function getInverseJoinColumnName entity property>
  <#local inverseJoinColumnName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local prefix = "${entity.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local inverseJoinColumnName= "${toUnderscoreCase(prefix+'_'+getClassName(property.@targetEntity)?lower_case+'_id')}">
  </#if>
  <#return inverseJoinColumnName>
</#function>

<#function getJoinTableName entity property>
  <#local joinTableName= "">
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
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
    	<#local queryName= "countBy${propertyName?cap_first}" />
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
    	<#local queryName= "findBy${propertyName?cap_first}" />
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
		<@format format=config.entityFileName values=[value]/>
	</#local>

	<#return entityName?replace(".java", "") />
</#function>


<#function getModelPackageName packageName>
    <#local modelPackageName = packageName>
	<#if config.entityPackage??>
		<#local modelPackageName = modelPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.entityPackage??>
		<#local modelPackageName = modelPackageName + config.modelPackage />
	</#if>

    <#return modelPackageName />
</#function>


<#function getModelName value>
	<#local modelName>
		<@format format=config.modelFileName values=[value]/>
	</#local>

	<#return modelName?replace(".java", "") />
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
    <@format format=config.daoFileName values=[value] assignTo="daoName"/>
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
    <@format format=config.daoImplFileName values=[value] assignTo="daoImplName"/>
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
    <@format format=config.serviceFileName values=[value] assignTo="serviceName"/>
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
    <@format format=config.serviceImplFileName values=[value] assignTo="serviceImplName"/>
	<#return serviceImplName?replace(".java", "") />
</#function>


<#function getTestServiceName value>
    <@format format=config.testServiceFileName values=[value] assignTo="testServiceName"/>
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


<#function getEntityPackageNames xml>
	<#local entityPackageNames = [] />
	<#local entities = xml["//j:entity"]/>
	<#list entities as entity>
		<#local entityPackageName = entity["ancestor::p:package/@name"] />
		<#local entityPackageNames = dir.addToList(entityPackageNames, entityPackageName, true)  />
	</#list>

	<#return entityPackageNames />
</#function>


<#function getCountQueryConstant propertyName singleResult modelName="">
	<#return java.getConstant(getCountQueryName(propertyName?cap_first, singleResult), modelName) />
</#function>


<#function getFindQueryConstant propertyName singleResult modelName="">
	<#return java.getConstant(getFindQueryName(propertyName?cap_first, singleResult), modelName) />
</#function>
