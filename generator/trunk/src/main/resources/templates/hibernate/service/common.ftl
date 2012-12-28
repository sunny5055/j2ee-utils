<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#macro getMethodName entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["h:properties/h:column"]>
  	<#list columns as column>
	Integer countFor${column.@name?cap_first}(${getType(column.@type)} ${column.@name});
    List<${entity.@name}> findAllFor${column.@name?cap_first}(${getType(column.@type)} ${column.@name});
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
    boolean existWith${property.@name?cap_first}(${getType(property.@type)} ${property.@name});
    ${entity.@name} findBy${property.@name?cap_first}(${getType(property.@type)} ${property.@name});
  <#elseif property?node_name = "many-to-one">
  	Integer countFor${property.@name?cap_first}(${getType(property.@targetEntity)} ${property.@name});
    List<${entity.@name}> findAllFor${property.@name?cap_first}(${getType(property.@targetEntity)} ${property.@name});
  <#elseif property?node_name = "one-to-many">
	<#local propertyName = property.@name?substring(0,property.@name?length-1) />
  	Integer countFor${propertyName?cap_first}(${getType(property.@targetEntity)} ${propertyName});
    List<${entity.@name}> findAllFor${propertyName?cap_first}(${getType(property.@targetEntity)} ${propertyName});
  <#elseif property?node_name = "many-to-many">
	<#local propertyName = property.@name?substring(0,property.@name?length-1) />
  	Integer countFor${propertyName?cap_first}(${getType(property.@targetEntity)} ${propertyName});
    List<${entity.@name}> findAllFor${propertyName?cap_first}(${getType(property.@targetEntity)} ${propertyName});
  </#if>
</#macro>
