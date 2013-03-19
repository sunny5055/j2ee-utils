<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>
<?xml version="1.0" encoding="UTF-8"?>
<dataset>
<#list 1..5 as i>
<@compress single_line=true>
	<${entity.@tableName}
		<@util.getAttribute doc=xml entity=entity property=primaryKey />
		<#list columns as column>
			<@util.getAttribute doc=xml entity=entity property=column />
		</#list>
		<#list manyToOnes as manyToOne>
			<@util.getAttribute doc=xml entity=entity property=manyToOne />
		</#list>
	/>
</@compress>

</#list>

<#if manyToOnes?size gt 0>
<#list manyToOnes as manyToOne>
	<@util.getTargetEntity doc=xml entity=entity property=manyToOne />
</#list>
</#if>
<#if oneToManys?size gt 0>

<#list oneToManys as oneToMany>
	<@util.getTargetEntity doc=xml entity=entity property=oneToMany />
	
	<@util.getJoinTable doc=xml entity=entity property=oneToMany />
</#list>
</#if>
<#if manyToManys?size gt 0>

<#list manyToManys as manyToMany>
	<@util.getTargetEntity doc=xml entity=entity property=manyToMany />

	<@util.getJoinTable doc=xml entity=entity property=manyToMany />
</#list>
</#if>
</dataset>

