<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
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
	xmlns:util="http://java.sun.com/jsf/composite/components">
<h:body>
	<ui:composition>

	</ui:composition>
</h:body>
</html>