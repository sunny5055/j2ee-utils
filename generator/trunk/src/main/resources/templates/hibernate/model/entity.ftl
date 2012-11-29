<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//p:entity[@name=$className]"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
@SuppressWarnings("serial")
public ${util.getModifiers(entity)} class ${className}
<#if util.xml.existAttribute(entity.@superClass)>
 extends ${util.java.getClassName(entity.@superClass)}
<#else>
 extends AbstractHibernateDto<${util.getPrimaryKeyType(entity)}>
</#if>
<#assign interfaces = xml["//p:entity[@name=$className]//p:interface"]>
<#list interfaces as interface>
<#if interface_index == 0>
 implements
</#if>
${util.java.getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
${util.getHibernateAnnotation(entity, primaryKey)}
<@util.getProperty property=primaryKey/>
<#assign columns = xml["//p:entity[@name=$className]/p:properties/p:column"]>
<#list columns as column>
${util.getHibernateAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>
<#assign manyToOnes = xml["//p:entity[@name=$className]/p:properties/p:many-to-one"]>
<#list manyToOnes as manyToOne>
${util.getHibernateAnnotation(entity, manyToOne)}
<@util.getProperty property=manyToOne/>
</#list>
<#assign oneToManys = xml["//p:entity[@name=$className]/p:properties/p:one-to-many"]>
<#list oneToManys as oneToMany>
${util.getHibernateAnnotation(entity, oneToMany)}
<@util.getProperty property=oneToMany/>
</#list>
<#assign manyToManys = xml["//p:entity[@name=$className]/p:properties/p:many-to-many"]>
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

<#assign constructors = xml["//p:entity[@name=$className]//p:constructor"]>
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

<#assign operations = xml["//p:entity[@name=$className]//p:operation"]>
<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
