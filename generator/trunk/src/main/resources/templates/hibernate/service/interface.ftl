<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if servicePackageName??>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.GenericService" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${packageName}.${primaryKey.@targetEntity}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0>
	<#list manyToOnes as manyToOne>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToOne) />
	</#list>
</#if>
<#if oneToManys?size gt 0>
	<#list oneToManys as oneToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(oneToMany) />
	</#list>
</#if>
<#if manyToManys?size gt 0>
	<#list manyToManys as manyToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToMany) />
	</#list>
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(true, servicePackageName, imports)}


public interface ${serviceName} extends GenericService<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {

<@util.getMethodName entity=entity property=primaryKey/>
<#list columns as column>
<@util.getMethodName entity=entity property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getMethodName entity=entity property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getMethodName entity=entity property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getMethodName entity=entity property=manyToMany/>
</#list>
}

