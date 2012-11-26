<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign interface = xml["//p:interface[@name=$interfaceName]"]>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
public interface ${interfaceName}
<#assign interfaces = xml["//p:interface[@name=$interfaceName]//p:interface"]>
<#list interfaces as interface>
<#if interface_index == 0>
 extends
</#if>
${util.java.getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
<#assign properties = xml["//p:interface[@name=$interfaceName]//p:static-property"]>
<#list properties as property>
<@util.getInterfaceProperty property=property/>
</#list>

<#assign operations = xml["//p:interface[@name=$interfaceName]//p:operation"]>
<#list operations as operation>
  <@util.interfaceOperation operation=operation/>
</#list>

}
