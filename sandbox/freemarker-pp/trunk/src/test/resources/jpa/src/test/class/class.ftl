<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/test-class.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="testServiceFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(servicePackageName) />

<@changeOutputFile name=filePath + "/"+ testServiceName + ".java" />

<#if servicePackageName?? && servicePackageName?length gt 0>
package ${servicePackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.test.context.ContextConfiguration" />
<@addTo assignTo="imports" element="org.junit.Test" />
<@addTo assignTo="imports" element="com.github.springtestdbunit.annotation.DatabaseSetup" />
<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />

<#if util.getPrimaryKeyType(entity) == "String">
	<@addTo assignTo="imports" element="org.apache.commons.lang3.RandomStringUtils" />
<#else>
	<@addTo assignTo="imports" element="java.util.Random" />
</#if>

<#list columns as column>
<#if column.@type == "String">
	<@addTo assignTo="imports" element="org.apache.commons.lang3.RandomStringUtils" />
<#else>
	<@addTo assignTo="imports" element="java.util.Random" />
</#if>
</#list>

<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
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
<#if util.xml.getAttribute(entity.@readOnly, "false") == "true">
AbstractGenericReadServiceTest<${primaryKeyType}, ${modelName}, ${entityName}, ${serviceName}>
<#else>
AbstractGenericServiceTest<${primaryKeyType}, ${modelName}, ${entityName}, ${serviceName}>
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
  <@util.getTestMethodForConstraints doc=xml entity=entity />

  @Override
  protected ${primaryKeyType} getPrimaryKey(Node node) {
    ${primaryKeyType} id = null;
    if (node != null) {
      final String stringValue = node.valueOf("@${util.getColumnName(entity.@columnPrefix, primaryKey)}");
      if (!StringUtil.isBlank(stringValue)) {
        id = ${util.java.convertFromString(util.getPrimaryKeyType(entity), "stringValue")};
      }
    }
    return id;
  }

  @Override
  protected ${primaryKeyType} getFakePrimaryKey() {
      return ${util.java.getRandomValue(util.getPrimaryKeyType(entity), "1000")};
  }

  <#if util.xml.getAttribute(entity.@readOnly, "false") == "" || util.xml.getAttribute(entity.@readOnly, "false") == "false">
  @Override
  protected ${modelName} createDto() {
    final ${modelName} dto = new ${modelName}();
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    dto.set${column.@name?cap_first}(${column.@name});
    </#list>

    return dto;
  }

  @Override
  protected void updateDto(${modelName} dto) {
    <#list columns as column>
    final ${getType(column.@type)} ${column.@name} = ${util.java.getRandomValue(getType(column.@type), util.xml.getAttribute(column.@length))};
    dto.set${column.@name?cap_first}(${column.@name});
    </#list>
  }

  @Override
  protected void assertDto(${modelName} dto, ${modelName} dto2) {
    Assert.assertNotNull(dto);
    Assert.assertNotNull(dto2);

    <#list columns as column>
    Assert.assertEquals(dto.get${column.@name?cap_first}(), dto2.get${column.@name?cap_first}());
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
  <@util.getValuesForConstraints doc=xml entity=entity />
}
</#list>
