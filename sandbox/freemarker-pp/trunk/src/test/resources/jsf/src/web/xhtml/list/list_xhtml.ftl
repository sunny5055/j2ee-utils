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
<f:view contentType="text/html">
	<ui:composition template="${util.getWebResource(config.layoutXhtmlFilePath, config.layoutXhtmlFileName)}">
		<ui:define name="headTitle">
			<h:outputText value="${sharp}{bundle.${toUnderscoreCase(modelName)?lower_case}_list_head_title}" />
		</ui:define>

		<ui:define name="searchForm">
			<h:form id="searchForm">
				<h:panelGroup id="searchFormContent">
					<h2 class="main-title" id="searchTitle">
						<h:outputText value="${sharp}{bundle.list_search_title}" />
					</h2>

					<h:panelGroup id="searchFields">
					<#list allProperties as property>
					<#if property?node_name = "column">
					<h:outputLabel value="${sharp}{bundle.${lowerModelName}_filter_${toUnderscoreCase(property.@name)?lower_case}}" />
					<@util.getXhtmlInput id="" entityName=lowerModelName path="${lowerModelName}FiltersBean" property=property useRequired=false />
					<#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many">
					<#if property?node_name = "one-to-many">
				  		<#assign propertyName = property.@name?substring(0, property.@name?length-1)>
				  	<#else>
				  		<#assign propertyName = property.@name>
				  	</#if>
					<h:outputLabel value="${sharp}{bundle.${lowerModelName}_filter_${toUnderscoreCase(propertyName)?lower_case}}" />
					<@util.getXhtmlInput id="" entityName=lowerModelName path="${lowerModelName}FiltersBean" property=property useRequired=false />
					</#if>
					</#list>
					</h:panelGroup>
				</h:panelGroup>

				<h:panelGroup id="searchFormActions">
					<p:commandButton id="searchBtn" value="${sharp}{bundle.search_btn}"
						title="${sharp}{bundle.search_btn}" icon="ui-icon-search"
						oncomplete="${lowerModelName}DataTable.filter();" />
					<p:commandButton title="${sharp}{bundle.reinit_btn}"
						actionListener="${sharp}{${lowerModelName}FiltersBean.clearFilters}"
						uppdate="searchFormContent"
						process="@this"
						icon="ui-icon-close" oncomplete="${lowerModelName}DataTable.filter();">
						<p:resetInput target="searchFormContent" />
				    </p:commandButton>
				</h:panelGroup>

				<p:defaultCommand target="searchBtn" />
			</h:form>
		</ui:define>

		<ui:define name="content">
			<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
			<p:commandButton ajax="false" value="${sharp}{bundle.new_btn}"
				title="${sharp}{bundle.new_btn}"
				icon="ui-icon-plusthick"
				action="${util.getWebResource(createXhtmlFilePath, createXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true" />
			</#if>

			<p:dataTable id="${lowerModelName}DataTable" widgetVar="${lowerModelName}DataTable"
				lazy="true" var="${lowerModelName}" sortMode="multiple" rowKey="${sharp}{${lowerModelName}.primaryKey}"
				value="${sharp}{${lowerModelName}DataTableBean.dataModel}" paginator="true"
				rows="${sharp}{${lowerModelName}DataTableBean.defaultRows}"
				rowsPerPageTemplate="${sharp}{${lowerModelName}DataTableBean.rowsPerPage}"
	            paginatorTemplate="{RowsPerPageDropdown} {FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {CurrentPageReport}"
				currentPageReportTemplate="${sharp}{bundle.list_results} : {totalRecords} ${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_list_found} (${sharp}{bundle.list_from} {startRecord} ${sharp}{bundle.list_to} {endRecord})"
				emptyMessage="${sharp}{${lowerModelName}FiltersBean.hasFilters()?bundle.list_result_empty:bundle.${lowerModelName}_list_empty}">
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
						<p:commandLink ajax="false"
							<#if util.xml.getAttribute(entity.@readOnly, "false") != "true">
        					action="${util.getWebResource(updateXhtmlFilePath, updateXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true"
							<#else>
        					action="${util.getWebResource(viewXhtmlFilePath, viewXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true"
        					</#if>
							title="${sharp}{bundle.view_btn}" styleClass="action-link view-link">
							<f:setPropertyActionListener value="${sharp}{${lowerModelName}.primaryKey}"
								target="${sharp}{flash.${lowerModelName}Id}" />
						</p:commandLink>
						<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
						<p:commandLink onclick="confirmation.show()"
								title="${sharp}{bundle.delete_btn}" styleClass="action-link delete-link">
							<f:setPropertyActionListener value="${sharp}{${lowerModelName}}" target="${sharp}{${lowerModelName}DataTableBean.selectedObject}" />
						</p:commandLink>
						</#if>
					</h:panelGroup>
				</p:column>
			</p:dataTable>
		</ui:define>

		<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
		<ui:define name="confirmDialog">
			<h:form id="confirmDialogForm">
				<p:confirmDialog id="confirmation"
					message="${sharp}{bundle.confirm_delete_content}"
					header="${sharp}{bundle.confirm_delete_title}"
					widgetVar="confirmation">
					<p:commandButton value="${sharp}{bundle.yes}"
						update=":msgs"
						actionListener="${sharp}{${lowerModelName}DataTableBean.delete}"
						oncomplete="confirmation.hide();${lowerModelName}DataTable.filter()" />
					<p:commandButton value="${sharp}{bundle.no}" onclick="confirmation.hide()"
						type="button" />
				</p:confirmDialog>
			</h:form>
		</ui:define>
		</#if>
	</ui:composition>
</f:view>
</html>
</#list>