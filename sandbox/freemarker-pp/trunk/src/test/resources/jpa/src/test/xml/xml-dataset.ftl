<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/test-xml.inc" as util>
<@dropOutputFile />
<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="testXmlDatasetFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="testXmlDatasetFileName" values=[modelName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
<#if manyToOnes?size gt 0>
<#list manyToOnes as manyToOne>
	<@util.getTargetEntity doc=xml entity=entity property=manyToOne />
</#list>
</#if>
<#if oneToManys?size gt 0>
<#list oneToManys as oneToMany>
	<@util.getTargetEntity doc=xml entity=entity property=oneToMany />
</#list>
</#if>
<#if manyToManys?size gt 0>
<#list manyToManys as manyToMany>
	<@util.getTargetEntity doc=xml entity=entity property=manyToMany />
</#list>
</#if>

<#list 1..5 as i>
<#compress>
	<${entity.@tableName}
		<@util.getAttribute doc=xml entity=entity property=primaryKey />
		<#list columns as column>
			<@util.getAttribute doc=xml entity=entity property=column />
		</#list>
		<#list manyToOnes as manyToOne>
			<@util.getAttribute doc=xml entity=entity property=manyToOne />
		</#list>
	/>
</#compress>

</#list>


<#if oneToManys?size gt 0>
<#list oneToManys as oneToMany>
	<@util.getJoinTable doc=xml entity=entity property=oneToMany />
</#list>
</#if>
<#if manyToManys?size gt 0>
<#list manyToManys as manyToMany>
	<@util.getJoinTable doc=xml entity=entity property=manyToMany />
</#list>
</#if>
</dataset>
</#list>
