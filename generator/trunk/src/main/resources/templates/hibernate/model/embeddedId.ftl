<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//p:entity[@name=$className]"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#if packageName??>
package ${packageName};
</#if>


@Embeddable
@SuppressWarnings("serial")
public class ${util.getPrimaryKeyType(entity)} implements Serializable {
<#assign columns = primaryKey["p:properties/p:column"]>
<#list columns as column>
${util.getHibernateAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>

	public ${util.getPrimaryKeyType(entity)}() {
		super();
  	}

<#list columns as column>
  <@util.getter property=column/>

  <@util.setter property=column/>
</#list>
}
