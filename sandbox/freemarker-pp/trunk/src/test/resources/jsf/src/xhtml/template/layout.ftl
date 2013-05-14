<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
<@resolveKey map=config key="layoutXhtmlFilePath" value=projectName assignTo="filePath"/>
<@resolveKey map=config key="layoutXhtmlFileName" value=projectName assignTo="fileName"/>
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
<h:head>
	<title><ui:insert name="headTitle" /> - <h:outputText value="${sharp}{bundle.global_head_title}" /></title>
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
</h:head>

<h:body>
	<f:view contentType="text/html">
		<link rel="stylesheet" href="${sharp}{resource['css:main.css']}" type="text/css" />
		<!--[if lte IE 8]><link rel="stylesheet" href="${sharp}{resource['css:main-ie.css']}" type="text/css" /><![endif]-->

		<ui:insert name="stylesheet" />

		<!--[if lt IE 7]><script type="text/javascript" src="${sharp}{resource['js:DD_belatedPNG_0.0.8a-min.js']}"></script><![endif]-->
		<!--[if IE]><script type="text/javascript" src="${sharp}{resource['js:html5.js']}"></script><![endif]-->
		<!--[if lte IE 7]><script type="text/javascript" src="${sharp}{resource['js:IE8.js']}"></script><![endif]-->

		<script type="text/javascript" src="${sharp}{resource['js:main.js']}"></script>

		<ui:insert name="javascript" />

		<ui:insert name="header">
			<ui:include src="${util.getWebResource(config.headerXhtmlFilePath, config.headerXhtmlFileName)}" />
		</ui:insert>

		<div id="wrapper">
			<ui:insert name="breadcrumb">
				<ui:include src="${util.getWebResource(config.breadcrumbXhtmlFilePath, config.breadcrumbXhtmlFileName)}" />
			</ui:insert>

			<div id="content">
				<h:form id="contentForm">
					<ui:insert name="content" />
				</h:form>
			</div>
		</div>

		<ui:insert name="footer">
			<ui:include src="${util.getWebResource(config.footerXhtmlFilePath, config.footerXhtmlFileName)}" />
		</ui:insert>


		<ui:insert name="confirmDialog" />

		<ui:insert name="ajaxStatus">
			<p:ajaxStatus onstart="statusDialog.show();"
				onsuccess="statusDialog.hide();" />
			<p:dialog modal="true" widgetVar="statusDialog" header="Status"
				draggable="false" closable="false" width="240">
				<h:graphicImage name="ajaxloadingbar.gif" library="img"
					alt="Loading bar" />
			</p:dialog>
		</ui:insert>
	</f:view>
</h:body>
</html>