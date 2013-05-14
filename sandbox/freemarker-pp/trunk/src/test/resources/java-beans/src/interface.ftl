<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "includes/common.inc" as util>

<@dropOutputFile />

<#assign interfaces = xml["//b:interface"]/>
<#list interfaces as interface>
<#assign packageName = interface["ancestor::p:package/@name"]>
<@format format=config.interfaceFileName value=interface.@name assignTo="fileName"/>
<#assign interfaceName = fileName?replace(".java", "")>
<@changeOutputFile name=config.javaPath + "/" + packageToDir(packageName) + "/"+ fileName />

<#assign implements = interface["./b:element"]>
<#assign properties = interface["./b:static-property"]>
<#assign operations = interface["./b:operation"]>

<#if packageName?? && packageName?length gt 0>
package ${packageName};
</#if>


<#assign imports = [] />
<#list implements as int>
	<@addTo assignTo="imports" element=getFqdn(int) />
</#list>
<#list properties as property>
  	<@addTo assignTo="imports" element=util.getImportsFor(property) />
</#list>
<#list operations as operation>
  <@addTo assignTo="imports" element=util.getImportsFor(operation) />
</#list>

${getImports(false, packageName, imports)}

<#compress>
public interface ${interfaceName}
<#if implements?size gt 0>
 extends <@myList list=implements var="int">${getClassName(int)}</@myList>
</#if>
{</#compress>
<#list properties as property>
<@util.getInterfaceProperty property=property/>
</#list>

<#list operations as operation>
  <@util.interfaceOperation operation=operation/>
</#list>

}
</#list>