<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:util="http://www.springframework.org/schema/util"
	xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd   http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd   http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc-3.2.xsd   http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-3.2.xsd   http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd   http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.2.xsd">

	<#list basePackages as basePackage>
	<context:component-scan base-package="${basePackage}" />
	</#list>
	<context:annotation-config />

	<context:property-placeholder location="classpath:${springTestDatabaseFile}" />

	<import resource="classpath:${springJpaFile}" />
	<import resource="classpath:${springTxFile}" />

	<#noparse>
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
		<property name="driverClass" value="${jdbc.driverClassName}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />
		<property name="acquireIncrement" value="${c3p0.acquire_increment}" />
		<property name="idleConnectionTestPeriod" value="${c3p0.idle_test_period}" />
		<property name="minPoolSize" value="${c3p0.min_size}" />
		<property name="maxPoolSize" value="${c3p0.max_size}" />
		<property name="loginTimeout" value="${c3p0.timeout}" />
		<property name="maxStatements" value="${c3p0.max_statements}" />
	</bean>
	</#noparse>
</beans>