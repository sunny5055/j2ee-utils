<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign lowerEntityName = entityName?uncap_first />
<@format format=config.listXhtmlFilePath values=[lowerEntityName] assignTo="listXhtmlFilePath"/>
<@format format=config.listXhtmlFileName values=[projectName] assignTo="listXhtmlFileName"/>
<@resolveKey map=config key="updateXhtmlFilePath" values=[projectName, lowerEntityName] assignTo="filePath"/>
<@resolveKey map=config key="updateXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
		<h:outputText value="${sharp}{bundle.${toUnderscoreCase(entityName)?lower_case}_update_head_title}" />
	</ui:define>

	<ui:define name="content">
		<h:panelGrid columns="2" cellpadding="0" cellspacing="0">
			<#if primaryKey?node_name = "embedded-id">
			<#if embeddedIdProperties??>
			<#list embeddedIdProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<util:inplace id="${property.@name}" value="${sharp}{${lowerEntityName}FormBean.entity.${primaryKey.@name}.${property.@name}}">
				<@util.getXhtmlInput entityName=lowerEntityName path="${lowerEntityName}FormBean.entity.${primaryKey.@name}" property=property />
			</util:inplace>

			</#list>
			</#if>
			<#else>
			<util:formLabel
				forId="${primaryKey.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(primaryKey.@name)?lower_case}}" />
				<@util.getXhtmlInput entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=primaryKey/>

			</#if>
			<#list allProperties as property>
			<util:formLabel
				forId="${property.@name}Value"
				value="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}" />
			<#if property?node_name == "one-to-many" || property?node_name == "many-to-many">
			<h:panelGroup id="${property.@name}">
				<h:panelGroup rendered="${sharp}{false}">
					<@util.getXhtmlOutput entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=property />
				</h:panelGroup>
				<h:panelGroup rendered="${sharp}{true}">
					<@util.getXhtmlInput entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=property />
				</h:panelGroup>
			</h:panelGroup>
			<#else>
			<util:inplace id="${property.@name}" value="${sharp}{${lowerEntityName}FormBean.entity.${property.@name}}">
				<@util.getXhtmlInput entityName=lowerEntityName path="${lowerEntityName}FormBean.entity" property=property />
			</util:inplace>
			</#if>
			</#list>
		</h:panelGrid>

		<h:panelGroup id="formActions">
			<p:commandButton immediate="true" process="@this"
				value="${sharp}{bundle.form_update}" icon="ui-icon-pencil" />

			<p:commandButton id="updateBtn"
				value="${sharp}{bundle.form_save}" icon="ui-icon-disk" />

			<p:commandButton immediate="true" process="@this"
				value="${sharp}{bundle.form_cancel}" />
		</h:panelGroup>
	</ui:define>
</ui:composition>
</html>
</#list>