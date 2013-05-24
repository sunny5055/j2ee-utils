<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getAssociationNamedQuery doc entity property>
	<#local namedQuery= "">
	<#local entityName = getEntityName(entity.@name) />
	<#local columnPrefix = entity.@columnPrefix?lower_case />
	<#if property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	      <#local propertyName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	      <#local propertyName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
	 	<#local targetColumnPrefix = targetEntity.@columnPrefix?lower_case />
	    <#local targetColumn = getPrimaryKey(targetEntity)>
	    <#local propertyName = "${propertyName}Id">
	  </#if>

	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
		  <#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getCountQueryName(propertyName, false, entity.@name) + "\", query = \"select count(*) from ${entityName} as ${columnPrefix} left join ${columnPrefix}.${property.@name} as ${targetColumnPrefix} where ${targetColumnPrefix}.${targetColumn.@name} = :${propertyName}\")," />
	      <#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getFindQueryName(propertyName, false, entity.@name) + "\", query = \"select ${columnPrefix} from ${entityName} as ${columnPrefix} left join ${columnPrefix}.${property.@name} as ${targetColumnPrefix} where ${targetColumnPrefix}.${targetColumn.@name} = :${propertyName}\")" />
	  <#else>
	      <#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getCountQueryName(propertyName, false, entity.@name) + "\", query = \"select count(*) from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${targetColumn.@name} = :${propertyName}\")," />
	      <#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getFindQueryName(propertyName, false, entity.@name) + "\", query = \"select ${columnPrefix} from ${entityName} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${targetColumn.@name} = :${propertyName}\")" />
	  </#if>
    </#if>
	<#return namedQuery />
</#function>
