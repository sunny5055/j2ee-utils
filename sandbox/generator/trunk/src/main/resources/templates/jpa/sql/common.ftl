<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "../common.ftl">


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

<#list 1..5 as line>
<#compress>
(${getValue("Integer")}, ${getValue("Integer")})<#if line_has_next>,<#else>;</#if>
</#compress>

</#list>
-------------------------------------------------------
</#macro>


<#function getColumnsDeclaration doc entity>
	<#local result = ""/>
	<#assign primaryKey = getPrimaryKey(entity)>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column" assignTo="columns" />
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />

	<#if primaryKey?node_name == "embedded-id" || database != "mysql">
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

	<#if primaryKey?node_name != "embedded-id">
		<#if database != "mysql">
			<#local result = getNextSequenceValue() />
		</#if>
	<#else>
		<#assign pkColumns = primaryKey["j:properties/j:column"]>
        <@myList list=pkColumns var="column" assignTo="pkColumnsDeclaration">${getValue(column.@type)}</@myList>
		<#local result = pkColumnsDeclaration />
	</#if>
	<#if columns?? && columns?size != 0>
		<@myList list=columns var="column" assignTo="columnsDeclaration">${getValue(column.@type)}</@myList>
		<#if result?length != 0 && columnsDeclaration?? && columnsDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + columnsDeclaration/>
	</#if>
	<#if manyToOnes?? && manyToOnes?size != 0>
		<@myList list=manyToOnes var="manyToOne" assignTo="manyToOnesDeclaration">${getValue("Integer")}</@myList>
		<#if result?length != 0 && manyToOnesDeclaration?? && manyToOnesDeclaration?length != 0>
			<#local result = result + ", "/>
		</#if>
		<#local result = result + manyToOnesDeclaration />
	</#if>

	<#return result />
</#function>

<#function getPrimaryKeyColumn columnPrefix property>
  <#local primaryKeyColum = ""/>
  <#if property?node_name == "id">
    <#local primaryKeyColum = getColumnName(columnPrefix, property) />
  <#elseif property?node_name == "embedded-id">
    <#assign pkColumns = property["j:properties/j:column"]>
        <#list pkColumns as column>
          <#if column_index != 0>
            <#local primaryKeyColum= primaryKeyColum + ", " />
          </#if>

          <#local primaryKeyColum = primaryKeyColum + getColumnName(columnPrefix, column) />
      </#list>
  </#if>
  <#return primaryKeyColum />
</#function>

<#function getNextSequenceValue sequenceName="hibernate_sequence">
  <#local nextSequenceValue = ""/>
  <#if database == "postgresql">
    <#local nextSequenceValue = "nextval('${sequenceName}')"/>
  <#elseif database == "oracle">
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
