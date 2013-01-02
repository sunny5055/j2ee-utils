<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = primaryKey["h:properties/h:column"]>
<#if packageName??>
package ${packageName};
</#if>


<#assign imports = [] />
<@addTo assignTo="imports" element="java.io.Serializable" />
<@addTo assignTo="imports" element="javax.persistence.Embeddable" />

<#if columns?size gt 0>
	<@addTo assignTo="imports" element="javax.persistence.Column" />
	<#list columns as column>
  		<@addTo assignTo="imports" element=util.getImportsFor(column) />
	</#list>
</#if>

${getImports(true, packageName, imports)}

@Embeddable
@SuppressWarnings("serial")
public class ${util.getPrimaryKeyType(entity)} implements Serializable {
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
