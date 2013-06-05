<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/web/xhtml/includes/xhtml.inc" as util />
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="headerXhtmlFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="headerXhtmlFileName" values=[projectName] assignTo="fileName"/>
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
	<h:body>
		<ui:composition>
			<h:form id="menuForm">
				<p:menubar>
				<#compress>
				<#list entities as entity>
				<#include "/common/assign.inc" />
				<p:menuitem value="${sharp}{bundle.menu_${toUnderscoreCase(lowerModelName)?lower_case}}"
					ajax="false"
					action="${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}?faces-redirect=true&amp;includeViewParams=true" />
				</#list>
				</#compress>
				</p:menubar>
			</h:form>
		</ui:composition>
	</h:body>
</f:view>
</html>