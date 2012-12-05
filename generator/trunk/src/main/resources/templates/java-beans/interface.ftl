<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "common.ftl" as util>
<#assign interface = xml["//b:interface[@name=$interfaceName]"]>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
public interface ${interfaceName}
<#assign interfaces = xml["//b:interface[@name=$interfaceName]//b:element"]>
<#list interfaces as interface>
<#if interface_index == 0>
 extends
</#if>
${getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
<#assign properties = xml["//b:interface[@name=$interfaceName]//b:static-property"]>
<#list properties as property>
<@util.getInterfaceProperty property=property/>
</#list>

<#assign operations = xml["//b:interface[@name=$interfaceName]//b:operation"]>
<#list operations as operation>
  <@util.interfaceOperation operation=operation/>
</#list>

}
