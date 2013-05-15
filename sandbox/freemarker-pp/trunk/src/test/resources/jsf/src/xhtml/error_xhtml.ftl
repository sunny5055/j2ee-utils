<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
<@resolveKey map=config key="errorXhtmlFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="errorXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
		<h:outputText value="${sharp}{bundle.error_head_title}" />
	</ui:define>

	<ui:define name="content">
		<t:htmlTag value="h2" rendered="${sharp}{!empty errorBean.title}">
			<h:outputText value="${sharp}{errorBean.title}" />
		</t:htmlTag>
		<h:outputText value="${sharp}{errorBean.message}" escape="false" />

		<!-- ${sharp}{exception} -->
	</ui:define>
</ui:composition>
</html>