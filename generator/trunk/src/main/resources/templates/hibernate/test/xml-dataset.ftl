<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>
<#assign max = 5>
<#assign maxTarget = 3>
<?xml version="1.0" encoding="UTF-8"?>
<dataset>

<#if manyToOnes?size gt 0>
<#list manyToOnes as manyToOne>
<@xPath xml=xml expression="//h:entity[@name='${getType(manyToOne.@targetEntity)}']" assignTo="targetEntity" />
<#assign targetPk = util.getPrimaryKey(targetEntity)>
<@xPath xml=xml expression="//h:entity[@name='${getType(manyToOne.@targetEntity)}']/h:properties/h:column" assignTo="targetColumns" />
<#list 1..maxTarget as i>
<@compress single_line=true>
	<${targetEntity.@tableName}
		<#if targetPk?node_name = "id">
			${util.getColumnName(targetEntity.@columnPrefix, targetPk)}="${random(util.getPrimaryKeyType(targetEntity))}"
		<#else>
			<#assign targetPkColumns = targetPk["h:properties/h:column"]>
    		<#list targetPkColumns as pkColumn>
    		${util.getColumnName(targetEntity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))}"
    		</#list>
		</#if>
		<#list targetColumns as column>
			${util.getColumnName(targetEntity.@columnPrefix, column)}="${random(getType(column.@type), util.xml.getAttribute(column.@length))}"
		</#list>
 />
</@compress>

</#list>

</#list>
</#if>
<#if oneToManys?size gt 0>
<#list oneToManys as oneToMany>
<@xPath xml=xml expression="//h:entity[@name='${getType(oneToMany.@targetEntity)}']" assignTo="targetEntity" />
<#assign targetPk = util.getPrimaryKey(targetEntity)>
<@xPath xml=xml expression="//h:entity[@name='${getType(oneToMany.@targetEntity)}']/h:properties/h:column" assignTo="targetColumns" />
<#list 1..maxTarget as i>
<@compress single_line=true>
	<${targetEntity.@tableName}
		<#if targetPk?node_name = "id">
			${util.getColumnName(targetEntity.@columnPrefix, targetPk)}="${random(util.getPrimaryKeyType(targetEntity))}"
		<#else>
			<#assign targetPkColumns = targetPk["h:properties/h:column"]>
    		<#list targetPkColumns as pkColumn>
    		${util.getColumnName(targetEntity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))}"
    		</#list>
		</#if>
		<#list targetColumns as column>
			${util.getColumnName(targetEntity.@columnPrefix, column)}="${random(getType(column.@type), util.xml.getAttribute(column.@length))}"
		</#list>
 />
</@compress>

</#list>
<#list 1..maxTarget as i>
<@compress single_line=true>
   <${util.getJoinTableName(entity, oneToMany)} ${util.getJoinColumnName(entity, oneToMany)}="" ${util.getInverseJoinColumnName(entity, oneToMany)}="" />
</@compress>

</#list>
</#list>
</#if>
<#if manyToManys?size gt 0>
<#list manyToManys as manyToMany>
<@xPath xml=xml expression="//h:entity[@name='${getType(manyToMany.@targetEntity)}']" assignTo="targetEntity" />
<#assign targetPk = util.getPrimaryKey(targetEntity)>
<@xPath xml=xml expression="//h:entity[@name='${getType(manyToMany.@targetEntity)}']/h:properties/h:column" assignTo="targetColumns" />
<#list 1..maxTarget as i>
<@compress single_line=true>
	<${targetEntity.@tableName}
		<#if targetPk?node_name = "id">
			${util.getColumnName(targetEntity.@columnPrefix, targetPk)}="${random(util.getPrimaryKeyType(targetEntity))}"
		<#else>
			<#assign targetPkColumns = targetPk["h:properties/h:column"]>
    		<#list targetPkColumns as pkColumn>
    		${util.getColumnName(targetEntity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))}"
    		</#list>
		</#if>
		<#list targetColumns as column>
			${util.getColumnName(targetEntity.@columnPrefix, column)}="${random(getType(column.@type), util.xml.getAttribute(column.@length))}"
		</#list>
 />
</@compress>

</#list>
<#list 1..maxTarget as i>
<@compress single_line=true>
   <${util.getJoinTableName(entity, manyToMany)} ${util.getJoinColumnName(entity, manyToMany)}="" ${util.getInverseJoinColumnName(entity, manyToMany)}="" />
</@compress>

</#list>
</#list>
</#if>

<#list 1..max as i>
<@compress single_line=true>
	<${entity.@tableName}
		<#if primaryKey?node_name = "id">
			${util.getColumnName(entity.@columnPrefix, primaryKey)}="${random(util.getPrimaryKeyType(entity))}"
		<#else>
			<#assign pkColumns = primaryKey["h:properties/h:column"]>
    		<#list pkColumns as pkColumn>
    		${util.getColumnName(entity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))}"
    		</#list>
		</#if>
		<#list columns as column>
			${util.getColumnName(entity.@columnPrefix, column)}="${random(getType(column.@type), util.xml.getAttribute(column.@length))}"
		</#list>
		<#list manyToOnes as manyToOne>
			${util.getColumnName(entity.@columnPrefix, manyToOne)}=""
		</#list>
 />
</@compress>

</#list>
</dataset>