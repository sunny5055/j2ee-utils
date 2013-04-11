<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign interfaces = xml["//j:entity[@name=$className]//j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//j:entity[@name=$className]/j:properties/j:column"]>
<#assign manyToOnes = xml["//j:entity[@name=$className]/j:properties/j:many-to-one"]>
<#assign oneToManys = xml["//j:entity[@name=$className]/j:properties/j:one-to-many"]>
<#assign manyToManys = xml["//j:entity[@name=$className]/j:properties/j:many-to-many"]>

<#if servicePackageName?? && servicePackageName?length gt 0>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.GenericReadService" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.GenericService" />
</#if>
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${embeddedIdPackageName}.${embeddedIdName}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(false, servicePackageName, imports)}


<@compress single_line=true>
public interface ${serviceName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	GenericReadService<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
<#else>
	GenericService<${util.getPrimaryKeyType(entity)}, ${entity.@name}> {
</#if>
</@compress>

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

