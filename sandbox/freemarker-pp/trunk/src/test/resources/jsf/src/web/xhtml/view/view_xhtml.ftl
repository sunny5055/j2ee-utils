<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly) == "true">
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign lowerEntityName = entityName?uncap_first />
<@format format=config.listXhtmlFilePath values=[lowerEntityName] assignTo="listXhtmlFilePath"/>
<@format format=config.listXhtmlFileName values=[projectName] assignTo="listXhtmlFileName"/>
<@resolveKey map=config key="viewXhtmlFilePath" values=[projectName, lowerEntityName] assignTo="filePath"/>
<@resolveKey map=config key="viewXhtmlFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
<#assign allProperties=entity["./j:properties/*"] />
<#assign embeddedIdProperties=entity["./j:embedded-id/j:properties/j:column"] />
<#assign columns = entity["./j:properties/j:column"]>
<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
<#assign manyToManys = entity["./j:properties/j:many-to-many"]>
<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:h="http://java.sun.com/jsf/html"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:ui="http://java.sun.com/jsf/facelets"
	xmlns:fn="http://java.sun.com/jsp/jstl/functions"
	xmlns:p="http://primefaces.org/ui"
	xmlns:pe="http://primefaces.org/ui/extensions"
	xmlns:t="http://myfaces.apache.org/tomahawk"
	xmlns:util="http://java.sun.com/jsf/composite/components">
<ui:composition template="${util.getWebResource(config.layoutXhtmlFilePath, config.layoutXhtmlFileName)}">
	<ui:define name="headTitle">
		<h:outputText value="${sharp}{bundle.${toUnderscoreCase(entityName)?lower_case}_view_head_title}" />
	</ui:define>

	<ui:define name="content">
		<h:panelGrid columns="2" cellpadding="0" cellspacing="0">
			<#if primaryKey?node_name = "embedded-id">
			<#if embeddedIdProperties??>
			<#list embeddedIdProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<@util.getXhtmlOutput id="${property.@name}Value" entityName=lowerEntityName path="${lowerEntityName}FormBean.entity.${primaryKey.@name}" property=property />

			</#list>
			</#if>
			<#else>
			<util:formLabel
				forId="${primaryKey.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(primaryKey.@name)?lower_case}}" />
				<@util.getXhtmlOutput id="${primaryKey.@name}Value" entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=primaryKey />

			</#if>
			<#list allProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<@util.getXhtmlOutput id="${property.@name}Value" entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=property />
			</#list>
		</h:panelGrid>

		<h:panelGroup id="formActions">
			<p:commandButton immediate="true" process="@this"
				value="${sharp}{bundle.back}" />
		</h:panelGroup>
	</ui:define>
</ui:composition>
</html>
</#if>
</#list>