<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />


<#macro getProperty property>
  <#local visibility= xml.getAttribute(property.@visibility, "private")>
  <#if property?node_name = "id">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getProperty visibility=visibility type=getEntityName(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  </#if>
</#macro>


<#macro initProperties property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <@java.initProperties type=property.@listType name=property.@name value=getEntityName(xml.getAttribute(property.@targetEntity)) />
  </#if>
</#macro>


<#macro getterPrimaryKey property>
  @Override
  <@java.getter type=getType(property.@type) name=property.@name methodName="getPrimaryKey" />
</#macro>

<#macro setterPrimaryKey property>
  @Override
  <@java.setter type=getType(property.@type) name=property.@name methodName="setPrimaryKey" argName="primaryKey"/>
</#macro>

<#macro getter property>
  <#if property?node_name = "id">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getter type=getEntityName(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getter type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getter type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  </#if>
</#macro>


<#macro setter property>
  <#if property?node_name = "id">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.setter type=getEntityName(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.setter type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.setter type=getType(property.@listType, getEntityName(xml.getAttribute(property.@targetEntity))) name=property.@name />
  </#if>
</#macro>

