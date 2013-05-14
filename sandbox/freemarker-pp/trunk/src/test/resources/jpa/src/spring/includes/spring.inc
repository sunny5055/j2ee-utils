<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />

<#function getEntityPackageNames xml>
	<#local entityPackageNames = [] />
	<#local entities = xml["//j:entity"]/>
	<#list entities as entity>
		<#local entityPackageName = entity["ancestor::p:package/@name"] />
		<#local entityPackageNames = dir.addToList(entityPackageNames, entityPackageName, true)  />
	</#list>

	<#return entityPackageNames />
</#function>
