<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getUniqueConstraintNamedQuery doc entity uniqueConstraint>
	<#local namedQuery= "">
	<#local entityName = getEntityName(entity.@name) />
	<#local columnPrefix = entity.@columnPrefix?lower_case />
	<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#if ucColumn_index != 0>
			<#local namedQuery= namedQuery + ", " />
		</#if>

		<#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(prop.@name, false, entityName) + ", query = \"select count(*) from ${entityName} as ${columnPrefix} where ${columnPrefix}.${prop.@name} = :${prop.@name}\")," />
		<#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(prop.@name, false, entityName) + ", query = \"from ${entityName} as ${columnPrefix} where ${columnPrefix}.${prop.@name} = :${prop.@name}\")" />
	</#list>


	<#local queryName= "" />
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local queryName = queryName + prop.@name?cap_first />
		<#if ucColumn_has_next>
			<#local queryName= queryName + "And"/>
		</#if>
	</#list>

	<#local namedQuery= namedQuery + ", @NamedQuery(name = " + getCountQueryConstant(queryName, false, entityName) />
	<#local namedQuery= namedQuery + ", query = \"select count(*) from ${entityName} as ${columnPrefix} where "/>
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local namedQuery = namedQuery + "${columnPrefix}.${prop.@name} = :${prop.@name}" />
		<#if ucColumn_has_next>
			<#local namedQuery= namedQuery + " and "/>
		</#if>
	</#list>
	<#local namedQuery= namedQuery + "\")" />

	<#local namedQuery= namedQuery + ", @NamedQuery(name = " + getFindQueryConstant(queryName, false, entityName) />
	<#local namedQuery= namedQuery + ", query = \"from ${entityName} as ${columnPrefix} where "/>
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local namedQuery = namedQuery + "${columnPrefix}.${prop.@name} = :${prop.@name}" />
		<#if ucColumn_has_next>
			<#local namedQuery= namedQuery + " and "/>
		</#if>
	</#list>
	<#local namedQuery= namedQuery + "\")" />

    <#return namedQuery />
</#function>

