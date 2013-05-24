<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />
<#include "id.inc">
<#include "column.inc">
<#include "many-to-one.inc">
<#include "one-to-many.inc">
<#include "many-to-many.inc">

<#function getJpaAnnotation entity property>
  <#local jpaAnnotation= "">
  <#if property?node_name = "id">
    <#local jpaAnnotation= getIdAnnotation(entity, property) />
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


<#function getTableAnnotation doc entity>
	<#local jpaAnnotation = "@Table(name=\"${entity.@tableName}\""/>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:constraints/j:uniqueConstraint" assignTo="uniqueConstraints" />
	<#if uniqueConstraints?? && uniqueConstraints?size gt 0>
		<#local jpaAnnotation = jpaAnnotation + ", uniqueConstraints = {" />
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
			<#local jpaAnnotation= jpaAnnotation + "@UniqueConstraint(columnNames = {"/>
			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<#local jpaAnnotation= jpaAnnotation + "\"${getColumnName(entity.@columnPrefix, prop)}\""/>
				<#if ucColumn_has_next>
					<#local jpaAnnotation= jpaAnnotation + ", "/>
				</#if>
			</#list>
			<#local jpaAnnotation= jpaAnnotation + "})"/>
			<#if uniqueConstraint_has_next>
				<#local jpaAnnotation= jpaAnnotation + ", "/>
			</#if>
		</#list>
		<#local jpaAnnotation= jpaAnnotation + "}"/>
	</#if>
	<#local jpaAnnotation = jpaAnnotation + ")" />
	<#return jpaAnnotation>
</#function>
