<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />
<#include "unique-constraints.inc">
<#include "unique-column.inc">
<#include "association.inc">

<#function hasNamedQuery doc entity>
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:constraints/j:uniqueConstraint" assignTo="uniqueConstraints" />
	<#if uniqueConstraints?? && uniqueConstraints?size gt 0>
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


<#macro getQueryNames doc entity>
	<#local entityName = getEntityName(entity.@name) />
	<#local modelName = getModelName(entity.@name) />
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:constraints/j:uniqueConstraint" assignTo="uniqueConstraints" />
	<#if uniqueConstraints?? && uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<@java.getStaticProperty type="String" name=getCountQueryConstant(prop.@name, false) value=getCountQueryName(prop.@name, false, modelName) />
				<@java.getStaticProperty type="String" name=getFindQueryConstant(prop.@name, false) value=getFindQueryName(prop.@name, false, modelName) />
			</#list>

			<#local queryName= "" />
			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<#local queryName = queryName + prop.@name?cap_first />
				<#if ucColumn_has_next>
					<#local queryName= queryName + "And"/>
				</#if>
			</#list>
			<@java.getStaticProperty type="String" name=getCountQueryConstant(queryName, false) value=getCountQueryName(queryName, false, modelName) />
			<@java.getStaticProperty type="String" name=getFindQueryConstant(queryName, false) value=getFindQueryName(queryName, false, modelName) />
		</#list>
	</#if>

	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
	<#if uniqueColumns?? && uniqueColumns?size gt 0>
		<#list uniqueColumns as uniqueColumn>
			<@java.getStaticProperty type="String" name=getCountQueryConstant(uniqueColumn.@name, true) value=getCountQueryName(uniqueColumn.@name, true, modelName) />
    		<@java.getStaticProperty type="String" name=getFindQueryConstant(uniqueColumn.@name, true) value=getFindQueryName(uniqueColumn.@name, true, modelName) />
    	</#list>
	</#if>

	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />
	<#if manyToOnes?? && manyToOnes?size gt 0>
		<#list manyToOnes as manyToOne>
    		<#local propertyName = manyToOne.@name>
		  	<@xPath xml=doc expression="//j:entity[@name='${getType(manyToOne.@targetEntity)}']" assignTo="targetEntity" />
  			<#if targetEntity??>
    			<#local propertyName = "${propertyName}Id">
			</#if>

  			<@java.getStaticProperty type="String" name=getCountQueryConstant(propertyName, false) value=getCountQueryName(propertyName, false, modelName) />
  			<@java.getStaticProperty type="String" name=getFindQueryConstant(propertyName, false) value=getFindQueryName(propertyName, false, modelName) />
		</#list>
	</#if>

	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:one-to-many" assignTo="oneToManys" />
	<#if oneToManys?? && oneToManys?size gt 0>
		<#list oneToManys as oneToMany>
			<#local propertyName = oneToMany.@name?substring(0, oneToMany.@name?length-1)>
			<@xPath xml=doc expression="//j:entity[@name='${getType(oneToMany.@targetEntity)}']" assignTo="targetEntity" />
			<#if targetEntity??>
				<#local propertyName = "${propertyName}Id">
			</#if>

			<@java.getStaticProperty type="String" name=getCountQueryConstant(propertyName, false) value=getCountQueryName(propertyName, false, modelName) />
			<@java.getStaticProperty type="String" name=getFindQueryConstant(propertyName, false) value=getFindQueryName(propertyName, false, modelName) />
		</#list>
	</#if>

	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-many" assignTo="manyToManys" />
	<#if manyToManys?? && manyToManys?size gt 0>
		<#list manyToManys as manyToMany>
			<#local propertyName = manyToMany.@name?substring(0, manyToMany.@name?length-1)>
			<@xPath xml=doc expression="//j:entity[@name='${getType(manyToMany.@targetEntity)}']" assignTo="targetEntity" />
			<#if targetEntity??>
				<#local propertyName = "${propertyName}Id">
			</#if>

			<@java.getStaticProperty type="String" name=getCountQueryConstant(propertyName, false) value=getCountQueryName(propertyName, false, modelName) />
			<@java.getStaticProperty type="String" name=getFindQueryConstant(propertyName, false) value=getFindQueryName(propertyName, false, modelName) />
		</#list>
	</#if>


</#macro>


<#macro getNamedQueries doc entity>
	<#if hasNamedQuery(doc, entity) == "true">
		<#local primaryKey = getPrimaryKey(entity) />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-one" assignTo="manyToOnes" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:one-to-many" assignTo="oneToManys" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/j:many-to-many" assignTo="manyToManys" />
		<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:constraints/j:uniqueConstraint" assignTo="uniqueConstraints" />

		<#local namedQueries = "" />
		<#if uniqueConstraints?? && uniqueConstraints?size gt 0>
			<#list uniqueConstraints as uniqueConstraint>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getUniqueConstraintNamedQuery(doc, entity, uniqueConstraint) />
			</#list>
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

