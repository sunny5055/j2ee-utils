<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />

<#macro getInterfaceMethod doc entity property>
  <#local entityName = getEntityName(entity.@name) />
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
    <@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}" />
  <@java.interfaceOperation returnType="${entityName}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}" />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local argType = getType(property.@targetEntity)>
    <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
      <#local propertyName = property.@name?substring(0, property.@name?length-1)>
    <#else>
      <#local propertyName = property.@name>
    </#if>
    <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
    <#if targetEntity??>
    <#local argType = getPrimaryKeyType(targetEntity)>
    <#local propertyName = "${propertyName}Id">
    </#if>
    <@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(propertyName, false) parameters="${argType} ${propertyName}" />
  <@java.interfaceOperation returnType="List<${entityName}>" methodName=getFindQueryName(propertyName, false) parameters="${argType} ${propertyName}" />
  </#if>
</#macro>


<#macro getMethod doc entity property>
  <#local entityName = getEntityName(entity.@name) />
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
    @Override
    <@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}">
      return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, ${getCountQueryConstant(property.@name, true, entityName)},
                new String[] { "${property.@name}" }, ${property.@name});
    </@java.operation>

  @Override
  <@java.operation visibility="public" returnType="${entityName}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}">
    return QueryUtil.getByNamedQueryAndNamedParam(entityManager,  ${getFindQueryConstant(property.@name, true, entityName)},
                new String[] { "${property.@name}" }, ${property.@name});
  </@java.operation>
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local argType = getType(property.@targetEntity)>
    <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
      <#local propertyName = property.@name?substring(0, property.@name?length-1)>
    <#else>
      <#local propertyName = property.@name>
    </#if>
    <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
    <#if targetEntity??>
    <#local argType = getPrimaryKeyType(targetEntity)>
    <#local propertyName = "${propertyName}Id">
    </#if>

  @Override
    <@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(propertyName, false) parameters="${argType} ${propertyName}">
      return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, ${getCountQueryConstant(propertyName, false, entityName)},
                new String[] { "${propertyName}" }, ${propertyName});
    </@java.operation>

  @Override
  <@java.operation visibility="public" returnType="List<${entityName}>" methodName=getFindQueryName(propertyName, false) parameters="${argType} ${propertyName}">
    return QueryUtil.findByNamedQueryAndNamedParam(entityManager, ${getFindQueryConstant(propertyName, false, entityName)},
                new String[] { "${propertyName}" }, ${propertyName});
  </@java.operation>
  </#if>
</#macro>


<#macro getInterfaceMethodForConstraints doc entity>
	<#local entityName = getEntityName(entity.@name) />
	<#local uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />
	<#if uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
  			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}" />
				<@java.interfaceOperation returnType="List<${entityName}>" methodName=getFindQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}" />
			</#list>

			<#local queryName= "" />
			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<#local queryName = queryName + prop.@name?cap_first />
				<#if ucColumn_has_next>
					<#local queryName= queryName + "And"/>
				</#if>
			</#list>
			Integer countBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
			${entityName} findBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
		</#list>
	</#if>
</#macro>


<#macro getMethodForConstraints doc entity>
  <#local entityName = getEntityName(entity.@name) />
  <#local uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />
	<#if uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
  			<#local parameters = ""/>
  			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<#if parameters?length gt 0>
					<#local parameters = parameters + ", "/>
				</#if>
				<#local parameters = parameters + "${getType(prop.@type)} ${prop.@name}"/>
				@Override
			  	<@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}">
			  		return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, ${getCountQueryConstant(prop.@name, false, entityName)},
                	new String[] { "${prop.@name}" }, ${prop.@name});
			  	</@java.operation>

			  	@Override
				<@java.operation visibility="public" returnType="List<${entityName}>" methodName=getFindQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}">
			  		return QueryUtil.findByNamedQueryAndNamedParam(entityManager, ${getFindQueryConstant(prop.@name, false, entityName)},
                	new String[] { "${prop.@name}" }, ${prop.@name});
				</@java.operation>
			</#list>

			<#local queryName= "" />
			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<#local queryName = queryName + prop.@name?cap_first />
				<#if ucColumn_has_next>
					<#local queryName= queryName + "And"/>
				</#if>
			</#list>
			@Override
			public Integer countBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>) {
				return QueryUtil.getNumberByNamedQueryAndNamedParam(entityManager, ${getCountQueryConstant(queryName, false, entityName)},
                	new String[] { <#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />"${prop.@name}"<#if ucColumn_has_next>, </#if></#list> }, <#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
			}

			@Override
			public ${entityName} findBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>) {
				return QueryUtil.getByNamedQueryAndNamedParam(entityManager, ${getFindQueryConstant(queryName, false, entityName)},
                	new String[] { <#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />"${prop.@name}"<#if ucColumn_has_next>, </#if></#list> }, <#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
			}
		</#list>
	</#if>
</#macro>


<#macro getSearchMethod doc entity>
	<#local entityName = getEntityName(entity.@name) />
	<@xPath xml=doc expression="//j:entity[@name='${entity.@name}']/j:properties/*" assignTo="allProperties" />
  @Override
  <@java.operation visibility="protected" returnType="Search" methodName="getSearch" parameters="SearchCriteria searchCriteria">
    <#local columnPrefix = entity.@columnPrefix?lower_case />
        Search search = null;
        if (searchCriteria != null) {
            search = new Search();

            final StringBuilder buffer = new StringBuilder();
            buffer.append("from ${entityName} as ${columnPrefix} ");

            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }

                        <#list allProperties as property>
                          	<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
				                <#local propertyName = property.@name?substring(0, property.@name?length-1)>
		              		<#else>
				                <#local propertyName = property.@name>
			              	</#if>
				            <#if property?node_name = "column">
					            <#if property_index != 0>
					              else
					            </#if>
				                <@getSearchParameter columnPrefix=columnPrefix propertyName=propertyName propertyType=getType(property.@type) />
				            <#elseif property?node_name = "many-to-one">
		                 		<@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
			                	<#if targetEntity??>
						            <#if property_index != 0>
					              	else
					            	</#if>
			                    	<@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
			                    	<#if uniqueColumns?? && uniqueColumns?size == 1>
			                      		<#local targetColumn = uniqueColumns[0]>
			                    	<#else>
			                    		<#local targetColumn = getPrimaryKey(targetEntity)>
			                  		</#if>

				                  	<@getSearchParameter columnPrefix=columnPrefix propertyName=propertyName propertyType=getType(targetColumn.@type) useLike=false fieldPath="${columnPrefix}.${propertyName}.${targetColumn.@name}" />
				                <#else>
				                	//TODO need to be completed
			                  	</#if>
				        	</#if>
			            </#list>

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

                    <#list allProperties as property>
						<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
							<#local propertyName = property.@name?substring(0, property.@name?length-1)>
						<#else>
					  		<#local propertyName = property.@name>
						</#if>
						<#if property?node_name = "column">
							<#if property_index != 0>
								else
							</#if>
					  		<@getSortParameter columnPrefix=columnPrefix propertyName=propertyName />
						<#elseif property?node_name = "many-to-one">
					  		<@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
					  		<#if targetEntity??>
								<#if property_index != 0>
									else
								</#if>
								<@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']/j:properties/j:column[@unique='true']" assignTo="uniqueColumns" />
								<#if uniqueColumns?? && uniqueColumns?size == 1>
					  				<#local targetColumn = uniqueColumns[0]>
								<#else>
									<#local targetColumn = getPrimaryKey(targetEntity)>
					  			</#if>

					  			<@getSortParameter columnPrefix=columnPrefix propertyName=propertyName fieldPath="${columnPrefix}.${propertyName}.${targetColumn.@name}" />
							<#else>
					 			//TODO need to be completed
					  		</#if>
					  	</#if>
		          	</#list>

                    index++;
                }
            }

            search.setQuery(buffer.toString());
        }
        return search;
    </@java.operation>
</#macro>

<#macro getSearchParameter columnPrefix propertyName propertyType useLike=true fieldPath="${columnPrefix}.${propertyName}">
  if (entry.getKey().equals("${propertyName}")) {
  <#if propertyType == "Double"  || propertyType == "double">
    buffer.append("${fieldPath} = :${propertyName} ");
    search.addDoubleParameter("${propertyName}", entry.getValue());
  <#elseif propertyType == "Integer" || propertyType == "int"
    || propertyType == "Byte" || propertyType == "byte"
    || propertyType == "Short" || propertyType == "short">
    buffer.append("${fieldPath} = :${propertyName} ");
    search.addIntegerParameter("${propertyName}", entry.getValue());
  <#elseif propertyType == "Long" || propertyType == "long">
    buffer.append("${fieldPath} = :${propertyName} ");
    search.addLongParameter("${propertyName}", entry.getValue());
  <#elseif propertyType == "Date">
    buffer.append("${fieldPath} = :${propertyName} ");
    search.addDateParameter("${propertyName}", entry.getValue(), "dd/MM/yyyy");
  <#elseif propertyType == "Boolean" || propertyType == "boolean">
      buffer.append("${fieldPath} = :${propertyName} ");
    search.addBooleanParameter("${propertyName}", entry.getValue());
  <#else>
  	<#if useLike>
    	buffer.append("upper(${fieldPath}) like upper(:${propertyName}) ");
    <#else>
    	buffer.append("${fieldPath} = :${propertyName} ");
    </#if>
    search.addStringParameter("${propertyName}", entry.getValue());
  </#if>
  }
</#macro>


<#macro getSortParameter columnPrefix propertyName fieldPath="${columnPrefix}.${propertyName}">
  if (entry.getKey().equals("${propertyName}")) {
    buffer.append("${fieldPath} ");
        if (entry.getValue() == SortOrder.DESCENDING) {
            buffer.append("desc ");
        }
    }
</#macro>
