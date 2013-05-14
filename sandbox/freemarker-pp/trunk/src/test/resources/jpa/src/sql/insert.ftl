<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/sql.inc" as util>
<@dropOutputFile />
<#assign projectName = xml["//p:projectName"]/>
<@resolveKey map=config key="insertSqlFilePath" value=projectName assignTo="filePath"/>
<@resolveKey map=config key="insertSqlFileName" value=projectName assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<#assign entities = xml["//j:entity"]>

<#list entities as entity>
	<@util.insertSql doc=xml entity=entity/>

</#list>

<#list entities as entity>
	<@xPath xml=xml expression="//j:entity[@name='${entity.@name}']/j:properties/j:one-to-many" assignTo="oneToManys" />
	<@xPath xml=xml expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-many" assignTo="manyToManys" />
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