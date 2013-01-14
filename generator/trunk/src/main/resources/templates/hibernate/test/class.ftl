<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if servicePackageName?? && servicePackageName?length gt 0>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.test.context.ContextConfiguration" />
<@addTo assignTo="imports" element="org.junit.Test" />
<@addTo assignTo="imports" element="com.github.springtestdbunit.annotation.DatabaseSetup" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />

<#if primaryKey?node_name == "embedded-id">
  <@addTo assignTo="imports" element="${embeddedIdPackageName}.${embeddedIdName}" />
    <#assign pkColumns = primaryKey["h:properties/h:column"]>
    <#list pkColumns as pkColumn>
  		<#if pkColumn.@type == "String">
			<@addTo assignTo="imports" element="org.apache.commons.lang3.RandomStringUtils" />
		<#else>
			<@addTo assignTo="imports" element="java.util.Random" />
		</#if>
	</#list>
<#else>
	<#if util.getPrimaryKeyType(entity) == "String">
		<@addTo assignTo="imports" element="org.apache.commons.lang3.RandomStringUtils" />
	<#else>
		<@addTo assignTo="imports" element="java.util.Random" />
	</#if>
</#if>

<#list columns as column>
<#if column.@type == "String">
	<@addTo assignTo="imports" element="org.apache.commons.lang3.RandomStringUtils" />
<#else>
	<@addTo assignTo="imports" element="java.util.Random" />
</#if>
</#list>

<#if util.xml.getAttribute(entity.@readOnly) == "true">
  <@addTo assignTo="imports" element="com.googlecode.jutils.dal.test.AbstractGenericReadServiceTest" />
<#else>
  <@addTo assignTo="imports" element="com.googlecode.jutils.dal.test.AbstractGenericServiceTest" />
</#if>

<@addTo assignTo="imports" element="junit.framework.Assert" />

<@addTo assignTo="imports" element="org.dom4j.Node" />
<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.collection.CollectionUtil" />
<@addTo assignTo="imports" element="java.util.List" />



${getImports(true, servicePackageName, imports)}


@ContextConfiguration(locations = { "classpath:spring/test-context.xml" })
@DatabaseSetup(value = "classpath:dataset/${serviceName}Test.xml")
<@compress single_line=true>
public class ${serviceName}Test extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
  AbstractGenericReadServiceTest<${util.getPrimaryKeyType(entity)}, ${entity.@name}, ${serviceName}>
<#else>
  AbstractGenericServiceTest<${util.getPrimaryKeyType(entity)}, ${entity.@name}, ${serviceName}>
</#if>
 {
</@compress>
  public ${serviceName}Test() {
    super(${util.getPrimaryKeyType(entity)}.class, ${entity.@name}.class);
  }

  @Autowired
  @Override
  public void setService(${serviceName} service) {
    this.service = service;
  }

  @Override
  public void testCountWithSearchCriteria() {
    // TODO need to be completed
  }

  @Override
  public void testFindAllWithSearchCriteria() {
    // TODO need to be completed
  }

  <@util.getMethod doc=xml entity=entity property=primaryKey/>
  <#list columns as column>
  <@util.getMethod doc=xml entity=entity property=column/>
  </#list>
  <#list manyToOnes as manyToOne>
  <@util.getMethod doc=xml entity=entity property=manyToOne/>
  </#list>
  <#list oneToManys as oneToMany>
  <@util.getMethod doc=xml entity=entity property=oneToMany/>
  </#list>
  <#list manyToManys as manyToMany>
  <@util.getMethod doc=xml entity=entity property=manyToMany/>
  </#list>

  @Override
  protected ${util.getPrimaryKeyType(entity)} getPrimaryKey(Node node) {
    ${util.getPrimaryKeyType(entity)} id = null;
    if (node != null) {
      <#if primaryKey?node_name == "embedded-id">
      <#assign pkColumns = primaryKey["h:properties/h:column"]>
        <#list pkColumns as pkColumn>
      final String ${pkColumn.@name}Value = node.valueOf("@${util.getColumnName(entity.@columnPrefix, pkColumn)}");
      </#list>

      id = new ${embeddedIdName}();
      <#list pkColumns as pkColumn>
      id.set${pkColumn.@name?cap_first}(${util.java.convertFromString(pkColumn.@type, pkColumn.@name+"Value")});
      </#list>
      <#else>
      final String stringValue = node.valueOf("@${util.getColumnName(entity.@columnPrefix, primaryKey)}");
      if (!StringUtil.isBlank(stringValue)) {
        id = ${util.java.convertFromString(util.getPrimaryKeyType(entity), "stringValue")};
      }
      </#if>
    }
    return id;
  }

  @Override
  protected ${util.getPrimaryKeyType(entity)} getFakePrimaryKey() {
    <#if primaryKey?node_name == "embedded-id">
      ${util.getPrimaryKeyType(entity)} id = null;
      <#assign pkColumns = primaryKey["h:properties/h:column"]>
        <#list pkColumns as pkColumn>
        final String ${pkColumn.@name}Value = ${util.java.getRandomValue(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))};
        </#list>

        id = new ${embeddedIdName}();
      <#list pkColumns as pkColumn>
      id.set${pkColumn.@name?cap_first}(${util.java.convertFromString(pkColumn.@type, pkColumn.@name+"Value")});
      </#list>
      return id;
    <#else>
      return ${util.java.getRandomValue(util.getPrimaryKeyType(entity), "1000")};
    </#if>
  }

  <#if util.xml.getAttribute(entity.@readOnly) == "" || util.xml.getAttribute(entity.@readOnly) == "false">
  @Override
  protected ${entity.@name} createEntity() {
    final ${entity.@name} entity = new ${entity.@name}();
	<#if primaryKey?node_name == "embedded-id">
		final ${embeddedIdName} primaryKey = getFakePrimaryKey();
		entity.setId(primaryKey);
	</#if>
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    entity.set${column.@name?cap_first}(${column.@name});
    </#list>

    return entity;
  }

  @Override
  protected void updateEntity(${entity.@name} entity) {
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    entity.set${column.@name?cap_first}(${column.@name});
    </#list>
  }

  @Override
  protected void assertEntity(${entity.@name} entity, ${entity.@name} entity2) {
    Assert.assertNotNull(entity);
    Assert.assertNotNull(entity2);

    <#list columns as column>
    Assert.assertEquals(entity.get${column.@name?cap_first}(), entity2.get${column.@name?cap_first}());
    </#list>
  }
  </#if>

  <@util.getValues doc=xml entity=entity property=primaryKey/>
  <#list columns as column>
  <@util.getValues doc=xml entity=entity property=column/>
  </#list>
  <#list manyToOnes as manyToOne>
  <@util.getValues doc=xml entity=entity property=manyToOne/>
  </#list>
  <#list oneToManys as oneToMany>
  <@util.getValues doc=xml entity=entity property=oneToMany/>
  </#list>
  <#list manyToManys as manyToMany>
  <@util.getValues doc=xml entity=entity property=manyToMany/>
  </#list>
}
