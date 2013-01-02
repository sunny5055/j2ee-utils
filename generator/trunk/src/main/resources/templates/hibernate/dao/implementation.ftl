<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if daoImplPackageName??>
package ${daoImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.stereotype.Repository" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.Search" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SortOrder" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${packageName}.${primaryKey.@targetEntity}" />
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if xml["//h:entity[@name=$className]//*[@unique='true']"]?size gt 0>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>

<@addTo assignTo="imports" element="java.util.Map" />


${getImports(false, daoImplPackageName, imports)}

@Repository
public class ${daoImplName} extends AbstractGenericDaoHibernate<${util.getPrimaryKeyType(entity)}, ${entity.@name}> implements ${daoName} {

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
