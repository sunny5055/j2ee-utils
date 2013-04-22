<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getEmbeddedIdAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "embedded-id">
    <#local jpaAnnotation= "@EmbeddedId">
  </#if>
  <#return jpaAnnotation>
</#function>

