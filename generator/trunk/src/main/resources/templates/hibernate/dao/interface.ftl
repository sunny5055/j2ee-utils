<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign allProperties = xml["//h:entity[@name=$className]/h:properties/*"]>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if daoPackageName?? && daoPackageName?length gt 0>
package ${daoPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericDao" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${embeddedIdPackageName}.${embeddedIdName}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(false, daoPackageName, imports)}

public interface ${daoName} extends GenericDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
<@util.getInterfaceQueryName doc=xml entity=entity property=primaryKey/>
<#list columns as column>
<@util.getInterfaceQueryName doc=xml entity=entity property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getInterfaceQueryName doc=xml entity=entity property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getInterfaceQueryName doc=xml entity=entity property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getInterfaceQueryName doc=xml entity=entity property=manyToMany/>
</#list>

<@util.getInterfaceMethod doc=xml entity=entity property=primaryKey/>
<#list columns as column>
<@util.getInterfaceMethod doc=xml entity=entity property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getInterfaceMethod doc=xml entity=entity property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getInterfaceMethod doc=xml entity=entity property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getInterfaceMethod doc=xml entity=entity property=manyToMany/>
</#list>
}
