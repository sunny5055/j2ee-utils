<#macro daoConstant prefix fieldName extraFieldName="">
	String ${toUnderscoreCase(prefix + " " + fieldName + " " + extraFieldName)} = "${modelName?uncap_first}.${toCamelCase(prefix + " " + fieldName + " " + extraFieldName)}";  
</#macro>

<#macro countBy fieldType fieldName extraFieldName="">
	Integer ${toCamelCase("countBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>
	
<#macro countByImpl fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)> 
	@Override
	public Integer ${toCamelCase("countBy "+ fieldName + " " + extraFieldName)}(${fieldType} ${argName}) {
		return QueryUtil.getNumberByNamedQueryAndNamedParam(getCurrentSession(), ${daoName}.${toUnderscoreCase("countBy " + fieldName + " " + extraFieldName)}, new String[] { "${argName}" }, ${argName});
    }
</#macro>	
	
<#macro findBy fieldType fieldName extraFieldName="">
	${modelName} ${toCamelCase("findBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>

<#macro findByImpl fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	@Override	
	public ${modelName} ${toCamelCase("findBy " + fieldName + " " + extraFieldName)}(${fieldType} ${argName}) {
		return QueryUtil.getByNamedQueryAndNamedParam(getCurrentSession(), ${daoName}.${toUnderscoreCase("findBy " + fieldName + " " + extraFieldName)}, new String[] { "${argName}" }, ${argName});
    }
</#macro>

<#macro findAllBy fieldType fieldName extraFieldName="">	
	List<${modelName}> ${toCamelCase("findAllBy " + fieldName + " " + extraFieldName)}(${fieldType} ${toCamelCase(fieldName + " " + extraFieldName)});
</#macro>

<#macro findAllByImpl fieldType fieldName extraFieldName="">
	<#assign argName = toCamelCase(fieldName + " " + extraFieldName)>
	@Override	
	public List<${modelName}> ${toCamelCase("findAllBy " + fieldName + " " + extraFieldName)}(${fieldType} ${argName}) {
		return QueryUtil.findByNamedQueryAndNamedParam(getCurrentSession(), ${daoName}.${toUnderscoreCase("findAllBy " + fieldName + " " + extraFieldName)}, new String[] { "${argName}" }, ${argName});
    }
</#macro>
