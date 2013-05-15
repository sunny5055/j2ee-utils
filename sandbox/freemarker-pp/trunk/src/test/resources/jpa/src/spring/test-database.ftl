<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/spring.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="springTestDatabaseFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="springTestDatabaseFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
# Jdbc
jdbc.driverClassName=org.hsqldb.jdbcDriver
jdbc.url=jdbc:hsqldb:mem:${projectName}
jdbc.username=sa
jdbc.password=
jdbc.maxActive=100
jdbc.maxWait=1000

# Hibernate
hibernate.dialect=org.hibernate.dialect.HSQLDialect
hibernate.show_sql=true
hibernate.hbm2ddl.auto=create

# c3p0
c3p0.acquire_increment=1
c3p0.idle_test_period=100
c3p0.min_size=10
c3p0.max_size=100
c3p0.timeout=100
c3p0.max_statements=0

