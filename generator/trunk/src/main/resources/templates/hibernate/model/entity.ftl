<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>
<#assign constructors = xml["//h:entity[@name=$className]//h:constructor"]>
<#assign operations = xml["//h:entity[@name=$className]//h:operation"]>

<#if packageName??>
package ${packageName};
</#if>

<#assign imports = [] />
<#if util.xml.existAttribute(entity.@superClass)>
	<@addTo assignTo="imports" element=getFqdn(entity.@superClass) />
<#else>
	<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dto.AbstractHibernateDto" />
</#if>
<#list interfaces as interface>
	<@addTo assignTo="imports" element=getFqdn(interface) />
</#list>

<@addTo assignTo="imports" element="javax.persistence.Entity" />
<@addTo assignTo="imports" element="javax.persistence.Column" />

<#if primaryKey?node_name = "id">
	<@addTo assignTo="imports" element="javax.persistence.Id" />
	<@addTo assignTo="imports" element="javax.persistence.GeneratedValue" />
	<@addTo assignTo="imports" element="javax.persistence.GenerationType" />
<#else>
	<@addTo assignTo="imports" element="javax.persistence.EmbeddedId" />
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

${getImports(false, packageName, imports)}


<@compress single_line=true>
@Entity(name="${entity.@tableName}")
@SuppressWarnings("serial")
public ${util.getModifiersFrom(entity)} class ${className}
<#if util.xml.existAttribute(entity.@superClass)>
 extends ${getClassName(entity.@superClass)}
<#else>
 extends AbstractHibernateDto<${util.getPrimaryKeyType(entity)}>
</#if>
<#if interfaces?size gt 0>
 implements <@myList list=interfaces var="interface">${getClassName(interface)}</@myList>
</#if>
{</@compress>
${util.getHibernateAnnotation(entity, primaryKey)}
<@util.getProperty property=primaryKey/>
<#list columns as column>
${util.getHibernateAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>
<#list manyToOnes as manyToOne>
${util.getHibernateAnnotation(entity, manyToOne)}
<@util.getProperty property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
${util.getHibernateAnnotation(entity, oneToMany)}
<@util.getProperty property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
${util.getHibernateAnnotation(entity, manyToMany)}
<@util.getProperty property=manyToMany/>
</#list>

	public ${className}() {
		super();
		<#list oneToManys as oneToMany>
			<@util.initProperties property=oneToMany/>
		</#list>
		<#list manyToManys as manyToMany>
			<@util.initProperties property=manyToMany/>
		</#list>
  	}

<#list constructors as constructor>
	<@util.constructor className=className constructor=constructor/>
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

  <@util.add property=oneToMany/>

  <@util.remove property=oneToMany/>
</#list>

<#list manyToManys as manyToMany>
  <@util.getter property=manyToMany/>

  <@util.setter property=manyToMany/>

  <@util.add property=manyToMany/>

  <@util.remove property=manyToMany/>
</#list>

<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
