<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />
<#include "embedded-id.inc">
<#include "unique-column.inc">
<#include "association.inc">

<#function hasNamedQuery doc entity>
	<#local primaryKey = getPrimaryKey(entity)>
	<#if primaryKey?? && primaryKey?node_name == "embedded-id">
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
	<#if uniqueColumns?? && uniqueColumns?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />
	<#if manyToOnes?? && manyToOnes?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:one-to-many" assignTo="oneToManys" />
	<#if oneToManys?? && oneToManys?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-many" assignTo="manyToManys" />
	<#if manyToManys?? && manyToManys?size gt 0>
		<#return "true">
	</#if>

	<#return "false">
</#function>


<#macro getNamedQueries doc entity>
	<#if hasNamedQuery(doc, entity) == "true">
		<#local primaryKey = getPrimaryKey(entity) />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:one-to-many" assignTo="oneToManys" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-many" assignTo="manyToManys" />

		<#local namedQueries = "" />
		<#if primaryKey?node_name = "embedded-id">
			<#local namedQueries = namedQueries + getEmbeddedIdNamedQuery(doc, entity, primaryKey) />
		</#if>
		<#if uniqueColumns??>
			<#list uniqueColumns as column>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getUniqueColumnNamedQuery(doc, entity, column) />
			</#list>
		</#if>
		<#if manyToOnes??>
			<#list manyToOnes as manyToOne>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getAssociationNamedQuery(doc, entity, manyToOne) />
			</#list>
		</#if>
		<#if oneToManys??>
			<#list oneToManys as oneToMany>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getAssociationNamedQuery(doc, entity, oneToMany) />
			</#list>
		</#if>
		<#if manyToManys??>
			<#list manyToManys as manyToMany>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getAssociationNamedQuery(doc, entity, manyToMany) />
			</#list>
		</#if>

		@NamedQueries({
			${namedQueries}
		})
	</#if>
</#macro>

