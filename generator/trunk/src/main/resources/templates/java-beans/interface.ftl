<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign interface = xml["//b:interface[@name=$interfaceName]"]>
<#assign interfaces = xml["//b:interface[@name=$interfaceName]//b:element"]>
<#assign properties = xml["//b:interface[@name=$interfaceName]//b:static-property"]>
<#assign operations = xml["//b:interface[@name=$interfaceName]//b:operation"]>

<#if packageName??>
package ${packageName};
</#if>


<#assign imports = [] />
<#if interfaces?size gt 0>
	<#list interfaces as int>
		<#assign imports = imports + [ getFqdn(int) ] />
	</#list>
</#if>
<#list properties as property>
  	<#assign imports = imports + util.getTypes(property) />
</#list>
<#list operations as operation>
  <#assign imports = imports + util.getTypes(operation) />
</#list>

${getImports(packageName, imports)}

<@compress single_line=true>
public interface ${interfaceName}
<#if interfaces?size gt 0>
 extends <@myList list=interfaces var="interface">${getClassName(interface)}</@myList>
</#if>
{</@compress>
<#list properties as property>
<@util.getInterfaceProperty property=property/>
</#list>

<#list operations as operation>
  <@util.interfaceOperation operation=operation/>
</#list>

}
