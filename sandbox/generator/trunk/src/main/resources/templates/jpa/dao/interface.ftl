<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign interfaces = xml["//j:entity[@name=$className]//j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign allProperties = xml["//j:entity[@name=$className]/j:properties/*"]>
<#assign columns = xml["//j:entity[@name=$className]/j:properties/j:column"]>
<#assign manyToOnes = xml["//j:entity[@name=$className]/j:properties/j:many-to-one"]>
<#assign oneToManys = xml["//j:entity[@name=$className]/j:properties/j:one-to-many"]>
<#assign manyToManys = xml["//j:entity[@name=$className]/j:properties/j:many-to-many"]>

<#if daoPackageName?? && daoPackageName?length gt 0>
package ${daoPackageName};
</#if>

<#assign imports = [] />
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericReadDao" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericDao" />
</#if>
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${embeddedIdPackageName}.${embeddedIdName}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(false, daoPackageName, imports)}

<@compress single_line=true>
public interface ${daoName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	GenericReadDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
<#else>
	GenericDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
</#if>
</@compress>
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
