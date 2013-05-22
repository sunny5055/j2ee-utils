<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/model.inc" as util>
<#import "includes/queries/queries.inc" as queries>
<#import "includes/jpa/annotation.inc" as jpa>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="entityFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(entityPackageName) />

<@changeOutputFile name=filePath + "/"+ entityName + ".java" />

<#if entityPackageName?? && entityPackageName?length gt 0>
package ${entityPackageName};
</#if>

<#assign imports = [] />
<#if util.xml.existAttribute(entity.@superClass)>
	<@addTo assignTo="imports" element=getFqdn(entity.@superClass) />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dto.AbstractDto" />
</#if>
<#list interfaces as interface>
	<@addTo assignTo="imports" element=getFqdn(interface) />
</#list>

<@addTo assignTo="imports" element="javax.persistence.Entity" />
<@addTo assignTo="imports" element="javax.persistence.Table" />
<@addTo assignTo="imports" element="javax.persistence.Column" />

<#if primaryKey?node_name = "id">
	<@addTo assignTo="imports" element="javax.persistence.Id" />
	<@addTo assignTo="imports" element="javax.persistence.GeneratedValue" />
	<@addTo assignTo="imports" element="javax.persistence.GenerationType" />
<#else>
	<@addTo assignTo="imports" element="javax.persistence.EmbeddedId" />
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(entityPackageName)}.${primaryKeyType}" />
</#if>

<#list columns as column>
  	<@addTo assignTo="imports" element=util.getImportsFor(column) />
</#list>
<#if manyToOnes?size gt 0>
	<@addTo assignTo="imports" element="javax.persistence.ManyToOne" />
	<#list manyToOnes as manyToOne>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToOne) />
	</#list>
</#if>
<#if oneToManys?size gt 0>
	<@addTo assignTo="imports" element="javax.persistence.OneToMany" />
	<#list oneToManys as oneToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(oneToMany) />
	</#list>
</#if>
<#if manyToManys?size gt 0>
	<@addTo assignTo="imports" element="javax.persistence.ManyToMany" />
	<@addTo assignTo="imports" element="javax.persistence.JoinTable" />
	<#list manyToManys as manyToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToMany) />
	</#list>
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="javax.persistence.CascadeType" />
	<@addTo assignTo="imports" element="javax.persistence.JoinColumn" />
	<@addTo assignTo="imports" element="javax.persistence.FetchType" />
</#if>
<#list constructors as constructor>
	<@addTo assignTo="imports" element=util.getImportsFor(constructor) />
</#list>
<#list operations as operation>
  <@addTo assignTo="imports" element=util.getImportsFor(operation) />
</#list>

<#if queries.hasNamedQuery(xml, entity) == "true">
	<@addTo assignTo="imports" element="javax.persistence.NamedQueries" />
	<@addTo assignTo="imports" element="javax.persistence.NamedQuery" />
</#if>

${getImports(true, entityPackageName, imports)}


@Entity
@Table(name="${entity.@tableName}")
<@queries.getNamedQueries doc=xml entity=entity/>
@SuppressWarnings("serial")
<#compress>
public ${util.getModifiersFrom(entity)} class ${entityName}
<#if util.xml.existAttribute(entity.@superClass)>
 extends ${getClassName(entity.@superClass)}
<#else>
 extends AbstractDto<${primaryKeyType}>
</#if>
<#if interfaces?size gt 0>
 implements <@myList list=interfaces var="interface">${getClassName(interface)}</@myList>
</#if>
{</#compress>
${jpa.getJpaAnnotation(entity, primaryKey)}
<@util.getProperty property=primaryKey/>
<#list columns as column>
${jpa.getJpaAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>
<#list manyToOnes as manyToOne>
${jpa.getJpaAnnotation(entity, manyToOne)}
<@util.getProperty property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
${jpa.getJpaAnnotation(entity, oneToMany)}
<@util.getProperty property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
${jpa.getJpaAnnotation(entity, manyToMany)}
<@util.getProperty property=manyToMany/>
</#list>

	public ${entityName}() {
		super();
		<#if primaryKey?node_name = "embedded-id">
		this.${primaryKey.@name} = new ${primaryKeyType}();
		<#else>
		</#if>
		<#list oneToManys as oneToMany>
			<@util.initProperties property=oneToMany/>
		</#list>
		<#list manyToManys as manyToMany>
			<@util.initProperties property=manyToMany/>
		</#list>
  	}

<#list constructors as constructor>
	<@util.constructor className=entityName constructor=constructor/>
</#list>

<@util.getterPrimaryKey property=primaryKey/>

<@util.setterPrimaryKey property=primaryKey/>

<@util.getter property=primaryKey/>

<@util.setter property=primaryKey/>

<#list columns as column>
  <@util.getter property=column/>

  <@util.setter property=column/>
</#list>

<#list manyToOnes as manyToOne>
  <@util.getter property=manyToOne/>

  <@util.setter property=manyToOne/>
</#list>

<#list oneToManys as oneToMany>
  <@util.getter property=oneToMany/>

  <@util.setter property=oneToMany/>
</#list>

<#list manyToManys as manyToMany>
  <@util.getter property=manyToMany/>

  <@util.setter property=manyToMany/>
</#list>
}
</#list>
