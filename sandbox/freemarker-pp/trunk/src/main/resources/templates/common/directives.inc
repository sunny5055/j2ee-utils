<#function addToMap map key value asString=true>
<#if map?length == 0>
	<#local first = true />
<#else>
	<#local first = false />
</#if>
<#local mapAsString = ""/>
<#if first = false>
    <#local mapAsString = map + ","/>
</#if>
<#local mapAsString = mapAsString + "\"" + key + "\":">
<#if asString>
	<#local mapAsString = mapAsString + "\"" + value + "\""/>
<#else>
	<#local mapAsString = mapAsString + value />
</#if>
<#return mapAsString />
</#function>

<#function addToList list value unique=false>
	<#if unique>
		<#if list?seq_contains(value)>
			<#return list/>
		<#else>
			<#return list + [value]/>
		</#if>
	<#else>
		<#return list + [value]/>
	</#if>
</#function>