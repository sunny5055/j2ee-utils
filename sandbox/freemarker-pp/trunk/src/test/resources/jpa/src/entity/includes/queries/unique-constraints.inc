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

		<#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getCountQueryName(prop.@name, false, entity.@name) + "\", query = \"select count(*) from ${entityName} as ${columnPrefix} where ${columnPrefix}.${prop.@name} = :${prop.@name}\")," />
		<#local namedQuery= namedQuery + "@NamedQuery(name = \"" + getFindQueryName(prop.@name, false, entity.@name) + "\", query = \"from ${entityName} as ${columnPrefix} where ${columnPrefix}.${prop.@name} = :${prop.@name}\")" />
	</#list>

	<#local namedQuery= namedQuery + ", @NamedQuery(name = \"" + entity.@name?uncap_first + ".countBy" />
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local namedQuery = namedQuery + prop.@name?cap_first />
		<#if ucColumn_has_next>
			<#local namedQuery= namedQuery + "And"/>
		</#if>
	</#list>
	<#local namedQuery= namedQuery + "\", query = \"select count(*) from ${entityName} as ${columnPrefix} where "/>
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local namedQuery = namedQuery + "${columnPrefix}.${prop.@name} = :${prop.@name}" />
		<#if ucColumn_has_next>
			<#local namedQuery= namedQuery + " and "/>
		</#if>
	</#list>
	<#local namedQuery= namedQuery + "\")" />

	<#local namedQuery= namedQuery + ", @NamedQuery(name = \"" + entity.@name?uncap_first + ".findBy" />
	<#list ucColumns as ucColumn>
		<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
		<#local namedQuery = namedQuery + prop.@name?cap_first />
		<#if ucColumn_has_next>
			<#local namedQuery= namedQuery + "And"/>
		</#if>
	</#list>
	<#local namedQuery= namedQuery + "\", query = \"from ${entityName} as ${columnPrefix} where "/>
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

