<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "../../common.ftl">
<#include "id.ftl">
<#include "embedded-id.ftl">
<#include "column.ftl">
<#include "many-to-one.ftl">
<#include "one-to-many.ftl">
<#include "many-to-many.ftl">

<#function getJpaAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "id">
    <#local jpaAnnotation= getIdAnnotation(entity, property) />
  <#elseif property?node_name = "embedded-id">
    <#local jpaAnnotation= getEmbeddedIdAnnotation(entity, property) />
  <#elseif property?node_name = "column">
    <#local jpaAnnotation= getColumnAnnotation(entity, property) />
  <#elseif property?node_name = "many-to-one">
    <#local jpaAnnotation= getManyToOneAnnotation(entity, property) />
  <#elseif property?node_name = "one-to-many">
    <#local jpaAnnotation= getOneToManyAnnotation(entity, property) />
  <#elseif property?node_name = "many-to-many">
    <#local jpaAnnotation= getManyToManyAnnotation(entity, property) />
  </#if>
  <#return jpaAnnotation>
</#function>

