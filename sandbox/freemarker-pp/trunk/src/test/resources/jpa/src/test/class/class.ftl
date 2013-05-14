<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/test-class.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:projectName"]/>

<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign servicePackageName = util.getServicePackageName(entityPackageName) />
<#assign serviceName = util.getServiceName(entity.@name) />
<#assign serviceImplPackageName = util.getServiceImplPackageName(entityPackageName) />
<#assign serviceImplName = util.getServiceImplName(entity.@name) />
<#assign testServiceName = util.getTestServiceName(entity.@name) />
<@resolveKey map=config key="testXmlDatasetFileName" value=entityName assignTo="testXmlDatasetFileName"/>

<@resolveKey map=config key="testServiceFilePath" value=projectName assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(servicePackageName) />

<@changeOutputFile name=filePath + "/"+ testServiceName + ".java" />

<#assign interfaces = entity["./j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
<#assign columns = entity["./j:properties/j:column"]>
<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
<#assign manyToManys = entity["./j:properties/j:many-to-many"]>

<#if servicePackageName?? && servicePackageName?length gt 0>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.test.context.ContextConfiguration" />
<@addTo assignTo="imports" element="org.junit.Test" />
<@addTo assignTo="imports" element="com.github.springtestdbunit.annotation.DatabaseSetup" />
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(entityPackageName)}.${primaryKeyType}" />
    <#assign pkColumns = primaryKey["j:properties/j:column"]>
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

<@addTo assignTo="imports" element="org.junit.Assert" />

<@addTo assignTo="imports" element="org.dom4j.Node" />
<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.collection.CollectionUtil" />
<@addTo assignTo="imports" element="java.util.List" />


${getImports(true, servicePackageName, imports)}

@ContextConfiguration(locations = { "classpath:${util.getClassPathResource(config.springTestBusinessFilePath, config.springTestBusinessFileName)}" })
@DatabaseSetup(value = "classpath:${util.getClassPathResource(config.testXmlDatasetFilePath, testXmlDatasetFileName)}")
<#compress>
public class ${testServiceName} extends
<#if util.xml.getAttribute(entity.@readOnly) == "true">
AbstractGenericReadServiceTest<${primaryKeyType}, ${entityName}, ${serviceName}>
<#else>
AbstractGenericServiceTest<${primaryKeyType}, ${entityName}, ${serviceName}>
</#if>
 {
</#compress>
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

  <@util.getTestMethod doc=xml entity=entity property=primaryKey/>
  <#list columns as column>
  <@util.getTestMethod doc=xml entity=entity property=column/>
  </#list>
  <#list manyToOnes as manyToOne>
  <@util.getTestMethod doc=xml entity=entity property=manyToOne/>
  </#list>
  <#list oneToManys as oneToMany>
  <@util.getTestMethod doc=xml entity=entity property=oneToMany/>
  </#list>
  <#list manyToManys as manyToMany>
  <@util.getTestMethod doc=xml entity=entity property=manyToMany/>
  </#list>

  @Override
  protected ${primaryKeyType} getPrimaryKey(Node node) {
    ${primaryKeyType} id = null;
    if (node != null) {
      <#if primaryKey?node_name == "embedded-id">
      <#assign pkColumns = primaryKey["j:properties/j:column"]>
        <#list pkColumns as pkColumn>
      final String ${pkColumn.@name}Value = node.valueOf("@${util.getColumnName(entity.@columnPrefix, pkColumn)}");
      </#list>

      id = new ${primaryKeyType}();
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
  protected ${primaryKeyType} getFakePrimaryKey() {
    <#if primaryKey?node_name == "embedded-id">
      ${primaryKeyType} id = null;
      <#assign pkColumns = primaryKey["j:properties/j:column"]>
        <#list pkColumns as pkColumn>
        final String ${pkColumn.@name}Value = ${util.java.getRandomValue(getType(pkColumn.@type), util.xml.getAttribute(pkColumn.@length))};
        </#list>

        id = new ${primaryKeyType}();
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
  protected ${entityName} createEntity() {
    final ${entityName} entity = new ${entityName}();
	<#if primaryKey?node_name == "embedded-id">
		final ${primaryKeyType} primaryKey = getFakePrimaryKey();
		entity.setId(primaryKey);
	</#if>
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    entity.set${column.@name?cap_first}(${column.@name});
    </#list>

    return entity;
  }

  @Override
  protected void updateEntity(${entityName} entity) {
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    entity.set${column.@name?cap_first}(${column.@name});
    </#list>
  }

  @Override
  protected void assertEntity(${entityName} entity, ${entityName} entity2) {
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
</#list>
