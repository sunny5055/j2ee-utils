<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />


<#macro insertSql doc entity>
<#assign primaryKey = getPrimaryKey(entity)>
<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column" assignTo="columns" />
<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />
-------------------------------------------------------
-- Table ${entity.@tableName}
<#compress>
INSERT INTO ${entity.@tableName} (${getColumnsDeclaration(doc, entity)}) VALUES
</#compress>

<#list 1..5 as line>
<#compress>
(${getColumnsValue(doc, entity)})<#if line_has_next>,<#else>;</#if>
</#compress>

</#list>
-------------------------------------------------------
</#macro>


<#macro insertJoinTableSql doc entity property>
-------------------------------------------------------
-- Table ${getJoinTableName(entity, property)}
<#compress>
INSERT INTO ${getJoinTableName(entity, property)} (${getJoinColumnName(entity, property)}, ${getInverseJoinColumnName(entity, property)}) VALUES
</#compress>

<@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
<#local entityId = getPrimaryKey(entity)>
<#local targetEntityId = getPrimaryKey(targetEntity)>
<#list 1..5 as line>
<#compress>
((SELECT ${getColumnName(entity.@columnPrefix, entityId)} FROM ${entity.@tableName} WHERE = 'TO_COMPLETE'), (SELECT ${getColumnName(targetEntity.@columnPrefix, targetEntityId)} FROM ${targetEntity.@tableName} WHERE = 'TO_COMPLETE'))<#if line_has_next>,<#else>;</#if>
</#compress>

</#list>
-------------------------------------------------------
</#macro>


<#function getColumnsDeclaration doc entity>
	<#local result = ""/>
	<#assign primaryKey = getPrimaryKey(entity)>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column" assignTo="columns" />
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />

	<#if config.database != "mysql">
		<#local result = getPrimaryKeyColumn(entity.@columnPrefix, primaryKey) />
	</#if>
	<#if columns?? && columns?size != 0>
		<@myList list=columns var="column" assignTo="columnsDeclaration">${getColumnName(entity.@columnPrefix, column)}</@myList>
		<#if result?length != 0 && columnsDeclaration?? && columnsDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + columnsDeclaration/>
	</#if>
	<#if manyToOnes?? && manyToOnes?size != 0>
		<@myList list=manyToOnes var="manyToOne" assignTo="manyToOnesDeclaration">${getColumnName(entity.@columnPrefix, manyToOne)}</@myList>
		<#if result?length != 0 && manyToOnesDeclaration?? && manyToOnesDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + manyToOnesDeclaration />
	</#if>

	<#return result />
</#function>


<#function getColumnsValue doc entity>
	<#local result = ""/>
	<#assign primaryKey = getPrimaryKey(entity)>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column" assignTo="columns" />
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />

	<#if config.database != "mysql">
		<#local result = getNextSequenceValue() />
	</#if>
	<#if columns?? && columns?size != 0>
		<@myList list=columns var="column" assignTo="columnsDeclaration">${getValue(column.@type)}</@myList>
		<#if result?length != 0 && columnsDeclaration?? && columnsDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + columnsDeclaration/>
	</#if>
	<#if manyToOnes?? && manyToOnes?size != 0>
		<@myList list=manyToOnes var="manyToOne" assignTo="manyToOnesDeclaration">
		<@xPath xml=doc expression="//j:entity[@name='${getType(manyToOne.@targetEntity)}']" assignTo="targetEntity" />
		<#local targetEntityId = getPrimaryKey(targetEntity)>
		(SELECT ${getColumnName(targetEntity.@columnPrefix, targetEntityId)} FROM ${targetEntity.@tableName} WHERE = 'TO_COMPLETE')
		</@myList>
		<#if result?length != 0 && manyToOnesDeclaration?? && manyToOnesDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + manyToOnesDeclaration />
	</#if>

	<#return result />
</#function>

<#function getPrimaryKeyColumn columnPrefix property>
  <#return getColumnName(columnPrefix, property) />
</#function>

<#function getNextSequenceValue sequenceName="hibernate_sequence">
  <#local nextSequenceValue = ""/>
  <#if config.database == "postgresql">
    <#local nextSequenceValue = "nextval('${sequenceName}')"/>
  <#elseif config.database == "oracle">
    <#local nextSequenceValue = "${sequenceName}.nextval"/>
  </#if>
  <#return nextSequenceValue />
</#function>


<#function getValue type>
	<#local value= "">
	<#local typeClassName= getClassName(type)>

	<#if typeClassName == "String">
  		<#local value = "'TO_COMPLETE'">
	<#elseif typeClassName == "Double"  || type == "double"
		|| typeClassName == "Float" || type == "float">
  		<#local value = "0.0">
    <#elseif typeClassName == "Integer" || type == "int"
		|| typeClassName == "Long" || type == "long"
		|| typeClassName == "Byte" || type == "byte"
		|| typeClassName == "Short" || type == "short">
  		<#local value = "0">
    <#elseif typeClassName == "Date">
  		<#local value = "'TO_COMPLETE'">
	<#elseif typeClassName == "Character" || type == "char">
		<#local value = "'TO_COMPLETE'">
  	<#elseif typeClassName == "Boolean" || type == "boolean">
  		<#local value = "false">
	<#else>
  		<#local value = "'TO_COMPLETE'">
	</#if>
	<#return value />
</#function>
