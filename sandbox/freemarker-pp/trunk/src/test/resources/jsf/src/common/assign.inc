<#if entity??>
	<#assign primaryKey = util.getPrimaryKey(entity)>
	<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
	<#assign allProperties = entity["./j:properties/*"]>
	<#assign columns = entity["./j:properties/j:column"]>
	<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
	<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
	<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
	<#assign manyToManys = entity["./j:properties/j:many-to-many"]>

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

	<#assign converterBeanPackageName = util.getConverterBeanPackageName(entityPackageName) />
	<#assign converterBeanName = util.getConverterBeanName(entity.@name) />

	<#assign dataTableBeanPackageName = util.getDataTableBeanPackageName(entityPackageName) />
	<#assign dataTableBeanName = util.getDataTableBeanName(entity.@name) />

	<#assign filtersBeanPackageName = util.getFiltersBeanPackageName(entityPackageName) />
	<#assign filtersBeanName = util.getFiltersBeanName(entity.@name) />

	<#assign dataModelBeanPackageName = util.getDataModelBeanPackageName(entityPackageName) />
	<#assign dataModelBeanName = util.getDataModelBeanName(entity.@name) />

	<#assign formBeanPackageName = util.getFormBeanPackageName(entityPackageName) />
	<#assign formBeanName = util.getFormBeanName(entity.@name) />

	<#assign listBeanPackageName = util.getListBeanPackageName(entityPackageName) />
	<#assign listBeanName = util.getListBeanName(entity.@name) />

	<@format format=config.resourceBundleFilePath values=[projectName] assignTo="resourceBundleFilePath"/>
	<@format format=config.resourceBundleFileName values=[projectName] assignTo="resourceBundleFileName"/>
	<@format format=config.listXhtmlFilePath values=[lowerModelName] assignTo="listXhtmlFilePath"/>
	<@format format=config.listXhtmlFileName values=[projectName] assignTo="listXhtmlFileName"/>
	<@format format=config.createXhtmlFilePath values=[lowerModelName] assignTo="createXhtmlFilePath"/>
	<@format format=config.createXhtmlFileName values=[projectName] assignTo="createXhtmlFileName"/>
	<@format format=config.updateXhtmlFilePath values=[lowerModelName] assignTo="updateXhtmlFilePath"/>
	<@format format=config.updateXhtmlFileName values=[projectName] assignTo="updateXhtmlFileName"/>
	<@format format=config.viewXhtmlFilePath values=[lowerModelName] assignTo="viewXhtmlFilePath"/>
	<@format format=config.viewXhtmlFileName values=[projectName] assignTo="viewXhtmlFileName"/>
</#if>
