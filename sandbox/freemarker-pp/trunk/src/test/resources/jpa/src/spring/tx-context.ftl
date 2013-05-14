<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/spring.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:projectName"]/>
<@resolveKey map=config key="springTxFilePath" value=projectName assignTo="filePath"/>
<@resolveKey map=config key="springTxFileName" value=projectName assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign basePackages = util.getEntityPackageNames(xml) />
<@format format=config.serviceFileName value="*" assignTo="serviceName"/>
<#assign serviceName = serviceName?replace(".java", "")/>
<@format format=config.daoFileName value="*" assignTo="daoName"/>
<#assign daoName = daoName?replace(".java", "")/>
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

	<aop:config>
		<aop:pointcut id="servicePointcut"
			expression="
			<#list basePackages as basePackage>execution(* ${basePackage}..${serviceName}*.*(..))<#if basePackage_has_next> or </#if></#list>
			" />
		<aop:pointcut id="daoPointcut" expression="
			<#list basePackages as basePackage>execution(* ${basePackage}..${daoName}*.*(..))<#if basePackage_has_next> or </#if></#list>
			" />

		<aop:advisor advice-ref="serviceTxAdvice" pointcut-ref="servicePointcut" />
		<aop:advisor advice-ref="daoTxAdvice" pointcut-ref="daoPointcut" />
	</aop:config>

	<tx:advice id="serviceTxAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="get*" read-only="true" propagation="REQUIRED" />
			<tx:method name="find*" read-only="true" propagation="REQUIRED" />
			<tx:method name="count*" read-only="true" propagation="REQUIRED" />
			<tx:method name="exist*" read-only="true" propagation="REQUIRED" />
			<tx:method name="*" propagation="REQUIRED" />
		</tx:attributes>
	</tx:advice>
	<tx:advice id="daoTxAdvice" transaction-manager="transactionManager">
		<tx:attributes>
			<tx:method name="get*" read-only="true" propagation="MANDATORY" />
			<tx:method name="find*" read-only="true" propagation="MANDATORY" />
			<tx:method name="count*" read-only="true" propagation="MANDATORY" />
			<tx:method name="exist*" read-only="true" propagation="MANDATORY" />
			<tx:method name="*" propagation="MANDATORY" />
		</tx:attributes>
	</tx:advice>
</beans>