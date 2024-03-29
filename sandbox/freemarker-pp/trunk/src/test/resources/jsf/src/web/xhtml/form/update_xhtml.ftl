<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly, "false") != "true">
<#include "/common/assign.inc" />
<@resolveKey map=config key="updateXhtmlFilePath" values=[projectName, lowerModelName] assignTo="filePath"/>
<@resolveKey map=config key="updateXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
	xmlns:o="http://omnifaces.org/ui"
	xmlns:of="http://omnifaces.org/functions"
	xmlns:util="http://java.sun.com/jsf/composite/components">
<f:view contentType="text/html">
	<ui:composition template="${util.getWebResource(config.layoutXhtmlFilePath, config.layoutXhtmlFileName)}">
		<ui:define name="headTitle">
			<h:outputText value="${sharp}{bundle.${toUnderscoreCase(modelName)?lower_case}_update_head_title}" />
		</ui:define>

		<ui:define name="content">
			<h:panelGrid columns="2" cellpadding="0" cellspacing="0">
				<util:formLabel
					forId="${primaryKey.@name}Value"
					value="${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_form_${toUnderscoreCase(primaryKey.@name)?lower_case}}" />
					<@util.getXhtmlInput entityName=lowerModelName path="${lowerModelName}FormBean.model" property=primaryKey/>

				<#list allProperties as property>
				<util:formLabel
					forId="${property.@name}Value"
					value="${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}"
					<#if util.xml.getAttribute(property.@nullable) == "false">required="${sharp}{true and ${lowerModelName}FormBean.editionMode}"</#if> />
				<#if property?node_name == "one-to-many" || property?node_name == "many-to-many">
				<h:panelGroup id="${property.@name}">
					<h:panelGroup rendered="${sharp}{false}">
						<@util.getXhtmlOutput entityName=lowerModelName path="${lowerModelName}FormBean.model" property=property />
					</h:panelGroup>
					<h:panelGroup rendered="${sharp}{true}">
						<@util.getXhtmlInput entityName=lowerModelName path="${lowerModelName}FormBean.model" property=property />
					</h:panelGroup>
				</h:panelGroup>
				<#else>
				<util:inplace id="${property.@name}" value="${sharp}{${lowerModelName}FormBean.model.${property.@name}}"
					editable="${sharp}{${lowerModelName}FormBean.editionMode}">
					<@util.getXhtmlInput entityName=lowerModelName path="${lowerModelName}FormBean.model" property=property />
				</util:inplace>
				</#if>
				</#list>
			</h:panelGrid>

			<h:panelGroup id="formActions">
				<p:commandButton immediate="true" process="@this" value="${sharp}{bundle.update_btn}"
					actionListener="${sharp}{${lowerModelName}FormBean.editionMode}" update=":contentForm"
					icon="ui-icon-pencil" rendered="${sharp}{!${lowerModelName}FormBean.editionMode}" />
				<p:commandButton immediate="true" process="@this" value="${sharp}{bundle.return_btn}"
					action="${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true"
					rendered="${sharp}{!${lowerModelName}FormBean.editionMode}" />

				<p:commandButton id="deleteBtn" onclick="confirmation.show()"
					value="${sharp}{bundle.delete_btn}"
					rendered="${sharp}{${lowerModelName}FormBean.editionMode}" />
				<p:commandButton id="updateBtn" value="${sharp}{bundle.save_btn}"
					icon="ui-icon-disk"  rendered="${sharp}{${lowerModelName}FormBean.editionMode}"
					actionListener="${sharp}{${lowerModelName}FormBean.update}" update=":contentForm" />
				<p:commandButton immediate="true" process="@this"
					value="${sharp}{bundle.cancel_btn}" update=":contentForm"
					actionListener="${sharp}{${lowerModelName}FormBean.reInit}"  rendered="${sharp}{${lowerModelName}FormBean.editionMode}"  />
			</h:panelGroup>
		</ui:define>

		<ui:define name="confirmDialog">
			<h:form id="confirmDialogForm">
				<p:confirmDialog id="confirmation"
					message="${sharp}{bundle.confirm_delete_content}"
					header="${sharp}{bundle.confirm_delete_title}"
					widgetVar="confirmation">
					<p:commandButton value="${sharp}{bundle.yes}"
						update=":msgs"
						actionListener="${sharp}{${lowerModelName}FormBean.delete}"
						oncomplete="confirmation.hide()" />
					<p:commandButton value="${sharp}{bundle.no}" onclick="confirmation.hide()"
						type="button" />
				</p:confirmDialog>
			</h:form>
		</ui:define>
	</ui:composition>
</f:view>
</html>
</#if>
</#list>