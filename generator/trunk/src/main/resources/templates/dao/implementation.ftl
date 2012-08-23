<#import "common.ftl" as util>
<#assign firstLetter = modelName?uncap_first?substring(0,1)/> 
package ${daoImplPackage};

import java.util.Map;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.google.code.jee.utils.dal.Search;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.dal.SortOrder;
import com.google.code.jee.utils.dal.dao.AbstractGenericDaoHibernate;
import com.google.code.jee.utils.dal.util.QueryUtil;
import ${daoPackage}.${daoName};
import ${modelPackage}.${modelName};

//TODO auto-generated, need to be completed
@Repository
public class ${daoImplName} extends AbstractGenericDaoHibernate<${primaryKey.type.simpleName}, ${modelName}> implements ${daoName} {

	public ${daoImplName}() {
        super(${modelName}.class);
    }

<#if uniqueFields??>
<#list uniqueFields as uniqueField>
	<@util.countByImpl fieldType="${uniqueField.type.simpleName}" fieldName="${uniqueField.name}" />
	
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

    @Override
    protected Search getSearch(SearchCriteria searchCriteria) {
        Search search = null;
        if (searchCriteria != null) {
            search = new Search();

            final StringBuilder buffer = new StringBuilder();
            buffer.append("from ${modelName} ${firstLetter} ");
            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        
                        <#assign index = 0>
                        <#if columnFields??>
                        <#list columnFields as field>
                        <#assign fieldType = field.type.simpleName />
                        <#if index == 0>
                        if (entry.getKey().equals("${field.name}")) {
                        <#else>
                        else if (entry.getKey().equals("${field.name}")) {
                        </#if>
                        	<#if fieldType == "String">
                            buffer.append("upper(${firstLetter}.${field.name}) like upper(:${field.name}) ");
                            <#else>
                            buffer.append("${firstLetter}.${field.name} = :${field.name} ");
                            </#if>
                            <#if fieldType == "Boolean">
                            search.addBooleanParameter("${field.name}", entry.getValue());
                            <#elseif fieldType == "Integer">
                            search.addIntegerParameter("${field.name}", entry.getValue());
                            <#elseif fieldType == "Date">
                            search.addDateParameter("${field.name}", entry.getValue(), "dd/MM/yyyy");
                            <#elseif fieldType == "Double">
                            search.addDoubleParameter("${field.name}", entry.getValue());
                            <#elseif fieldType == "Long">
                            search.addLongParameter("${field.name}", entry.getValue());
                            <#else>
                            search.addStringParameter("${field.name}", entry.getValue());
                            </#if>
                        }
                        <#assign index = index + 1>
						</#list>
                        </#if>
						<#if manyToOneFields??>
						<#list manyToOneFields as field>
                    	<#if index == 0>
                        if (entry.getKey().equals("${field.name}")) {
                        <#else>
                        else if (entry.getKey().equals("${field.name}")) {
                        </#if>
                        	//TODO to complete
                        }
                        <#assign index = index + 1>
						</#list>
						</#if>
						
                        index++;
                    }
                }
            }

            search.setCountQuery("select count(*) " + buffer.toString());

            if (searchCriteria.hasSorts()) {
                buffer.append("order by ");
                int index = 0;
                for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
                    if (index != 0) {
                        buffer.append(", ");
                    }
                   
                   	<#assign index = 0>
                   	<#if columnFields??>
                    <#list columnFields as field>
                    <#if index == 0>
                    if (entry.getKey().equals("${field.name}")) {
                    <#else>
                    else if (entry.getKey().equals("${field.name}")) {
                    </#if>
                        buffer.append("${firstLetter}.${field.name} ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                        	buffer.append("desc ");
                    	}
                    }
                    <#assign index = index + 1>
					</#list>
					</#if>
					<#if manyToOneFields??>
					<#list manyToOneFields as field>
                	<#if index == 0>
                    if (entry.getKey().equals("${field.name}")) {
                    <#else>
                    else if (entry.getKey().equals("${field.name}")) {
                    </#if>
                    	//TODO to complete
                    	 if (entry.getValue() == SortOrder.DESCENDING) {
                        	buffer.append("desc ");
                    	}
                    }
                    <#assign index = index + 1>
					</#list>
					</#if>
                    index++;
                }
            }
            search.setQuery(buffer.toString());
        }
        return search;
    }
}


