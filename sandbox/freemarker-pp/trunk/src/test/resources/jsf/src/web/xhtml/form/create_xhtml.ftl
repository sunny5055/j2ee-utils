<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly, "false") != "true">
<#include "/common/assign.inc" />
<@resolveKey map=config key="createXhtmlFilePath" values=[projectName, lowerModelName] assignTo="filePath"/>
<@resolveKey map=config key="createXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
			<h:outputText value="${sharp}{bundle.${toUnderscoreCase(modelName)?lower_case}_create_head_title}" />
		</ui:define>

		<ui:define name="content">
			<h:panelGrid columns="2" cellpadding="0" cellspacing="0">
				<#list allProperties as property>
				<util:formLabel
					forId="${property.@name}Value"
					value="${sharp}{bundle.${toUnderscoreCase(lowerModelName)?lower_case}_form_${toUnderscoreCase(property.@name)?lower_case}}"
					<#if util.xml.getAttribute(property.@nullable) == "false">required="${sharp}{true}"</#if> />
	 			<@util.getXhtmlInput entityName=lowerModelName path="${lowerModelName}FormBean.model" property=property />


				</#list>
			</h:panelGrid>

			<h:panelGroup id="formActions">
				<p:commandButton id="createButton" value="${sharp}{bundle.save_btn}"
					icon="ui-icon-disk"
					actionListener="${sharp}{${lowerModelName}FormBean.create}" />

				<p:commandButton value="${sharp}{bundle.cancel_btn}"
					immediate="true" process="@this"
					action="${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true" />
			</h:panelGroup>
		</ui:define>
	</ui:composition>
</f:view>
</html>
</#if>
</#list>