<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#macro getInterfaceQueryName entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["h:properties/h:column"]>
  	<#list columns as column>
  	<@java.getInterfaceProperty type="String" name="countFor${column.@name?cap_first}" value="${entity.@name?uncap_first}.countBy${column.@name?cap_first}" />
    <@java.getInterfaceProperty type="String" name="findAllFor${column.@name?cap_first}" value="${entity.@name?uncap_first}.findBy${column.@name?cap_first}" />
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
    <@java.getInterfaceProperty type="String" name="countBy${property.@name?cap_first}" value="${entity.@name?uncap_first}.countBy${property.@name?cap_first}" />
    <@java.getInterfaceProperty type="String" name="findBy${property.@name?cap_first}" value="${entity.@name?uncap_first}.findBy${property.@name?cap_first}" />
  <#elseif property?node_name = "many-to-one">
	<@java.getInterfaceProperty type="String" name="countFor${property.@name?cap_first}" value="${entity.@name?uncap_first}.countFor${property.@name?cap_first}" />
	<@java.getInterfaceProperty type="String" name="findAllFor${property.@name?cap_first}" value="${entity.@name?uncap_first}.findAllFor${property.@name?cap_first}" />
  <#elseif property?node_name = "one-to-many">
	<#local propertyName = property.@name?substring(0,property.@name?length-1) />
	<@java.getInterfaceProperty type="String" name="countFor${propertyName?cap_first}" value="${entity.@name?uncap_first}.countFor${propertyName?cap_first}" />
	<@java.getInterfaceProperty type="String" name="findAllFor${propertyName?cap_first}" value="${entity.@name?uncap_first}.findAllFor${propertyName?cap_first}" />
  <#elseif property?node_name = "many-to-many">
	<#local propertyName = property.@name?substring(0,property.@name?length-1) />
	<@java.getInterfaceProperty type="String" name="countFor${propertyName?cap_first}" value="${entity.@name?uncap_first}.countFor${propertyName?cap_first}" />
	<@java.getInterfaceProperty type="String" name="findAllFor${propertyName?cap_first}" value="${entity.@name?uncap_first}.findAllFor${propertyName?cap_first}" />
  </#if>
</#macro>


<#macro getInterfaceMethod doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["h:properties/h:column"]>
  	<#list columns as column>
  	<@java.interfaceOperation returnType="Integer" methodName="countFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}" />
	<@java.interfaceOperation returnType="List<${entity.@name}>" methodName="findAllFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}" />
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	<@java.interfaceOperation returnType="Integer" methodName="countBy${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}" />
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


<#macro getMethod doc daoName entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["h:properties/h:column"]>
  	<#list columns as column>
  	@Override
  	<@java.operation visibility="public" returnType="Integer" methodName="countFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}">
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "countFor" + column.@name?cap_first)}, 
                new String[] { "${column.@name}" }, ${column.@name});
  	</@java.operation>
  	
  	@Override
	<@java.operation visibility="public" returnType="List<${entity.@name}>" methodName="findAllFor${column.@name?cap_first}" parameters="${getType(column.@type)} ${column.@name}">
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "findAllFor" + column.@name?cap_first)},
                new String[] { "${column.@name}" }, ${column.@name});
	</@java.operation>
	
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@Override
  	<@java.operation visibility="public" returnType="Integer" methodName="countBy${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}">
  		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "countBy" + property.@name?cap_first)},
                new String[] { "${property.@name}" }, ${property.@name});
  	</@java.operation>
	
	@Override	
	<@java.operation visibility="public" returnType="${entity.@name}" methodName="findBy${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}">
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "findBy" + property.@name?cap_first)},
                new String[] { "${property.@name}" }, ${property.@name});
	</@java.operation>
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
	  
	@Override
  	<@java.operation visibility="public" returnType="Integer" methodName="countFor${property.@name?cap_first}" parameters="${argType} ${argName}">
  		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "countFor" + argName?cap_first)},
                new String[] { "${argName}" }, ${argName});
  	</@java.operation>
	
	@Override
	<@java.operation visibility="public" returnType="List<${entity.@name}>" methodName="findAllFor${property.@name?cap_first}" parameters="${argType} ${argName}">
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), ${java.getConstant(daoName, "findAllFor" + argName?cap_first)},
                new String[] { "${argName}" }, ${argName});
	</@java.operation>
  </#if>
</#macro>

