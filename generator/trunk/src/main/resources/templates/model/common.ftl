<#assign firstLetter = modelName?uncap_first?substring(0,1)/>

<#macro daoConstant prefix fieldName extraFieldName="">
	${daoName}.${toUnderscoreCase(prefix + " " + fieldName + " " + extraFieldName)}
</#macro>

<#macro countByQuery fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	<#if extraFieldName?length != 0>
		<#assign fullFieldName = fieldName + '.' + extraFieldName>
	<#else>
		<#assign fullFieldName = fieldName>
	</#if>
	<#if fieldType == "List" || fieldType == "Set">
	<#assign targetName = fieldName?uncap_first?substring(0,fieldName?length-1)/>
	@NamedQuery(name = <@daoConstant prefix="countBy" fieldName="${targetName}" extraFieldName="${extraFieldName}"/>, query = "select count(${firstLetter}) from ${modelName} as ${firstLetter} left join ${firstLetter}.${fieldName} as ${targetName} where ${targetName}.${extraFieldName} = :${argName}")
	<#else>
	@NamedQuery(name = <@daoConstant prefix="countBy" fieldName="${fieldName}" extraFieldName="${extraFieldName}"/>, query = "select count(*) from ${modelName} as ${firstLetter} where ${firstLetter}.${fullFieldName} = :${argName}")
	</#if>
</#macro>

<#macro findByQuery fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	<#if extraFieldName?length != 0>
		<#assign fullFieldName = fieldName + '.' + extraFieldName>
	<#else>
		<#assign fullFieldName = fieldName>
	</#if>
	<#if fieldType == "List" || fieldType == "Set">
	<#assign targetName = fieldName?uncap_first?substring(0,fieldName?length-1)/>
	@NamedQuery(name = <@daoConstant prefix="findAllBy" fieldName="${targetName}" extraFieldName="${extraFieldName}"/>, query = "select ${firstLetter} from ${modelName} as ${firstLetter} left join ${firstLetter}.${fieldName} as ${targetName} where ${targetName}.${extraFieldName} = :${argName}")
	<#else>
	@NamedQuery(name = <@daoConstant prefix="findBy" fieldName="${fieldName}" extraFieldName="${extraFieldName}"/>, query = "select ${firstLetter} from ${modelName} as ${firstLetter} where ${firstLetter}.${fullFieldName} = :${argName}")
	</#if>
</#macro>