<#import "common.ftl" as util>

package ${modelPackage}.queries;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;

<#if uniqueFields?? || associations??>
<#compress>
//TODO auto-generated, need to be completed
@NamedQueries({
<#if uniqueFields??>
<#list uniqueFields as uniqueField>
<@util.countByQuery fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />,
<@util.findByQuery fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" /><#if uniqueField_has_next>,</#if>
</#list>
<#if associations??>,</#if>
</#if>

<#if associations??>
<#list associations as association>
	<#if association.type.simpleName == "List" || association.type.simpleName == "Set">
	<#assign targetEntityType = getTypeParameterFromList(association.genericType) />
	<#else>
	<#assign targetEntityType = association.type />
	</#if>
	<#assign targetEntityName = association.name />
	<#assign targetEntityPk = getPrimaryKeyField(targetEntityType) />
	<#assign targetEntityUniqueFields = getUniqueFields(targetEntityType) />

	<@util.countByQuery fieldType="${association.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/>,
	<@util.findByQuery fieldType="${association.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityPk.name}"/><#if targetEntityUniqueFields??>,</#if>

	<#if targetEntityUniqueFields??>
	<#list targetEntityUniqueFields as targetEntityUniqueField>
	<@util.countByQuery fieldType="${association.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/>,
	<@util.findByQuery fieldType="${association.type.simpleName}" fieldName="${targetEntityName}" extraFieldName="${targetEntityUniqueField.name}"/><#if targetEntityUniqueField_has_next>,</#if>
	</#list>
	</#if>
	<#if association_has_next>,</#if>
</#list>
</#if>
})
</#compress>
</#if>
public class ${modelName} extends AbstractHibernateDto<Integer> {

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void setPrimaryKey(Integer primaryKey) {
		this.id = primaryKey;
	}

}
