<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign lowerEntityName = entityName?uncap_first />
<@format format=config.listXhtmlFilePath values=[projectName, lowerEntityName] assignTo="listXhtmlFilePath"/>
<@format format=config.listXhtmlFileName values=[projectName] assignTo="listXhtmlFileName"/>
<@resolveKey map=config key="createXhtmlFilePath" values=[projectName, lowerEntityName] assignTo="filePath"/>
<@resolveKey map=config key="createXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
		<h:outputText value="${sharp}{bundle.${toUnderscoreCase(entityName)?lower_case}_create_head_title}" />
	</ui:define>

	<ui:define name="content">
		<h:panelGrid columns="2" cellpadding="0" cellspacing="0">
			<#if primaryKey?node_name = "embedded-id">
			<#if embeddedIdProperties??>
			<#list embeddedIdProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<p:inputText id="${property.@name}Value"
				required="true"
				requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(lowerEntityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
				maxlength="255"
				value="${sharp}{${lowerEntityName}CreationFormBean.entity.${primaryKey.@name}.${property.@name}}"/>

			</#list>
			</#if>
			</#if>
			<#list allProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<p:inputText id="${property.@name}Value"
				required="true"
				requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(lowerEntityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
				maxlength="255"
				value="${sharp}{${lowerEntityName}CreationFormBean.entity.${property.@name}}"/>

			</#list>
		</h:panelGrid>

		<h:panelGroup id="formActions">
			<p:commandButton id="createButton" value="${sharp}{bundle.form_save}"
				icon="ui-icon-disk"
				actionListener="${sharp}{${lowerEntityName}CreationFormBean.create}" />

			<p:commandButton value="${sharp}{bundle.form_cancel}"
				immediate="true" process="@this"
				action="${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true" />
		</h:panelGroup>
	</ui:define>
</ui:composition>
</html>
</#list>