<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getOneToManyAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "one-to-many">
    <#local jpaAnnotation= "@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local jpaAnnotation= jpaAnnotation + "@JoinTable(name = \"${getJoinTableName(entity, property)}\"">
      <#local jpaAnnotation= jpaAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(entity, property)}\") }">
      <#local jpaAnnotation= jpaAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(entity, property)}\") }">
      <#local jpaAnnotation= jpaAnnotation + ")">
  </#if>
  <#return jpaAnnotation>
</#function>

