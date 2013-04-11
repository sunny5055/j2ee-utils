<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "../../common.ftl">

<#macro getTestMethod doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["j:properties/j:column"]>
  	<#list columns as column>
  	@Test
  	<@java.operation visibility="public" returnType="void" methodName="test${getCountQueryName(column.@name, false)?cap_first}">
  		final List<${getType(column.@type)}> ${column.@name}s = get${column.@name?cap_first}s();
		final List<${getType(column.@type)}> fake${column.@name?cap_first}s = getFake${column.@name?cap_first}s();
		Integer count = null;

		for (final ${getType(column.@type)} ${column.@name} : ${column.@name}s) {
			count = service.${getCountQueryName(column.@name, false)}(${column.@name});
			Assert.assertNotNull(count);
			Assert.assertNotSame(0, count);
		}

		for (final ${getType(column.@type)} ${column.@name} : fake${column.@name?cap_first}s) {
			count = service.${getCountQueryName(column.@name, false)}(${column.@name});
			Assert.assertNotNull(count);
			Assert.assertEquals((Integer) 0, count);
		}
  	</@java.operation>

  	@Test
	<@java.operation visibility="public" returnType="void" methodName="test${getFindQueryName(column.@name, false)?cap_first}">
		final List<${getType(column.@type)}> ${column.@name}s = get${column.@name?cap_first}s();
		final List<${getType(column.@type)}> fake${column.@name?cap_first}s = getFake${column.@name?cap_first}s();
		List<${entity.@name}> entities = null;

		for (final ${getType(column.@type)} ${column.@name} : ${column.@name}s) {
			entities = service.${getFindQueryName(column.@name, false)}(${column.@name});
			Assert.assertNotNull(entities);
			Assert.assertNotSame(0, entities.size());
		}

		for (final ${getType(column.@type)} ${column.@name} : fake${column.@name?cap_first}s) {
			entities = service.${getFindQueryName(column.@name, false)}(${column.@name});
			Assert.assertNotNull(entities);
			Assert.assertEquals(0, entities.size());
		}
	</@java.operation>
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
  	@Test
  	<@java.operation visibility="public" returnType="void" methodName="testExistWith${property.@name?cap_first}">
  		final List<${getType(property.@type)}> ${property.@name}s = get${property.@name?cap_first}s();
		final List<${getType(property.@type)}> fake${property.@name?cap_first}s = getFake${property.@name?cap_first}s();
		boolean exist = false;

		for (final ${getType(property.@type)} ${property.@name} : ${property.@name}s) {
			exist = service.existWith${property.@name?cap_first}(${property.@name});
			Assert.assertTrue(exist);
		}

		for (final ${getType(property.@type)} ${property.@name} : fake${property.@name?cap_first}s) {
			exist = service.existWith${property.@name?cap_first}(${property.@name});
			Assert.assertFalse(exist);
		}
  	</@java.operation>

  	@Test
	<@java.operation visibility="public" returnType="void" methodName="test${getFindQueryName(property.@name, true)?cap_first}">
		final List<${getType(property.@type)}> ${property.@name}s = get${property.@name?cap_first}s();
		final List<${getType(property.@type)}> fake${property.@name?cap_first}s = getFake${property.@name?cap_first}s();
		${entity.@name} entity = null;

		for (final ${getType(property.@type)} ${property.@name} : ${property.@name}s) {
			entity = service.${getFindQueryName(property.@name, true)}(${property.@name});
			Assert.assertNotNull(entity);
		}

		for (final ${getType(property.@type)} ${property.@name} : fake${property.@name?cap_first}s) {
			entity = service.${getFindQueryName(property.@name, true)}(${property.@name});
			Assert.assertNull(entity);
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
		List<${entity.@name}> entities = null;

		for (final ${argType} ${argName} : ${argName}s) {
			entities = service.${getFindQueryName(argName, false)}(${argName});
			Assert.assertNotNull(entities);
			Assert.assertNotSame(0, entities.size());
		}

		for (final ${argType} ${argName} : fake${argName?cap_first}s) {
			entities = service.${getFindQueryName(argName, false)}(${argName});
			Assert.assertNotNull(entities);
			Assert.assertEquals(0, entities.size());
		}
	</@java.operation>
  </#if>
</#macro>


<#macro getValues doc entity property>
  <#if property?node_name = "embedded-id">
  	<#assign columns = property["j:properties/j:column"]>
  	<#list columns as column>
  	@SuppressWarnings("unchecked")
  	<@java.operation visibility="private" returnType="List<${getType(column.@type)}>" methodName="get${column.@name?cap_first}s">
		List<${getType(column.@type)}> ${column.@name}s = null;
		final List<Node> nodes = document.selectNodes("//" + getTableName());
		if (!CollectionUtil.isEmpty(nodes)) {
			${column.@name}s = new ArrayList<${getType(column.@type)}>();
			for (final Node node : nodes) {
				final String ${column.@name} = node.valueOf("@${getColumnName(entity.@columnPrefix, column)}");
				if (!StringUtil.isBlank(${column.@name})) {
					${column.@name}s.add(${util.java.convertFromString(getType(column.@type), column.@name)});
				}
			}
		}
		return ${column.@name}s;
  	</@java.operation>

  	<@java.operation visibility="private" returnType="List<${getType(column.@type)}>" methodName="getFake${column.@name?cap_first}s">
		List<${getType(column.@type)}> fake${column.@name?cap_first}s = null;
		List<${getType(column.@type)}> ${column.@name}s = get${column.@name?cap_first}s();
		if (!CollectionUtil.isEmpty(${column.@name}s)) {
			fake${column.@name?cap_first}s = new ArrayList<${getType(column.@type)}>();
			for (int i = 0; i < ${column.@name}s.size(); i++) {
				${getType(column.@type)} ${column.@name} = null;
				do {
					${column.@name} = ${java.getRandomValue(column.@type, xml.getAttribute(column.@length))};
				} while (${column.@name}s.contains(${column.@name}) || fake${column.@name?cap_first}s.contains(${column.@name}));

				if (${java.checkNotNull(column.@type, column.@name)}) {
					fake${column.@name?cap_first}s.add(${column.@name});
				}
			}
		}
		return fake${column.@name?cap_first}s;
  	</@java.operation>
	</#list>
  <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
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

