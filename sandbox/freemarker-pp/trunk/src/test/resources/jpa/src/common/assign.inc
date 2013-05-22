<#if entity??>
	<#assign entityPackageName = entity["ancestor::p:package/@name"] />
	<#assign entityName = util.getEntityName(entity.@name) />

	<#assign daoPackageName = util.getDaoPackageName(entityPackageName) />
	<#assign daoName = util.getDaoName(entity.@name) />

	<#assign daoImplPackageName = util.getDaoImplPackageName(entityPackageName) />
	<#assign daoImplName = util.getDaoImplName(entity.@name) />

	<#assign servicePackageName = util.getServicePackageName(entityPackageName) />
	<#assign serviceName = util.getServiceName(entity.@name) />

	<#assign serviceImplPackageName = util.getServiceImplPackageName(entityPackageName) />
	<#assign serviceImplName = util.getServiceImplName(entity.@name) />

	<#assign testServiceName = util.getTestServiceName(entity.@name) />
	<@resolveKey map=config key="testXmlDatasetFileName" values=[entityName] assignTo="testXmlDatasetFileName"/>

	<#assign interfaces = entity["./j:interface"]>
	<#assign primaryKey = util.getPrimaryKey(entity)>
	<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
	<#assign allProperties = entity["/j:properties/*"]>
	<#assign columns = entity["./j:properties/j:column"]>
	<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
	<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
	<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
	<#assign manyToManys = entity["./j:properties/j:many-to-many"]>
	<#assign constructors = entity["./j:constructor"]>
	<#assign operations = entity["./j:operation"]>
</#if>
