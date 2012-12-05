<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign class = xml["//b:class[@name=$className]"]>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
public ${util.getModifiers(class)} class ${className}
<#if util.xml.existAttribute(class.@superClass)>
 extends ${util.java.getClassName(class.@superClass)}
</#if>
<#assign interfaces = xml["//b:class[@name=$className]//b:element"]>
<#list interfaces as interface>
<#if interface_index == 0>
 implements
</#if>
${util.java.getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
<#assign properties = xml["//b:class[@name=$className]//b:property"]>
<#list properties as property>
<@util.getProperty property=property/>
</#list>
<#assign listProperties = xml["//b:class[@name=$className]//b:property-list"]>
<#list listProperties as property>
<@util.getProperty property=property/>
</#list>
<#assign mapProperties = xml["//b:class[@name=$className]//b:property-map"]>
<#list mapProperties as property>
<@util.getProperty property=property/>
</#list>

	public ${className}() {
		super();
		<#list listProperties as property>
			<@util.initProperties property=property/>
		</#list>
		<#list mapProperties as property>
			<@util.initProperties property=property/>
		</#list>
  	}

<#assign constructors = xml["//b:class[@name=$className]//b:constructor"]>
<#list constructors as constructor>
	<@util.constructor className=className constructor=constructor/>
</#list>

<#list properties as property>
  <@util.getter property=property/>

  <@util.setter property=property/>
</#list>

<#list listProperties as property>
  <@util.getter property=property/>

  <@util.setter property=property/>

  <@util.add property=property/>

  <@util.remove property=property/>
</#list>

<#list mapProperties as property>
  <@util.getter property=property/>

  <@util.setter property=property/>

  <@util.add property=property/>

  <@util.remove property=property/>
</#list>

<#assign operations = xml["//b:class[@name=$className]//b:operation"]>
<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
