<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign class = xml["//p:class[@name=$className]"]>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
public ${util.getModifiers(class)} class ${className}
<#if util.xml.existAttribute(class.@superClass)>
 extends ${util.java.getClassName(class.@superClass)}
</#if>
<#assign interfaces = xml["//p:class[@name=$className]//p:interface"]>
<#list interfaces as interface>
<#if interface_index == 0>
 implements
</#if>
${util.java.getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
<#assign properties = xml["//p:class[@name=$className]//p:property"]>
<#list properties as property>
<@util.getProperty property=property/>
</#list>
<#assign listProperties = xml["//p:class[@name=$className]//p:property-list"]>
<#list listProperties as property>
<@util.getProperty property=property/>
</#list>
<#assign mapProperties = xml["//p:class[@name=$className]//p:property-map"]>
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

<#assign constructors = xml["//p:class[@name=$className]//p:constructor"]>
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

<#assign operations = xml["//p:class[@name=$className]//p:operation"]>
<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
