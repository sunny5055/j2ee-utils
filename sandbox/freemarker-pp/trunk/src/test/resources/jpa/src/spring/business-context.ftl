<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/spring.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="springBusinessFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="springBusinessFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign basePackages = util.getEntityPackageNames(xml) />
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd
		http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.2.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">

	<#list basePackages as basePackage>
	<context:component-scan base-package="${basePackage}" />
	</#list>
	<context:annotation-config />

	<context:property-placeholder location="classpath:${util.getClassPathResource(config.springDatabaseFilePath, config.springDatabaseFileName)}" />

	<import resource="classpath:${util.getClassPathResource(config.springJdbcFilePath, config.springJdbcFileName)}" />
	<import resource="classpath:${util.getClassPathResource(config.springJpaFilePath, config.springJpaFileName)}" />
	<import resource="classpath:${util.getClassPathResource(config.springTxFilePath, config.springTxFileName)}" />
</beans>
