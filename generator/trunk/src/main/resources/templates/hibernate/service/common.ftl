<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#macro getMethodName doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["h:properties/h:column"]>
  	<#list columns as column>
  	<@java.interfaceOperation returnType="Integer" methodName="countFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}" />
	<@java.interfaceOperation returnType="List<${entity.@name}>" methodName="findAllFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}" />
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	<@java.interfaceOperation returnType="boolean" methodName="existWith${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}" />
	<@java.interfaceOperation returnType="${entity.@name}" methodName="findBy${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}" />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>	
		<#local argName = "${argName}Id">
	  </#if>
  	<@java.interfaceOperation returnType="Integer" methodName="countFor${property.@name?cap_first}" parameters="${argType} ${argName}" />
	<@java.interfaceOperation returnType="List<${entity.@name}>" methodName="findAllFor${property.@name?cap_first}" parameters="${argType} ${argName}" />
  </#if>
</#macro>