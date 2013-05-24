<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />


<#macro getInterfaceMethod doc entity property>
  <#local modelName = getModelName(entity.@name) />
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	<@java.interfaceOperation returnType="boolean" methodName="existsWith${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}" />
	<@java.interfaceOperation returnType="${modelName}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}" />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>
		<#local argName = "${argName}Id">
	  </#if>
  	<@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(argName, false) parameters="${argType} ${argName}" />
	<@java.interfaceOperation returnType="List<${modelName}>" methodName=getFindQueryName(argName, false) parameters="${argType} ${argName}" />
  </#if>
</#macro>


<#macro getMethod doc entity property>
  <#local modelName = getModelName(entity.@name) />
  <#local entityName = getEntityName(entity.@name) />
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@Override
  	<@java.operation visibility="public" returnType="boolean" methodName="existsWith${property.@name?cap_first}" parameters="${getType(property.@type)} ${property.@name}">
  		boolean exists = false;
  		if(${java.checkNotNull(getType(property.@type), property.@name)}) {
  			final Integer count = dao.${getCountQueryName(property.@name, true)}(${property.@name});
  			exists = count != 0;
  		}
  		return exists;
  	</@java.operation>

  	@Override
	<@java.operation visibility="public" returnType="${modelName}" methodName=getFindQueryName(property.@name, true) parameters="${getType(property.@type)} ${property.@name}">
        ${modelName} ${entity.@name?uncap_first} = null;
		if(${java.checkNotNull(getType(property.@type), property.@name)}) {
            ${entityName} entity = dao.${getFindQueryName(property.@name, true)}(${property.@name});
            ${entity.@name?uncap_first} = toDto(entity);
  		}
        return ${entity.@name?uncap_first};
	</@java.operation>
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>
		<#local argName = "${argName}Id">
	  </#if>

	  @Override
	<@java.operation visibility="public" returnType="Integer" methodName=getCountQueryName(argName, false) parameters="${argType} ${argName}">
  		Integer count = 0;
  		if(${java.checkNotNull(argType, argName)}) {
  			count = dao.${getCountQueryName(argName, false)}(${argName});
  		}
  		return count;
  	</@java.operation>

  	@Override
	<@java.operation visibility="public" returnType="List<${modelName}>" methodName=getFindQueryName(argName, false) parameters="${argType} ${argName}">
		List<${modelName}> list = null;
		if(${java.checkNotNull(argType, argName)}) {
  			List<${entityName}> entities = dao.${getFindQueryName(argName, false)}(${argName});
  			list = toDtos(entities);
  		}
  		return list;
	</@java.operation>
  </#if>
</#macro>


<#macro getInterfaceMethodForConstraints doc entity>
	<#local modelName = getModelName(entity.@name) />
	<#local uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />
	<#if uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
  			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				<@java.interfaceOperation returnType="Integer" methodName=getCountQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}" />
				<@java.interfaceOperation returnType="List<${modelName}>" methodName=getFindQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}" />
			</#list>

			boolean existsWith(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
			${modelName} findBy(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
		</#list>
	</#if>
</#macro>


<#macro getMethodForConstraints doc entity>
  <#local modelName = getModelName(entity.@name) />
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
			  		Integer count = 0;
			  		if(${java.checkNotNull(getType(prop.@type), prop.@name)}) {
			  			count = dao.${getCountQueryName(prop.@name, false)}(${prop.@name});
			  		}
			  		return count;
			  	</@java.operation>

			  	@Override
				<@java.operation visibility="public" returnType="List<${modelName}>" methodName=getFindQueryName(prop.@name, false) parameters="${getType(prop.@type)} ${prop.@name}">
			  		List<${modelName}> list = null;
					if(${java.checkNotNull(getType(prop.@type), prop.@name)}) {
			  			List<${entityName}> entities = dao.${getFindQueryName(prop.@name, false)}(${prop.@name});
			  			list = toDtos(entities);
			  		}
			  		return list;
				</@java.operation>
			</#list>

			@Override
			public boolean existsWith(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>) {
				boolean exists = false;
				if(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${java.checkNotNull(getType(prop.@type), prop.@name)}<#if ucColumn_has_next>&& </#if></#list>) {
					Integer count = dao.countBy(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					exists = count != 0;
				}
				return exists;
			}

			@Override
			public ${modelName} findBy(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${getType(prop.@type)} ${prop.@name}<#if ucColumn_has_next>, </#if></#list>) {
				${modelName} ${modelName?uncap_first} = null;
				if(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${java.checkNotNull(getType(prop.@type), prop.@name)}<#if ucColumn_has_next>&& </#if></#list>) {
					${entityName} entity = dao.findBy(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					${modelName?uncap_first} = toDto(entity);
				}
				return ${modelName?uncap_first};
			}
		</#list>
	</#if>
</#macro>

