<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/service.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="serviceFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(servicePackageName) />

<@changeOutputFile name=filePath + "/"+ serviceName + ".java" />

<#if servicePackageName?? && servicePackageName?length gt 0>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.GenericReadService" />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.service.GenericService" />
</#if>
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(entityPackageName)}.${primaryKeyType}" />
	<@addTo assignTo="imports" element="java.util.List" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

${getImports(false, servicePackageName, imports)}


<#compress>
public interface ${serviceName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
	GenericReadService<${primaryKeyType}, ${entityName}> {
<#else>
	GenericService<${primaryKeyType}, ${entityName}> {
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
}
</#list>
