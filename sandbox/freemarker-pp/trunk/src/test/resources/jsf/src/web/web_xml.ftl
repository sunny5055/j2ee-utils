<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
<@resolveKey map=config key="webXmlFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="webXmlFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
	<display-name>${projectName}</display-name>
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:${util.getClassPathResource(config.springApplicationFilePath, config.springApplicationFileName)}</param-value>
	</context-param>
	<context-param>
		<param-name>javax.faces.DEFAULT_SUFFIX</param-name>
		<param-value>.xhtml</param-value>
	</context-param>
	<context-param>
		<param-name>javax.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL</param-name>
		<param-value>true</param-value>
	</context-param>
	<context-param>
		<param-name>javax.faces.PROJECT_STAGE</param-name>
		<param-value>Development</param-value>
	</context-param>
	<context-param>
		<param-name>primefaces.THEME</param-name>
		<param-value>aristo</param-value>
	</context-param>

	<filter>
		<filter-name>entityManagerFilter</filter-name>
		<filter-class>org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>entityManagerFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
	<listener>
		<listener-class>org.springframework.web.context.request.RequestContextListener</listener-class>
	</listener>

	<servlet>
		<servlet-name>Faces Servlet</servlet-name>
		<servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>Faces Servlet</servlet-name>
		<url-pattern>*.jsf</url-pattern>
	</servlet-mapping>
	<servlet-mapping>
		<servlet-name>Faces Servlet</servlet-name>
		<url-pattern>/faces/*</url-pattern>
	</servlet-mapping>

	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>
</web-app>