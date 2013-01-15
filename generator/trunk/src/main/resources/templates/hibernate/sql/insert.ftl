<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entities = xml["//h:entity"]>

<#list entities as entity>
	<@util.insertSql doc=xml entity=entity/>

</#list>

<#list entities as entity>
	<@xPath xml=xml expression="//h:entity[@name='${entity.@name}']/h:properties/h:one-to-many" assignTo="oneToManys" />
	<@xPath xml=xml expression="//h:entity[@name='${entity.@name}']/h:properties/h:many-to-many" assignTo="manyToManys" />
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