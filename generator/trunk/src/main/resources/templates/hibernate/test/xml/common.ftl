<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../../common.ftl">


<#macro getAttribute doc entity property>
<#if property?node_name = "id">
	${getColumnName(entity.@columnPrefix, property)}="${random(getType(property.@type), xml.getAttribute(property.@length))}"
<#elseif property?node_name = "embedded-id">
	<#assign pkColumns = property["h:properties/h:column"]>
	<#list pkColumns as pkColumn>
		${getColumnName(entity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), xml.getAttribute(pkColumn.@length))}"
	</#list>
<#elseif property?node_name = "column">
	${getColumnName(entity.@columnPrefix, property)}="${random(getType(property.@type), xml.getAttribute(property.@length))}"
<#elseif property?node_name = "many-to-one">
	${getColumnName(entity.@columnPrefix, property)}=""
</#if>
</#macro>


<#macro getTargetEntity doc entity property>
<#if property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	<@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	<#assign targetPk = getPrimaryKey(targetEntity)>
	<@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']/h:properties/h:column" assignTo="targetColumns" />
	
	<#list 1..3 as i>
		<@compress single_line=true>
		<${targetEntity.@tableName}
			<#if targetPk?node_name = "id">
				${getColumnName(targetEntity.@columnPrefix, targetPk)}="${random(getPrimaryKeyType(targetEntity))}"
			<#else>
				<#assign targetPkColumns = targetPk["h:properties/h:column"]>
    			<#list targetPkColumns as pkColumn>
    				${getColumnName(targetEntity.@columnPrefix, pkColumn)}="${random(getType(pkColumn.@type), xml.getAttribute(pkColumn.@length))}"
    			</#list>
			</#if>
			<#list targetColumns as column>
				${getColumnName(targetEntity.@columnPrefix, column)}="${random(getType(column.@type), xml.getAttribute(column.@length))}"
			</#list>
		/>
	</@compress>
	
	</#list>
</#if>
</#macro>


<#macro getJoinTable doc entity property>
<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	<@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	<#assign targetPk = getPrimaryKey(targetEntity)>
	<@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']/h:properties/h:column" assignTo="targetColumns" />
	
	<#list 1..5 as i>
		<@compress single_line=true>
		<${getJoinTableName(entity, property)}
			${getJoinColumnName(entity, property)}=""
			${getInverseJoinColumnName(entity, property)}=""
		/>
	</@compress>
	
	</#list>
</#if>
</#macro>

