<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
@SuppressWarnings("serial")
public ${util.getModifiersFrom(entity)} class ${className}
<#if util.xml.existAttribute(entity.@superClass)>
 extends ${getClassName(entity.@superClass)}
<#else>
 extends AbstractHibernateDto<${util.getPrimaryKeyType(entity)}>
</#if>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#list interfaces as interface>
<#if interface_index == 0>
 implements
</#if>
${getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
${util.getHibernateAnnotation(entity, primaryKey)}
<@util.getProperty property=primaryKey/>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#list columns as column>
${util.getHibernateAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#list manyToOnes as manyToOne>
${util.getHibernateAnnotation(entity, manyToOne)}
<@util.getProperty property=manyToOne/>
</#list>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#list oneToManys as oneToMany>
${util.getHibernateAnnotation(entity, oneToMany)}
<@util.getProperty property=oneToMany/>
</#list>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>
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

<#assign constructors = xml["//h:entity[@name=$className]//h:constructor"]>
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

<#assign operations = xml["//h:entity[@name=$className]//h:operation"]>
<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
