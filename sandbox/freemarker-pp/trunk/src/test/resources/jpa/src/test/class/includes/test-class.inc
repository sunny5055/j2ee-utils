<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />

<#macro getTestMethod doc entity property>
	<#local modelName = getModelName(entity.@name) />
  	<#local entityName = getEntityName(entity.@name) />
    <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@Test
  	<@java.operation visibility="public" returnType="void" methodName="testExistWith${property.@name?cap_first}">
  		final List<${getType(property.@type)}> ${property.@name}s = get${property.@name?cap_first}s();
		final List<${getType(property.@type)}> fake${property.@name?cap_first}s = getFake${property.@name?cap_first}s();
		boolean exists = false;

		for (final ${getType(property.@type)} ${property.@name} : ${property.@name}s) {
			exists = service.existsWith${property.@name?cap_first}(${property.@name});
			Assert.assertTrue(exists);
		}

		for (final ${getType(property.@type)} ${property.@name} : fake${property.@name?cap_first}s) {
			exists = service.existsWith${property.@name?cap_first}(${property.@name});
			Assert.assertFalse(exists);
		}
  	</@java.operation>

  	@Test
	<@java.operation visibility="public" returnType="void" methodName="test${getFindQueryName(property.@name, true)?cap_first}">
		final List<${getType(property.@type)}> ${property.@name}s = get${property.@name?cap_first}s();
		final List<${getType(property.@type)}> fake${property.@name?cap_first}s = getFake${property.@name?cap_first}s();
		${modelName} dto = null;

		for (final ${getType(property.@type)} ${property.@name} : ${property.@name}s) {
			dto = service.${getFindQueryName(property.@name, true)}(${property.@name});
			Assert.assertNotNull(dto);
		}

		for (final ${getType(property.@type)} ${property.@name} : fake${property.@name?cap_first}s) {
			dto = service.${getFindQueryName(property.@name, true)}(${property.@name});
			Assert.assertNull(dto);
		}
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

	  @Test
	<@java.operation visibility="public" returnType="void" methodName="test${getCountQueryName(argName, false)?cap_first}">
  		final List<${argType}> ${argName}s = get${argName?cap_first}s();
		final List<${argType}> fake${argName?cap_first}s = getFake${argName?cap_first}s();
		Integer count = null;

		for (final ${argType} ${argName} : ${argName}s) {
			count = service.${getCountQueryName(argName, false)}(${argName});
			Assert.assertNotNull(count);
			Assert.assertNotSame(0, count);
		}

		for (final ${argType} ${argName} : fake${argName?cap_first}s) {
			count = service.${getCountQueryName(argName, false)}(${argName});
			Assert.assertNotNull(count);
			Assert.assertEquals((Integer) 0, count);
		}
  	</@java.operation>

  	@Test
	<@java.operation visibility="public" returnType="void" methodName="test${getFindQueryName(argName, false)?cap_first}">
		final List<${argType}> ${argName}s = get${argName?cap_first}s();
		final List<${argType}> fake${argName?cap_first}s = getFake${argName?cap_first}s();
		List<${modelName}> dtos = null;

		for (final ${argType} ${argName} : ${argName}s) {
			dtos = service.${getFindQueryName(argName, false)}(${argName});
			Assert.assertNotNull(dtos);
			Assert.assertNotSame(0, dtos.size());
		}

		for (final ${argType} ${argName} : fake${argName?cap_first}s) {
			dtos = service.${getFindQueryName(argName, false)}(${argName});
			Assert.assertNotNull(dtos);
			Assert.assertEquals(0, dtos.size());
		}
	</@java.operation>
  </#if>
</#macro>




<#macro getTestMethodForConstraints doc entity>
  <#local modelName = getModelName(entity.@name) />
  <#local entityName = getEntityName(entity.@name) />
  <#local uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />
	<#if uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
  			<#local parameters = ""/>
  			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				@Test
			  	<@java.operation visibility="public" returnType="void" methodName="test${getCountQueryName(prop.@name, false)?cap_first}">
			  		final List<${getType(prop.@type)}> ${prop.@name}s = get${prop.@name?cap_first}s();
					final List<${getType(prop.@type)}> fake${prop.@name?cap_first}s = getFake${prop.@name?cap_first}s();
					Integer count = null;

					for (final ${getType(prop.@type)} ${prop.@name} : ${prop.@name}s) {
						count = service.${getCountQueryName(prop.@name, false)}(${prop.@name});
						Assert.assertNotNull(count);
						Assert.assertNotSame(0, count);
					}

					for (final ${getType(prop.@type)} ${prop.@name} : fake${prop.@name?cap_first}s) {
						count = service.${getCountQueryName(prop.@name, false)}(${prop.@name});
						Assert.assertNotNull(count);
						Assert.assertEquals((Integer) 0, count);
					}
			  	</@java.operation>

			  	@Test
				<@java.operation visibility="public" returnType="void" methodName="test${getFindQueryName(prop.@name, false)?cap_first}">
					final List<${getType(prop.@type)}> ${prop.@name}s = get${prop.@name?cap_first}s();
					final List<${getType(prop.@type)}> fake${prop.@name?cap_first}s = getFake${prop.@name?cap_first}s();
					List<${modelName}> dtos = null;

					for (final ${getType(prop.@type)} ${prop.@name} : ${prop.@name}s) {
						dtos = service.${getFindQueryName(prop.@name, false)}(${prop.@name});
						Assert.assertNotNull(dtos);
						Assert.assertNotSame(0, dtos.size());
					}

					for (final ${getType(prop.@type)} ${prop.@name} : fake${prop.@name?cap_first}s) {
						dtos = service.${getFindQueryName(prop.@name, false)}(${prop.@name});
						Assert.assertNotNull(dtos);
						Assert.assertEquals(0, dtos.size());
					}
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
			@Test
			public void testExistsWith${queryName?cap_first}() {
				<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					final List<${getType(prop.@type)}> ${prop.@name}s = get${prop.@name?cap_first}s();
					final List<${getType(prop.@type)}> fake${prop.@name?cap_first}s = getFake${prop.@name?cap_first}s();
					<#if !ucColumn_has_next>

					int maxSize = ${prop.@name}s.size();
					int maxFakeSize = fake${prop.@name?cap_first}s.size();
					</#if>
				</#list>
				boolean exists = false;

				for(int i = 0; i < maxSize; i++) {
					<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					${getType(prop.@type)} ${prop.@name} = ${prop.@name}s.get(i);
					</#list>
					exists = service.existsWith${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					Assert.assertTrue(exists);
				}

				for(int i = 0; i < maxFakeSize; i++) {
					<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					${getType(prop.@type)} ${prop.@name} = fake${prop.@name?cap_first}s.get(i);
					</#list>
					exists = service.existsWith${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					Assert.assertFalse(exists);
				}
			}

			@Test
			public void testFindBy${queryName?cap_first}() {
				<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					final List<${getType(prop.@type)}> ${prop.@name}s = get${prop.@name?cap_first}s();
					final List<${getType(prop.@type)}> fake${prop.@name?cap_first}s = getFake${prop.@name?cap_first}s();
					<#if !ucColumn_has_next>

					int maxSize = ${prop.@name}s.size();
					int maxFakeSize = fake${prop.@name?cap_first}s.size();
					</#if>
				</#list>
				${modelName} dto = null;

				for(int i = 0; i < maxSize; i++) {
					<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					${getType(prop.@type)} ${prop.@name} = ${prop.@name}s.get(i);
					</#list>
					dto = service.findBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					Assert.assertNotNull(dto);
				}

				for(int i = 0; i < maxFakeSize; i++) {
					<#list ucColumns as ucColumn>
					<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
					${getType(prop.@type)} ${prop.@name} = fake${prop.@name?cap_first}s.get(i);
					</#list>
					dto = service.findBy${queryName?cap_first}(<#list ucColumns as ucColumn><#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />${prop.@name}<#if ucColumn_has_next>, </#if></#list>);
					Assert.assertNull(dto);
				}
			}
		</#list>
	</#if>
</#macro>

<#macro getValues doc entity property>
  <#if property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@SuppressWarnings("unchecked")
  	<@java.operation visibility="private" returnType="List<${getType(property.@type)}>" methodName="get${property.@name?cap_first}s">
		List<${getType(property.@type)}> ${property.@name}s = null;
		final List<Node> nodes = document.selectNodes("//" + getTableName());
		if (!CollectionUtil.isEmpty(nodes)) {
			${property.@name}s = new ArrayList<${getType(property.@type)}>();
			for (final Node node : nodes) {
				final String ${property.@name} = node.valueOf("@${getColumnName(entity.@columnPrefix, property)}");
				if (!StringUtil.isBlank(${property.@name})) {
					${property.@name}s.add(${util.java.convertFromString(getType(property.@type), property.@name)});
				}
			}
		}
		return ${property.@name}s;
  	</@java.operation>

  	<@java.operation visibility="private" returnType="List<${getType(property.@type)}>" methodName="getFake${property.@name?cap_first}s">
		List<${getType(property.@type)}> fake${property.@name?cap_first}s = null;
		List<${getType(property.@type)}> ${property.@name}s = get${property.@name?cap_first}s();
		if (!CollectionUtil.isEmpty(${property.@name}s)) {
			fake${property.@name?cap_first}s = new ArrayList<${getType(property.@type)}>();
			for (int i = 0; i < ${property.@name}s.size(); i++) {
				${getType(property.@type)} ${property.@name} = null;
				do {
					${property.@name} = ${java.getRandomValue(property.@type, xml.getAttribute(property.@length))};
				} while (${property.@name}s.contains(${property.@name}) || fake${property.@name?cap_first}s.contains(${property.@name}));

				if (${java.checkNotNull(property.@type, property.@name)}) {
					fake${property.@name?cap_first}s.add(${property.@name});
				}
			}
		}
		return fake${property.@name?cap_first}s;
  	</@java.operation>
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#local argType = getType(property.@targetEntity)>
	  <#local tableName = "\"//\"+ getTableName()">
	  <#local columnName = getColumnName(entity.@columnPrefix, property)>
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#local argName = property.@name?substring(0, property.@name?length-1)>
	  	<#local tableName = "\"//${getJoinTableName(entity, property)}\"">
	  	<#local columnName = getInverseJoinColumnName(entity, property)>
	  <#else>
	  	<#local argName = property.@name>
	  </#if>
	  <@xPath xml=doc expression="//j:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
	  <#if targetEntity??>
		<#local argType = getPrimaryKeyType(targetEntity)>
		<#local argName = "${argName}Id">
	  </#if>

	  @SuppressWarnings("unchecked")
	  <@java.operation visibility="private" returnType="List<${argType}>" methodName="get${argName?cap_first}s">
		List<${argType}> ${argName}s = null;
		final List<Node> nodes = document.selectNodes(${tableName});
		if (!CollectionUtil.isEmpty(nodes)) {
			${argName}s = new ArrayList<${argType}>();
			for (final Node node : nodes) {
				final String ${argName} = node.valueOf("@${columnName}");
				if (!StringUtil.isBlank(${argName})) {
					${argName}s.add(${util.java.convertFromString(argType, argName)});
				}
			}
		}
		return ${argName}s;
  	  </@java.operation>

  	  <@java.operation visibility="private" returnType="List<${argType}>" methodName="getFake${argName?cap_first}s">
		List<${getType(argType)}> fake${argName?cap_first}s = null;
		List<${getType(argType)}> ${argName}s = get${argName?cap_first}s();
		if (!CollectionUtil.isEmpty(${argName}s)) {
			fake${argName?cap_first}s = new ArrayList<${getType(argType)}>();
			for (int i = 0; i < ${argName}s.size(); i++) {
				${getType(argType)} ${argName} = null;
				do {
					${argName} = ${java.getRandomValue(argType, xml.getAttribute(property.@length))};
				} while (${argName}s.contains(${argName}) || fake${argName?cap_first}s.contains(${argName}));

				if (${java.checkNotNull(argType, argName)}) {
					fake${argName?cap_first}s.add(${argName});
				}
			}
		}
		return fake${argName?cap_first}s;
  	  </@java.operation>
  </#if>
</#macro>



<#macro getValuesForConstraints doc entity>
	<#local modelName = getModelName(entity.@name) />
	<#local entityName = getEntityName(entity.@name) />
	<#local uniqueConstraints=entity["./j:constraints/j:uniqueConstraint"] />
	<#if uniqueConstraints?size gt 0>
		<#list uniqueConstraints as uniqueConstraint>
			<#local ucColumns = uniqueConstraint["./j:columns/j:column"]>
  			<#local parameters = ""/>
  			<#list ucColumns as ucColumn>
				<#local prop = entity["./j:properties/j:column[@name='${ucColumn}']"] />
				@SuppressWarnings("unchecked")
			  	<@java.operation visibility="private" returnType="List<${getType(prop.@type)}>" methodName="get${prop.@name?cap_first}s">
					List<${getType(prop.@type)}> ${prop.@name}s = null;
					final List<Node> nodes = document.selectNodes("//" + getTableName());
					if (!CollectionUtil.isEmpty(nodes)) {
						${prop.@name}s = new ArrayList<${getType(prop.@type)}>();
						for (final Node node : nodes) {
							final String ${prop.@name} = node.valueOf("@${getColumnName(entity.@columnPrefix, prop)}");
							if (!StringUtil.isBlank(${prop.@name})) {
								${prop.@name}s.add(${util.java.convertFromString(getType(prop.@type), prop.@name)});
							}
						}
					}
					return ${prop.@name}s;
			  	</@java.operation>

			  	<@java.operation visibility="private" returnType="List<${getType(prop.@type)}>" methodName="getFake${prop.@name?cap_first}s">
					List<${getType(prop.@type)}> fake${prop.@name?cap_first}s = null;
					List<${getType(prop.@type)}> ${prop.@name}s = get${prop.@name?cap_first}s();
					if (!CollectionUtil.isEmpty(${prop.@name}s)) {
						fake${prop.@name?cap_first}s = new ArrayList<${getType(prop.@type)}>();
						for (int i = 0; i < ${prop.@name}s.size(); i++) {
							${getType(prop.@type)} ${prop.@name} = null;
							do {
								${prop.@name} = ${java.getRandomValue(prop.@type, xml.getAttribute(prop.@length))};
							} while (${prop.@name}s.contains(${prop.@name}) || fake${prop.@name?cap_first}s.contains(${prop.@name}));

							if (${java.checkNotNull(prop.@type, prop.@name)}) {
								fake${prop.@name?cap_first}s.add(${prop.@name});
							}
						}
					}
					return fake${prop.@name?cap_first}s;
			  	</@java.operation>
			</#list>
		</#list>
	</#if>
</#macro>

