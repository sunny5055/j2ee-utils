<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign projectName = xml["//p:configuration/p:projectName"]>

# Jdbc
jdbc.driverClassName=org.postgresql.Driver
jdbc.maxActive=100
jdbc.maxWait=1000
jdbc.url=jdbc:postgresql://localhost:5432/${projectName}
jdbc.username=postgres
jdbc.password=postgres

# Hibernate
hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
hibernate.show_sql=false
hibernate.hbm2ddl.auto=update
hibernate.connection.release_mode=auto
hibernate.auto_close_session=true
hibernate.cache.use_query_cache=false


# c3p0
c3p0.acquire_increment=1
c3p0.idle_test_period=100
c3p0.min_size=10
c3p0.max_size=100
c3p0.timeout=100
c3p0.max_statements=0
