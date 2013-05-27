<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/sql.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]/>
<@resolveKey map=config key="insertSqlFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="insertSqlFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign entities = xml["//j:entity"]>

<#list entities as entity>
	<@util.insertSql doc=xml entity=entity/>

</#list>

<#list entities as entity>
	<#include "/common/assign.inc" />
	<#if oneToManys?? && oneToManys?size != 0>
	<#list oneToManys as oneToMany>
		<@util.insertJoinTableSql doc=xml entity=entity property=oneToMany />
	</#list>

	</#if>
	<#if manyToManys?? && manyToManys?size != 0>
	<#list manyToManys as manyToMany>
		<@util.insertJoinTableSql doc=xml entity=entity property=manyToMany />
	</#list>
	</#if>
</#list>