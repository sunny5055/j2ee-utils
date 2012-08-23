<#import "common.ftl" as util>
package ${serviceImplPackage};

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import ${modelPackage}.${modelName};
import ${daoPackage}.${daoName};
import ${servicePackage}.${serviceName};

//TODO auto-generated, need to be completed
@Service
public class ${serviceImplName} extends AbstractGenericService<${primaryKey.type.simpleName}, ${modelName}, ${daoName}> implements ${serviceName} {

    @Override
    @Autowired
    public void setDao(${daoName} dao) {
        this.dao = dao;
    }

<#if uniqueFields??>
<#list uniqueFields as uniqueField>
	<@util.existWithImpl fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />
	
	<@util.findByImpl fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />	
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
	
	<@util.countByImpl fieldType="${targetEntityPk.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>
	
	<@util.findAllByImpl fieldType="${targetEntityPk.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>
	
	<#if targetEntityUniqueFields??>
	<#list targetEntityUniqueFields as targetEntityUniqueField>
	<@util.countByImpl fieldType="${targetEntityUniqueField.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	
	<@util.findAllByImpl fieldType="${targetEntityUniqueField.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>
	
	</#list>
	</#if>
</#list>
</#if>
}


