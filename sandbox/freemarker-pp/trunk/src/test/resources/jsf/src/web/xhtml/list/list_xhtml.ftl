<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#include "/common/assign.inc" />
<@resolveKey map=config key="listXhtmlFilePath" values=[projectName, lowerModelName] assignTo="filePath"/>
<@resolveKey map=config key="listXhtmlFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
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
		<p:dataTable id="${lowerModelName}DataTable" widgetVar="${lowerModelName}DataTable"
			lazy="true" var="${lowerModelName}" sortMode="multiple" rowKey="${sharp}{${lowerModelName}.primaryKey}"
			value="${sharp}{${lowerModelName}DataTableBean.dataModel}" paginator="true"
			emptyMessage="${sharp}{${lowerModelName}DataTableBean.listEmpty}">
			<p:column headerText="${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_list_${toUnderscoreCase(primaryKey.@name)?lower_case}}" sortBy="${sharp}{${lowerModelName}.${primaryKey.@name}}">
				<@util.getXhtmlOutput entityName=lowerModelName property=primaryKey/>
			</p:column>
			<#list allProperties as property>
			<p:column headerText="${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_list_${toUnderscoreCase(property.@name)?lower_case}}" sortBy="${sharp}{${lowerModelName}.${property.@name}}">
				<@util.getXhtmlOutput entityName=lowerModelName property=property/>
			</p:column>
			</#list>

			<p:column style="width:30px">
				<h:panelGroup>
					<p:commandLink ajax="false" action="${sharp}{${lowerModelName}DataTableBean.view}"
						title="${sharp}{bundle.list_display}" icon="ui-icon-search">
						<f:setPropertyActionListener value="${sharp}{${lowerModelName}}"
							target="${sharp}{${lowerModelName}DataTableBean.selectedObject}" />
					</p:commandLink>
				</h:panelGroup>
			</p:column>
		</p:dataTable>
	</ui:define>
</ui:composition>
</html>
</#list>