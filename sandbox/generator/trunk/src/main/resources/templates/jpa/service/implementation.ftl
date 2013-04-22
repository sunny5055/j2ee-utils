<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign interfaces = xml["//j:entity[@name=$className]//j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
<#assign columns = xml["//j:entity[@name=$className]/j:properties/j:column"]>
<#assign manyToOnes = xml["//j:entity[@name=$className]/j:properties/j:many-to-one"]>
<#assign oneToManys = xml["//j:entity[@name=$className]/j:properties/j:one-to-many"]>
<#assign manyToManys = xml["//j:entity[@name=$className]/j:properties/j:many-to-many"]>

<#assign entityPackageName = util.getEntityPackageName(packageName) />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign daoPackageName = util.getDaoPackageName(packageName) />
<#assign daoName = util.getDaoName(entity.@name) />
<#assign servicePackageName = util.getServicePackageName(packageName) />
<#assign serviceName = util.getServiceName(entity.@name) />
<#assign serviceImplPackageName = util.getServiceImplPackageName(packageName) />
<#assign serviceImplName = util.getServiceImplName(entity.@name) />


<#if serviceImplPackageName?? && serviceImplPackageName?length gt 0>
package ${serviceImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Service" />

<#if util.xml.getAttribute(entity.@readOnly) == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.AbstractGenericReadService" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.AbstractGenericService" />
</#if>

<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(packageName)}.${primaryKeyType}" />
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>

<#if xml["//j:entity[@name=$className]//*[@unique='true']"]?size gt 0>
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>

${getImports(false, serviceImplPackageName, imports)}


@Service
<#compress>
public class ${serviceImplName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
AbstractGenericReadService<${primaryKeyType}, ${entityName}, ${daoName}>
<#else>
AbstractGenericService<${primaryKeyType}, ${entityName}, ${daoName}>
</#if>
 implements ${serviceName} {
</#compress>

    @Autowired
    @Override
    public void setDao(${daoName} dao) {
        this.dao = dao;
    }

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
}

