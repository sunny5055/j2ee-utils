<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"} />
<#import "includes/common.inc" as util/>

<@dropOutputFile />

<#assign classes = xml["//b:class"]/>
<#list classes as class>
<#assign packageName = class["ancestor::p:package/@name"]>
<@format format=config.classFileName value=class.@name assignTo="fileName"/>
<#assign className = fileName?replace(".java", "")>
<@changeOutputFile name=config.javaPath + "/" + packageToDir(packageName) + "/"+ fileName />

<#assign interfaces = class["./b:element"]>
<#assign properties = class["./b:property"]>
<#assign listProperties = class["./b:property-list"]>
<#assign mapProperties = class["./b:property-map"]>
<#assign constructors = class["./b:constructor"]>
<#assign operations = class["./b:operation"]>


<#if packageName?? && packageName?length gt 0>
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
<#compress>
public ${util.getModifiersFrom(class)} class ${className}
<#if util.xml.existAttribute(class.@superClass)>
 extends ${getClassName(class.@superClass)}
</#if>
<#if interfaces?size gt 0>
 implements <@myList list=interfaces var="interface">${getClassName(interface)}</@myList>
</#if>
{</#compress>
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
</#list>
