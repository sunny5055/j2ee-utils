<#if entity??>
	<#assign interfaces = entity["./j:interface"]>
	<#assign primaryKey = util.getPrimaryKey(entity)>
	<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
	<#assign allProperties = entity["./j:properties/*"]>
	<#assign columns = entity["./j:properties/j:column"]>
	<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
	<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
	<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
	<#assign manyToManys = entity["./j:properties/j:many-to-many"]>
	<#assign constructors = entity["./j:constructor"]>
	<#assign operations = entity["./j:operation"]>

	<#assign allConstraints=entity["./j:constraints/*"] />
	<#assign uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />

	<#assign entityPackageName = entity["ancestor::p:package/@name"] />
	<#assign entityName = util.getEntityName(entity.@name) />
	<#assign lowerEntityName = entityName?uncap_first />

	<#assign modelPackageName =  util.getModelPackageName(entityPackageName) />
	<#assign modelName = util.getModelName(entity.@name) />
	<#assign lowerModelName = modelName?uncap_first />

	<#assign daoPackageName = util.getDaoPackageName(entityPackageName) />
	<#assign daoName = util.getDaoName(entity.@name) />

	<#assign daoImplPackageName = util.getDaoImplPackageName(entityPackageName) />
	<#assign daoImplName = util.getDaoImplName(entity.@name) />

	<#assign servicePackageName = util.getServicePackageName(entityPackageName) />
	<#assign serviceName = util.getServiceName(entity.@name) />

	<#assign serviceImplPackageName = util.getServiceImplPackageName(entityPackageName) />
	<#assign serviceImplName = util.getServiceImplName(entity.@name) />

	<#assign testServiceName = util.getTestServiceName(entity.@name) />
	<@format format=config.testXmlDatasetFileName values=[modelName] assignTo="testXmlDatasetFileName"/>
</#if>
