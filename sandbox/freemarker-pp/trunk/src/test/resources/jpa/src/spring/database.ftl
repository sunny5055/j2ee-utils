<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/spring.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="springDatabaseFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="springDatabaseFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
# Jdbc
<#if config.database == "postgresql">
jdbc.driverClassName=org.postgresql.Driver
jdbc.url=jdbc:postgresql://localhost:5432/${projectName}
<#elseif config.database == "oracle">
jdbc.driverClassName=oracle.jdbc.driver.OracleDriver
jdbc.url=jdbc:oracle:thin:@host:1521:${projectName}
<#elseif config.database == "mysql">
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/${projectName}
</#if>
jdbc.username=#TODO to define
jdbc.password=#TODO to define
jdbc.maxActive=100
jdbc.maxWait=1000

# Hibernate
<#if config.database == "postgresql">
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
<#elseif config.database == "oracle">
hibernate.dialect=org.hibernate.dialect.OracleDialect
<#elseif config.database == "mysql">
hibernate.dialect=org.hibernate.dialect.MySQLDialect
</#if>
hibernate.show_sql=false
hibernate.hbm2ddl.auto=update

# c3p0
c3p0.acquire_increment=1
c3p0.idle_test_period=100
c3p0.min_size=10
c3p0.max_size=100
c3p0.timeout=100
c3p0.max_statements=0

