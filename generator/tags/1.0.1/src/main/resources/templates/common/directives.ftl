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


<#function addToList list value>
<#if list?length == 0>
	<#local first = true />
<#else>
	<#local first = false />
</#if>
<#local listAsString = ""/>
<#if first = false>
    <#local listAsString = list + ","/>
</#if>
<#local listAsString = listAsString + "\"" +value + "\""/>
<#return listAsString />
</#function>