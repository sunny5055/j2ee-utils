<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign lowerEntityName = entityName?uncap_first />
<@resolveKey map=config key="listXhtmlFilePath" values=[projectName, lowerEntityName] assignTo="filePath"/>
<@resolveKey map=config key="listXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
		<h:outputText value="${sharp}{bundle.${toUnderscoreCase(entityName)?lower_case}_list_head_title}" />
	</ui:define>

	<ui:define name="content">
		<p:dataTable id="${lowerEntityName}DataTable" widgetVar="${lowerEntityName}DataTable"
			lazy="true" var="${lowerEntityName}" sortMode="multiple" rowKey="${sharp}{${lowerEntityName}.primaryKey}"
			value="${sharp}{${lowerEntityName}DataTableBean.dataModel}" paginator="true"
			emptyMessage="${sharp}{${lowerEntityName}DataTableBean.listEmpty}">
			<#if primaryKey?node_name = "embedded-id">
			<#if embeddedIdProperties??>
			<#list embeddedIdProperties as property>
			<p:column headerText="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}}" sortBy="${sharp}{${lowerEntityName}.${primaryKey.@name}.${property.@name}}">
				<@util.getXhtmlOutput entityName=lowerEntityName path="${lowerEntityName}.${primaryKey.@name}" property=property/>
			</p:column>
			</#list>
			</#if>
			<#else>
			<p:column headerText="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_list_${toUnderscoreCase(primaryKey.@name)?lower_case}}" sortBy="${sharp}{${lowerEntityName}.${primaryKey.@name}}">
				<@util.getXhtmlOutput entityName=lowerEntityName property=primaryKey/>
			</p:column>
			</#if>
			<#list allProperties as property>
			<p:column headerText="${sharp}{bundle.${toUnderscoreCase(lowerEntityName)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}}" sortBy="${sharp}{${lowerEntityName}.${property.@name}}">
				<@util.getXhtmlOutput entityName=lowerEntityName property=property/>
			</p:column>
			</#list>

			<p:column style="width:30px">
				<h:panelGroup>
					<p:commandLink ajax="false" action="${sharp}{${lowerEntityName}DataTableBean.view}"
						title="${sharp}{bundle.list_display}" icon="ui-icon-search">
						<f:setPropertyActionListener value="${sharp}{${lowerEntityName}}"
							target="${sharp}{${lowerEntityName}DataTableBean.selectedObject}" />
					</p:commandLink>
				</h:panelGroup>
			</p:column>
		</p:dataTable>
	</ui:define>
</ui:composition>
</html>
</#list>