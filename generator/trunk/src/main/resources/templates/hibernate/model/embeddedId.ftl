<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = primaryKey["h:properties/h:column"]>
<#if packageName??>
package ${packageName};
</#if>


<#assign imports = [] />
<#assign imports = imports + [ "java.io.Serializable" ] />
<#assign imports = imports + [ "javax.persistence.Embeddable" ] />

<#if columns?size gt 0>
	<#assign imports = imports + [ "javax.persistence.Column" ] />
	<#list columns as column>
  		<#assign imports = imports + util.getTypes(column) />
	</#list>
</#if>

${getImports(packageName, imports)}

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
