<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getUniqueColumnNamedQuery doc entity property>
  <#local namedQuery= "">
  <#local entityName = getEntityName(entity.@name) />
  <#local columnPrefix = entity.@columnPrefix?lower_case />
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
      <#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(property.@name, true, entityName) + ", query = \"select count(*) from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name} = :${property.@name}\"), " />
      <#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(property.@name, true, entityName) + ", query = \"from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name} = :${property.@name}\")" />
  </#if>
  <#return namedQuery />
</#function>
