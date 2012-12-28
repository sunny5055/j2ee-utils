<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "../common/xml.ftl" as xml>
<#import "../common/java.ftl" as java>


<#function getImportsFor node>
  <#assign imports = [] />
  <#if node?node_name = "property" || node?node_name = "type" || node?node_name = "parameter">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "property-list" || node?node_name = "type-list" || node?node_name = "parameter-list">
      <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
  <#elseif node?node_name = "property-map" || node?node_name = "type-map" || node?node_name = "parameter-map">
      <@addTo assignTo="imports" element=node.@type />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@value) />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@key) />
  <#elseif node?node_name = "constructor">
    <#assign parameters = node["b:parameters/*"]>
    <#list parameters as parameter>
      <@addTo assignTo="imports" element=getImportsFor(parameter) />
    </#list>
  <#elseif node?node_name = "operation">
    <#if node["b:return/b:type"]?is_node>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type"]) />
    <#elseif node["b:return/b:type-list"]?is_node>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type-list"]) />
    <#else>
      <@addTo assignTo="imports" element=getImportsFor(node["b:return/b:type-map"]) />
    </#if>

    <#assign parameters = node["b:parameters/*"]>
    <#list parameters as parameter>
      <@addTo assignTo="imports" element=getImportsFor(parameter) />
    </#list>
  <#elseif node?node_name = "column">
      <@addTo assignTo="imports" element=node.@type />
  <#elseif node?node_name = "many-to-one">
      <@addTo assignTo="imports" element=node.@targetEntity />
  <#elseif node?node_name = "one-to-many" || node?node_name = "many-to-many">
      <@addTo assignTo="imports" element=xml.getAttribute(node.@listType) />
      <@addTo assignTo="imports" element=xml.getAttribute(node.@targetEntity) />
  </#if>
  <#return imports>
</#function>


<#function getPrimaryKey class>
  <#if class["h:id"]?is_node>
    <#local primaryKey = class["h:id"]>
  <#else>
    <#local primaryKey = class["h:embedded-id"]>
  </#if>
  <#return primaryKey>
</#function>


<#function getPrimaryKeyType class>
  <#local primaryKeyType= "">
  <#if class["h:id"]?is_node>
    <#local primaryKeyType = getType(class["h:id/@type"])>
  <#else>
    <#local primaryKeyType = getType(class["h:embedded-id/@targetEntity"])>
  </#if>
  <#return primaryKeyType>
</#function>


<#function getEntity xml name>
	<#local entity = "">
	<@xPath xml=xml expression="//h:entity[@name='${name}']" assignTo=entity />
  	<#return entity>
</#function>


<#function getParametersDeclaration parameters>
  <@myList list=parameters var="parameter" assignTo="parametersDeclaration">
  ${getType(parameter.@type, xml.getAttribute(parameter.@value))} ${parameter.@name}
  </@myList>
  <#return parametersDeclaration>
</#function>


<#function getReturnType operation>
  <#local returnType= "">
  <#if operation["h:return/h:type"]?is_node>
    <#local returnType = getType(operation["h:return/h:type/@type"])>
  <#else>
    <#local typeList = operation["h:return/h:type-list"]>
    <#local returnType = getType(typeList.@type, xml.getAttribute(typeList.@value))>
  </#if>
  <#return returnType>
</#function>

<#function getModifiersFrom node>
  <#return getModifiers(xml.getAttribute(node.@abstract), xml.getAttribute(node.@static), xml.getAttribute(node.@final))>
</#function>


<#function getColumnName columnPrefix property>
  <#local columnName= "">
  <#if xml.existAttribute(property.@column)>
    <#local columnName= "${property.@column}">
  <#else>
    <#if property?node_name = "id">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
    <#elseif property?node_name = "column">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
    <#elseif property?node_name = "many-to-one">
      <#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name+'_id')}">
    </#if>
  </#if>
  <#return columnName>
</#function>

<#function getJoinColumnName class property>
  <#local joinColumnName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#local prefix = "${class.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local joinColumnName= "${toUnderscoreCase(prefix+'_'+class.@name?lower_case+'_id')}">
  </#if>
  <#return joinColumnName>
</#function>

<#function getInverseJoinColumnName class property>
  <#local inverseJoinColumnName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#local prefix = "${class.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
    <#local inverseJoinColumnName= "${toUnderscoreCase(prefix+'_'+getClassName(property.@targetEntity)?lower_case+'_id')}">
  </#if>
  <#return inverseJoinColumnName>
</#function>

<#function getJoinTableName class property>
  <#local joinTableName= "">
  <#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
    <#if xml.existAttribute(property.@joinTable)>
      <#local joinTableName= "${property.@joinTable}">
    <#else>
      <#local prefix = "${class.@name?substring(0,2)?lower_case+getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
      <#local joinTableName= "${toUnderscoreCase(prefix+'_'+class.@name?lower_case+'_'+getClassName(property.@targetEntity)?lower_case)}">
    </#if>
  </#if>
  <#return joinTableName>
</#function>
