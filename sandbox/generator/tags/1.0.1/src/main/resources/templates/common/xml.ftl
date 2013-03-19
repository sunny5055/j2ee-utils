<#function existAttribute attribute>
	<#return attribute[0]??>
</#function>

<#function getAttribute attribute defaultValue="">
	<#local value = defaultValue>
	<#if attribute[0]??>
		<#local value = attribute>
	</#if>
	<#return value>
</#function>
