<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign interfaces = xml["//j:entity[@name=$className]//j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//j:entity[@name=$className]/j:properties/j:column"]>
<#assign manyToOnes = xml["//j:entity[@name=$className]/j:properties/j:many-to-one"]>
<#assign oneToManys = xml["//j:entity[@name=$className]/j:properties/j:one-to-many"]>
<#assign manyToManys = xml["//j:entity[@name=$className]/j:properties/j:many-to-many"]>
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

