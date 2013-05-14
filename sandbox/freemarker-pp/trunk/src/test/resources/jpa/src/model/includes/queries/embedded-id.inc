<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getEmbeddedIdNamedQuery doc entity property>
  <#local namedQuery= "">
  <#local entityName = getEntityName(entity.@name) />
  <#local columnPrefix = entity.@columnPrefix?lower_case />
    <#if property?node_name = "embedded-id">
    	<#assign columns = property["j:properties/j:column"]>
      	<#list columns as column>
      		<#if column_index != 0>
      			<#local namedQuery= namedQuery + ", " />
      		</#if>

        	<#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getCountQueryName(column.@name, false, entity.@name) + "\", query = \"select count(*) from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${column.@name} = :${column.@name}\")," />
        	<#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getFindQueryName(column.@name, false, entity.@name) + "\", query = \"from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${column.@name} = :${column.@name}\")" />
    	</#list>
    </#if>
    <#return namedQuery />
</#function>
