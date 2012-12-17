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
	<#assign imports = imports + [ getFqdn(entity.@superClass) ] />
<#else>
	<#assign imports = imports + [ "com.googlecode.jutils.dal.dto.AbstractHibernateDto" ] />
</#if>
<#if interfaces?size gt 0>
	<#list interfaces as interface>
		<#assign imports = imports + [ getFqdn(interface) ] />
	</#list>
</#if>

<#assign imports = imports + [ "javax.persistence.Entity" ] />
<#assign imports = imports + [ "javax.persistence.Column" ] />

<#if primaryKey?node_name = "id">
	<#assign imports = imports + [ "javax.persistence.Id" ] />
	<#assign imports = imports + [ "javax.persistence.GeneratedValue" ] />
	<#assign imports = imports + [ "javax.persistence.GenerationType" ] />
<#else>
	<#assign imports = imports + [ "javax.persistence.EmbeddedId" ] />
</#if>

<#list columns as column>
  	<#assign imports = imports + util.getTypes(column) />
</#list>
<#if manyToOnes?size gt 0>
	<#assign imports = imports + [ "javax.persistence.ManyToOne" ] />
	<#list manyToOnes as manyToOne>
		<#assign imports = imports + util.getTypes(manyToOne) />
	</#list>
</#if>
<#if oneToManys?size gt 0>
	<#assign imports = imports + [ "javax.persistence.OneToMany" ] />
	<#list oneToManys as oneToMany>
		<#assign imports = imports + util.getTypes(oneToMany) />
	</#list>
</#if>
<#if manyToManys?size gt 0>
	<#assign imports = imports + [ "javax.persistence.ManyToMany" ] />
	<#assign imports = imports + [ "javax.persistence.JoinTable" ] />
	<#list manyToManys as manyToMany>
		<#assign imports = imports + util.getTypes(manyToMany) />
	</#list>
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<#assign imports = imports + [ "javax.persistence.CascadeType" ] />
	<#assign imports = imports + [ "javax.persistence.JoinColumn" ] />
	<#assign imports = imports + [ "javax.persistence.FetchType" ] />
</#if>
<#list constructors as constructor>
	<#assign imports = imports + util.getTypes(constructor) />
</#list>
<#list operations as operation>
  <#assign imports = imports + util.getTypes(operation) />
</#list>

${getImports(packageName, imports)}


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
