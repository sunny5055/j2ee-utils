<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/spring.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:projectName"]/>
<@resolveKey map=config key="springJpaFilePath" value=projectName assignTo="filePath"/>
<@resolveKey map=config key="springJpaFileName" value=projectName assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
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

 	<bean id="entityManagerFactory"
		class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="persistenceUnitName" value="${projectName}" />
		<property name="jpaVendorAdapter">
   			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"/>
  		</property>
		<#noparse>
		<property name="jpaProperties">
		        <props>
		                <prop key="hibernate.dialect">${hibernate.dialect}</prop>
		                <prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
		                <prop key="hibernate.show_sql">${hibernate.show_sql}</prop>
		        </props>
		</property>
		</#noparse>
    </bean>

	<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
     	<property name="entityManagerFactory" ref="entityManagerFactory" />
		<property name="nestedTransactionAllowed" value="true" />
    </bean>
</beans>
