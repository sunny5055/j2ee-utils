<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getIdAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "id">
    <#local jpaAnnotation= "@Id">
    <#local jpaAnnotation= jpaAnnotation + " @GeneratedValue(strategy = GenerationType.AUTO)">
    <#local jpaAnnotation= jpaAnnotation + " @Column(">
    <#local jpaAnnotation= jpaAnnotation + "name = \"${getColumnName(entity.@columnPrefix, property)}\"">
    <#local jpaAnnotation= jpaAnnotation + ")">
  </#if>
  <#return jpaAnnotation>
</#function>

