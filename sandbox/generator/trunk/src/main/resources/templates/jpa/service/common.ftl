<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "../common.ftl">


<#macro getInterfaceMethod doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["j:properties/j:column"]>
  	<#list columns as column>
  	<@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(column.@name, false) parameters="${getType(column.@type)} ${column.@name}" />
	<@java.interfaceOperation returnType="List<${entity.@name}>" methodName=getFindQueryName(column.@name, false) parameters="${getType(column.@type)} ${column.@name}" />
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	<@java.interfaceOperation returnType="boolean" methodName="existWith${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}" />
	<@java.interfaceOperation returnType="${entity.@name}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}" />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>
		<#local argName = "${argName}Id">
	  </#if>
  	<@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(argName, false) parameters="${argType} ${argName}" />
	<@java.interfaceOperation returnType="List<${entity.@name}>" methodName=getFindQueryName(argName, false) parameters="${argType} ${argName}" />
  </#if>
</#macro>


<#macro getMethod doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["j:properties/j:column"]>
  	<#list columns as column>
  	@Override
  	<@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(column.@name, false) parameters="${getType(column.@type)} ${column.@name}">
  		Integer count = 0;
  		if(${java.checkNotNull(getType(column.@type), column.@name)}) {
  			count = dao.${getCountQueryName(column.@name, false)}(${column.@name});
  		}
  		return count;
  	</@java.operation>

  	@Override
	<@java.operation visibility="public" returnType="List<${entity.@name}>" methodName=getFindQueryName(column.@name, false) parameters="${getType(column.@type)} ${column.@name}">
		List<${entity.@name}> list = null;
  		if(${java.checkNotNull(getType(column.@type), column.@name)}) {
  			list = dao.${getFindQueryName(column.@name, false)}(${column.@name});
  		}
  		return list;
	</@java.operation>
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@Override
  	<@java.operation visibility="public" returnType="boolean" methodName="existWith${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}">
  		boolean exist = false;
  		if(${java.checkNotNull(getType(property.@type), property.@name)}) {
  			final Integer count = dao.${getCountQueryName(property.@name, true)}(${property.@name});
  			exist = count != 0;
  		}
  		return exist;
  	</@java.operation>

  	@Override
	<@java.operation visibility="public" returnType="${entity.@name}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}">
		${entity.@name} ${entity.@name?uncap_first} = null;
        if(${java.checkNotNull(getType(property.@type), property.@name)}) {
            ${entity.@name?uncap_first} = dao.${getFindQueryName(property.@name, true)}(${property.@name});
        }
        return ${entity.@name?uncap_first};
	</@java.operation>
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>
		<#local argName = "${argName}Id">
	  </#if>

	  @Override
	<@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(argName, false) parameters="${argType} ${argName}">
  		Integer count = 0;
  		if(${java.checkNotNull(argType, argName)}) {
  			count = dao.${getCountQueryName(argName, false)}(${argName});
  		}
  		return count;
  	</@java.operation>

  	@Override
	<@java.operation visibility="public" returnType="List<${entity.@name}>" methodName=getFindQueryName(argName, false) parameters="${argType} ${argName}">
		List<${entity.@name}> list = null;
  		if(${java.checkNotNull(argType, argName)}) {
  			list = dao.${getFindQueryName(argName, false)}(${argName});
  		}
  		return list;
	</@java.operation>
  </#if>
</#macro>
