<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
<@resolveKey map=config key="facesConfigFilePath" value=projectName assignTo="filePath"/>
<@resolveKey map=config key="facesConfigFileName" value=projectName assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<?xml version="1.0" encoding="UTF-8"?>
<faces-config version="2.0" xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xi="http://www.w3.org/2001/XInclude" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facesconfig_2_0.xsd">

	<application>
		<resource-bundle>
			<base-name>TO_COMPLETE</base-name>
			<var>bundle</var>
		</resource-bundle>
		<locale-config>
			<default-locale>fr</default-locale>
			<supported-locale>fr</supported-locale>
		</locale-config>
		<el-resolver>org.springframework.web.jsf.el.SpringBeanFacesELResolver</el-resolver>
	</application>
	<factory>
		<exception-handler-factory>TO_COMPLETE</exception-handler-factory>
	</factory>
</faces-config>
