
<#macro existWith fieldType fieldName extraFieldName="">
	boolean ${toCamelCase("existWith " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>

<#macro existWithImpl fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)> 
	@Override
	public boolean ${toCamelCase("existWith "+ fieldName + " " + extraFieldName)}(${fieldType} ${argName}) {
		boolean exist = false;
		<#if fieldType =="String">
        if (!StringUtil.isEmpty(${argName})) {
        <#else>
        if (${argName} != null) {
        </#if>
             final Integer count = this.dao.${toCamelCase("countBy "+ fieldName + " " + extraFieldName)}(${argName});
             exist = count != 0;
         }
         return exist;
    }
</#macro>	
	
<#macro countBy fieldType fieldName extraFieldName="">
	Integer ${toCamelCase("countBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>
	
<#macro countByImpl fieldType fieldName extraFieldName="">
	<#assign methodName = toCamelCase("countBy "+ fieldName + " " + extraFieldName)>
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	@Override
	public Integer ${methodName}(${fieldType} ${argName}) {
		Integer count = 0;
		<#if fieldType =="String">
        if (!StringUtil.isEmpty(${argName})) {
        <#else>
        if (${argName} != null) {
        </#if>
             count = this.dao.${methodName}(${argName});
        }
		return count;
    }
</#macro>	
	
<#macro findBy fieldType fieldName extraFieldName="">	
	${modelName} ${toCamelCase("findBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>

<#macro findByImpl fieldType fieldName extraFieldName="">
	<#assign methodName = toCamelCase("findBy " + fieldName + " " + extraFieldName)>
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	@Override	
	public ${modelName} ${methodName}(${fieldType} ${argName}) {
		<#assign varName = "${modelName?uncap_first}">
		${modelName} ${varName} = null;
        <#if fieldType =="String">
        if (!StringUtil.isEmpty(${argName})) {
        <#else>
        if (${argName} != null) {
        </#if>
            ${varName} = this.dao.${methodName}(${argName});
        }
        return ${varName};
    }
</#macro>

<#macro findAllBy fieldType fieldName extraFieldName="">	
	List<${modelName}> ${toCamelCase("findAllBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>

<#macro findAllByImpl fieldType fieldName extraFieldName="">
	<#assign methodName = toCamelCase("findAllBy " + fieldName + " " + extraFieldName)>
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	@Override	
	public List<${modelName}> ${methodName}(${fieldType} ${argName}) {
		<#assign varName = "${modelName?uncap_first}s">
		List<${modelName}> ${varName} = null;
        <#if fieldType =="String">
        if (!StringUtil.isEmpty(${argName})) {
        <#else>
        if (${argName} != null) {
        </#if>
            ${varName} = this.dao.${methodName}(${argName});
        }
        return ${varName};
    }
</#macro>
