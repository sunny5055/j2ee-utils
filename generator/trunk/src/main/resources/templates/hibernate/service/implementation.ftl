<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if serviceImplPackageName??>
package ${serviceImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Service" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.AbstractGenericService" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${packageName}.${primaryKey.@targetEntity}" />
</#if>

<@addTo assignTo="imports" element="java.util.List" />

${getImports(true, servicePackageName, imports)}


@Service
public class ${serviceImplName} extends AbstractGenericService<${util.getPrimaryKeyType(entity)}, ${entity.@name}, ${daoName}> implements ${serviceName} {

    @Autowired
    @Override
    public void setDao(${daoName} dao) {
        this.dao = dao;
    }
}

