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


<#function getBasePackageName packageName>
    <#local basePackageName = packageName>
	<#if config.entityPackage??>
		<#local basePackageName = basePackageName?replace(config.entityPackage, "") />
	</#if>

    <#return basePackageName />
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



<#function getWebResource path fileName>
	<#local webResource = ""/>

	<#local webResource =  path />
	<#local webResource =  webResource?replace("{moduleWeb}", "") />
	<#local webResource =  webResource?replace("{webappPath}", "") />

	<#list 1..100000000 as x>
		<#if webResource?starts_with("/")>
			<#local webResource =  webResource?substring(1) />
		<#else>
			<#break>
		</#if>
	</#list>
	<#list 1..100000000 as x>
		<#if webResource?starts_with("\\")>
			<#local webResource =  webResource?substring(2) />
		<#else>
			<#break>
		</#if>
	</#list>

	<#list 1..100000000 as x>
		<#if webResource?ends_with("/")>
			<#local webResource =  webResource?substring(0, classpathResource?length-1) />
		<#else>
			<#break>
		</#if>
	</#list>
	<#list 1..100000000 as x>
		<#if webResource?ends_with("\\")>
			<#local webResource =  webResource?substring(0, classpathResource?length-2) />
		<#else>
			<#break>
		</#if>
	</#list>

	<#local webResource =  "/" + webResource + "/" + fileName />

	<#return webResource/>
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


<#function getFormBeanPackageName packageName>
    <#local formBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local formBeanPackageName = formBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.formBeanPackage??>
		<#local formBeanPackageName = formBeanPackageName + config.formBeanPackage />
	</#if>

    <#return formBeanPackageName />
</#function>


<#function getFormBeanName value>
    <@format format=config.formBeanFileName values=[value] assignTo="formBeanName"/>
	<#return formBeanName?replace(".java", "") />
</#function>


<#function getListBeanPackageName packageName>
    <#local listBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local listBeanPackageName = listBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.listBeanPackage??>
		<#local listBeanPackageName = listBeanPackageName + config.listBeanPackage />
	</#if>

    <#return listBeanPackageName />
</#function>


<#function getListBeanName value>
    <@format format=config.listBeanFileName values=[value] assignTo="listBeanName"/>
	<#return listBeanName?replace(".java", "") />
</#function>


<#function getDataTableBeanPackageName packageName>
    <#local dataTableBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local dataTableBeanPackageName = dataTableBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.dataTableBeanPackage??>
		<#local dataTableBeanPackageName = dataTableBeanPackageName + config.dataTableBeanPackage />
	</#if>

    <#return dataTableBeanPackageName />
</#function>


<#function getDataTableBeanName value>
    <@format format=config.dataTableBeanFileName values=[value] assignTo="dataTableBeanName"/>
	<#return dataTableBeanName?replace(".java", "") />
</#function>


<#function getFiltersBeanPackageName packageName>
    <#local filtersBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local filtersBeanPackageName = filtersBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.filtersBeanPackage??>
		<#local filtersBeanPackageName = filtersBeanPackageName + config.filtersBeanPackage />
	</#if>

    <#return filtersBeanPackageName />
</#function>


<#function getFiltersBeanName value>
    <@format format=config.filtersBeanFileName values=[value] assignTo="filtersBeanName"/>
	<#return filtersBeanName?replace(".java", "") />
</#function>


<#function getDataModelBeanPackageName packageName>
    <#local dataModelBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local dataModelBeanPackageName = dataModelBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.dataModelBeanPackage??>
		<#local dataModelBeanPackageName = dataModelBeanPackageName + config.dataModelBeanPackage />
	</#if>

    <#return dataModelBeanPackageName />
</#function>


<#function getDataModelBeanName value>
    <@format format=config.dataModelBeanFileName values=[value] assignTo="dataModelBeanName"/>
	<#return dataModelBeanName?replace(".java", "") />
</#function>


<#function getConverterBeanPackageName packageName>
    <#local converterBeanPackageName = packageName>
	<#if config.entityPackage??>
		<#local converterBeanPackageName = converterBeanPackageName?replace(config.entityPackage, "") />
	</#if>
	<#if config.converterBeanPackage??>
		<#local converterBeanPackageName = converterBeanPackageName + config.converterBeanPackage />
	</#if>

    <#return converterBeanPackageName />
</#function>


<#function getConverterBeanName value>
    <@format format=config.converterBeanFileName values=[value] assignTo="converterBeanName"/>
	<#return converterBeanName?replace(".java", "") />
</#function>
