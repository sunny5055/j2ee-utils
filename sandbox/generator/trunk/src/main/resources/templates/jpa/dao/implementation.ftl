<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign interfaces = xml["//j:entity[@name=$className]//j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//j:entity[@name=$className]/j:properties/j:column"]>
<#assign manyToOnes = xml["//j:entity[@name=$className]/j:properties/j:many-to-one"]>
<#assign oneToManys = xml["//j:entity[@name=$className]/j:properties/j:one-to-many"]>
<#assign manyToManys = xml["//j:entity[@name=$className]/j:properties/j:many-to-many"]>

<#if daoImplPackageName?? && daoImplPackageName?length gt 0>
package ${daoImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.stereotype.Repository" />
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.AbstractGenericJpaReadDao" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.AbstractGenericJpaDao" />
</#if>
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.Search" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SortOrder" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${embeddedIdPackageName}.${embeddedIdName}" />
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if xml["//j:entity[@name=$className]//*[@unique='true']"]?size gt 0>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>

<@addTo assignTo="imports" element="java.util.Map" />


${getImports(false, daoImplPackageName, imports)}

@Repository
<@compress single_line=true>
public class ${daoImplName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	AbstractGenericJpaReadDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}>
<#else>
	AbstractGenericJpaDao<${util.getPrimaryKeyType(entity)}, ${entity.@name}>
</#if>
 implements ${daoName} {
</@compress>

    public ${daoImplName}() {
        super(${entity.@name}.class);
    }

	<@util.getMethod doc=xml daoName=daoName entity=entity property=primaryKey/>
	<#list columns as column>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=column/>
	</#list>
	<#list manyToOnes as manyToOne>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=manyToOne/>
	</#list>
	<#list oneToManys as oneToMany>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=oneToMany/>
	</#list>
	<#list manyToManys as manyToMany>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=manyToMany/>
	</#list>

	<@util.getSearchMethod doc=xml entity=entity />
}
