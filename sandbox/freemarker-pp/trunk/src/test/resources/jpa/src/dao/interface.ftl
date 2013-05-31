<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/dao.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="daoFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(daoPackageName) />

<@changeOutputFile name=filePath + "/"+ daoName + ".java" />

<#if daoPackageName?? && daoPackageName?length gt 0>
package ${daoPackageName};
</#if>

<#assign imports = [] />
<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericReadDao" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.GenericDao" />
</#if>
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<#if uniqueConstraints?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(false, daoPackageName, imports)}

<#compress>
public interface ${daoName} extends
<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
GenericReadDao<${primaryKeyType}, ${entityName}> {
<#else>
GenericDao<${primaryKeyType}, ${entityName}> {
</#if>
</#compress>

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
<@util.getInterfaceMethodForConstraints doc=xml entity=entity />
}
</#list>