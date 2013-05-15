<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="jpaPersistenceFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="jpaPersistenceFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign entities = xml["//j:entity"]>
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/persistence http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
	version="2.0">

	<persistence-unit name="${projectName}" transaction-type="RESOURCE_LOCAL">
		<provider>org.hibernate.ejb.HibernatePersistence</provider>

		<#list entities as entity>
			<#assign entityPackageName = entity["ancestor::p:package/@name"] />
			<#assign entityName = util.getEntityName(entity.@name) />
			<class>${entityPackageName}.${entityName}</class>
		</#list>
	</persistence-unit>
</persistence>
