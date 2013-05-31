<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/service.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="serviceImplFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(serviceImplPackageName) />

<@changeOutputFile name=filePath + "/"+ serviceImplName + ".java" />

<#if serviceImplPackageName?? && serviceImplPackageName?length gt 0>
package ${serviceImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Service" />
<@addTo assignTo="imports" element="org.dozer.Mapper" />

<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.AbstractGenericReadService" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.AbstractGenericService" />
</#if>
<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<#if uniqueConstraints?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>

<#if uniqueColumns?size gt 0>
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
<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
AbstractGenericReadService<${primaryKeyType}, ${modelName}, ${entityName}, ${daoName}>
<#else>
AbstractGenericService<${primaryKeyType}, ${modelName}, ${entityName}, ${daoName}>
</#if>
 implements ${serviceName} {
</#compress>

    @Autowired
    @Override
    public void setDao(${daoName} dao) {
        this.dao = dao;
    }

	@Autowired
	@Override
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
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
	<@util.getMethodForConstraints doc=xml entity=entity />
}
</#list>
