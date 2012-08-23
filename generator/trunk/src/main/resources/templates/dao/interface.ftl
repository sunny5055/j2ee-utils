<#import "common.ftl" as util> 
package ${daoPackage};

import java.util.List;
import com.google.code.jee.utils.dal.dao.GenericDao;
import ${modelPackage}.${modelName};

//TODO auto-generated, need to be completed
public interface ${daoName} extends GenericDao<${primaryKey.type.simpleName}, ${modelName}> {
<#compress>
<#if uniqueFields??>
<#list uniqueFields as uniqueField>
	<@util.daoConstant prefix="countBy" fieldName="${uniqueField.name}"/>
	<@util.daoConstant prefix="findBy" fieldName="${uniqueField.name}"/>
</#list>
</#if>
<#if associations??>
<#list associations as association>
	<#if association.type.simpleName == "List" || association.type.simpleName == "Set">
	<#assign targetEntityType = getTypeParameterFromList(association.genericType) />
	<#assign targetEntityName = association.name?substring(0,association.name?length-1) />
	<#else>
	<#assign targetEntityType = association.type />
	<#assign targetEntityName = association.name />
	</#if>
	<#assign targetEntityPk = getPrimaryKeyField(targetEntityType) />
	<#assign targetEntityUniqueFields = getUniqueFields(targetEntityType) />

	<@util.daoConstant prefix="countBy" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>
	<@util.daoConstant prefix="findAllBy" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>

	<#if targetEntityUniqueFields??>
	<#list targetEntityUniqueFields as targetEntityUniqueField>
	<@util.daoConstant prefix="countBy" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	<@util.daoConstant prefix="findAllBy" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	</#list>
	</#if>
</#list>	
</#if>
</#compress>


<#if uniqueFields??>
<#list uniqueFields as uniqueField>
	<@util.countBy fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />
	
	<@util.findBy fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />

</#list>
</#if>
<#if associations??>
<#list associations as association>
	<#if association.type.simpleName == "List" || association.type.simpleName == "Set">
	<#assign targetEntityType = getTypeParameterFromList(association.genericType) />
	<#assign targetEntityName = association.name?substring(0,association.name?length-1) />
	<#else>
	<#assign targetEntityType = association.type />
	<#assign targetEntityName = association.name />
	</#if>
	<#assign targetEntityPk = getPrimaryKeyField(targetEntityType) />
	<#assign targetEntityUniqueFields = getUniqueFields(targetEntityType) />
	
	<@util.countBy fieldType="${targetEntityPk.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>
	
	<@util.findAllBy fieldType="${targetEntityPk.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>
	
	<#if targetEntityUniqueFields??>
	<#list targetEntityUniqueFields as targetEntityUniqueField>
	<@util.countBy fieldType="${targetEntityUniqueField.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	
	<@util.findAllBy fieldType="${targetEntityUniqueField.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	</#list>
	</#if>
</#list>
</#if>
}