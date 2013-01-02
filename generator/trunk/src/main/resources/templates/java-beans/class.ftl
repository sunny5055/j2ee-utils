<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign class = xml["//b:class[@name=$className]"]>
<#assign interfaces = xml["//b:class[@name=$className]//b:element"]>
<#assign properties = xml["//b:class[@name=$className]//b:property"]>
<#assign listProperties = xml["//b:class[@name=$className]//b:property-list"]>
<#assign mapProperties = xml["//b:class[@name=$className]//b:property-map"]>
<#assign constructors = xml["//b:class[@name=$className]//b:constructor"]>
<#assign operations = xml["//b:class[@name=$className]//b:operation"]>

<#if packageName??>
package ${packageName};
</#if>

<#assign imports = [] />
<#if util.xml.existAttribute(class.@superClass)>
	<@addTo assignTo="imports" element=getFqdn(class.@superClass) />
</#if>
<#list interfaces as interface>
	<@addTo assignTo="imports" element=getFqdn(interface) />
</#list>
<#list properties as property>
  	<@addTo assignTo="imports" element=util.getImportsFor(property) />
</#list>
<#list listProperties as property>
	<@addTo assignTo="imports" element=util.getImportsFor(property) />
</#list>
<#list mapProperties as property>
	<@addTo assignTo="imports" element=util.getImportsFor(property) />
</#list>
<#list constructors as constructor>
	<@addTo assignTo="imports" element=util.getImportsFor(constructor) />
</#list>
<#list operations as operation>
  <@addTo assignTo="imports" element=util.getImportsFor(operation) />
</#list>

${getImports(true, packageName, imports)}

<#if interfaces?size gt 0 && (interfaces?seq_contains("Serializable") || interfaces?seq_contains("java.io.Serializable"))>
@SuppressWarnings("serial")
</#if>
<@compress single_line=true>
public ${util.getModifiersFrom(class)} class ${className}
<#if util.xml.existAttribute(class.@superClass)>
 extends ${getClassName(class.@superClass)}
</#if>
<#if interfaces?size gt 0>
 implements <@myList list=interfaces var="interface">${getClassName(interface)}</@myList>
</#if>
{</@compress>
<#list properties as property>
<@util.getProperty property=property/>
</#list>
<#list listProperties as property>
<@util.getProperty property=property/>
</#list>
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

  <@util.addMethod property=property/>

  <@util.removeMethod property=property/>
</#list>

<#list mapProperties as property>
  <@util.getter property=property/>

  <@util.setter property=property/>

  <@util.addMethod property=property/>

  <@util.removeMethod property=property/>
</#list>

<#list operations as operation>
  <@util.operation operation=operation/>
</#list>
}
