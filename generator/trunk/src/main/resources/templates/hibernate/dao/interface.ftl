<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if daoPackageName??>
package ${daoPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericDao" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${packageName}.${primaryKey.@targetEntity}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(true, daoPackageName, imports)}

public interface ${daoName} extends GenericDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
<@util.getQueryName entity=entity property=primaryKey/>
<#list columns as column>
<@util.getQueryName entity=entity property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getQueryName entity=entity property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getQueryName entity=entity property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getQueryName entity=entity property=manyToMany/>
</#list>

<@util.getMethodName doc=xml entity=entity property=primaryKey/>
<#list columns as column>
<@util.getMethodName doc=xml entity=entity property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getMethodName doc=xml entity=entity property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getMethodName doc=xml entity=entity property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getMethodName doc=xml entity=entity property=manyToMany/>
</#list>
}
