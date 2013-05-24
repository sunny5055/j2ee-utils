<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/dao.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="daoImplFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(daoImplPackageName) />

<@changeOutputFile name=filePath + "/"+ daoImplName + ".java" />

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
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />

<#if uniqueConstraints?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if entity[".//*[@unique='true']"]?size gt 0>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
</#if>

<@addTo assignTo="imports" element="java.util.Map" />


${getImports(false, daoImplPackageName, imports)}

@Repository
<#compress>
public class ${daoImplName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
AbstractGenericJpaReadDao<${primaryKeyType}, ${entityName}>
<#else>
AbstractGenericJpaDao<${primaryKeyType}, ${entityName}>
</#if>
 implements ${daoName} {
</#compress>

	<@util.getMethod doc=xml entity=entity property=primaryKey/>
	<#list columns as column>
	<@util.getMethod doc=xml entity=entity property=column/>
	</#list>
	<#list manyToOnes as manyToOne>
	<@util.getMethod doc=xml entity=entity property=manyToOne/>
	</#list>
	<#list oneToManys as oneToMany>
	<@util.getMethod doc=xml entity=entity property=oneToMany/>
	</#list>
	<#list manyToManys as manyToMany>
	<@util.getMethod doc=xml entity=entity property=manyToMany/>
	</#list>

	<@util.getMethodForConstraints doc=xml entity=entity />

	<@util.getSearchMethod doc=xml entity=entity />
}
</#list>