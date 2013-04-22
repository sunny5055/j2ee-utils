<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getManyToManyAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "many-to-many">
    <#local jpaAnnotation= "@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local jpaAnnotation= jpaAnnotation + "@JoinTable(name = \"${getJoinTableName(entity, property)}\"">
      <#local jpaAnnotation= jpaAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(entity, property)}\") }">
      <#local jpaAnnotation= jpaAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(entity, property)}\") }">
      <#local jpaAnnotation= jpaAnnotation + ")">
  </#if>
  <#return jpaAnnotation>
</#function>

