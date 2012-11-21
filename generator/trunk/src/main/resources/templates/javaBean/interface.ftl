<#import "common.ftl" as util>
<#assign interface = xml["//interface[@name=$interfaceName]"]>
<#if packageName??>
package ${packageName};
</#if>

import java.util.List;


<@compress single_line=true>
public interface ${interfaceName}
<#assign interfaces = xml["//interface[@name=$interfaceName]//interface"]>
<#list interfaces as interface>
<#if interface_index == 0>
 extends
</#if>
${util.java.getClassName(interface)}
<#if interface_has_next>, </#if>
</#list>
{</@compress>
<#assign properties = xml["//interface[@name=$interfaceName]//static-property"]>
<#list properties as property>
<@util.getInterfaceProperty property=property/>
</#list>

<#assign operations = xml["//interface[@name=$interfaceName]//operation"]>
<#list operations as operation>
  <@util.interfaceOperation operation=operation/>
</#list>

}
