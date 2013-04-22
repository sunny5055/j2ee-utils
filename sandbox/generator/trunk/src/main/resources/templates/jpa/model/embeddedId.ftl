<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "common.ftl" as util>
<#import "jpa/annotation.ftl" as jpa>

<#assign entity = xml["//j:entity[@name=$className]"]>
<#assign embeddedId = xml["//j:entity[@name=$className]/j:embedded-id"]>
<#assign columns = embeddedId["j:properties/j:column"]>

<#assign embeddedIdPackageName = util.getEmbeddedIdPackageName(packageName) />
<#assign embeddedIdName = util.getEmbeddedIdName(embeddedId.@targetEntity) />

<#if embeddedIdPackageName?? && embeddedIdPackageName?length gt 0>
package ${embeddedIdPackageName};
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

${getImports(true, embeddedIdPackageName, imports)}

@Embeddable
@SuppressWarnings("serial")
public class ${embeddedIdName} implements Serializable {
<#list columns as column>
${jpa.getJpaAnnotation(entity, column)}
<@util.getProperty property=column/>
</#list>

	public ${embeddedIdName}() {
		super();
  	}

	public ${embeddedIdName}(${util.getParametersDeclaration(columns)}) {
		this();
		<#list columns as column>
			<@util.java.assign name=column.@name value=column.@name />
		</#list>
  	}

<#list columns as column>
  <@util.getter property=column/>

  <@util.setter property=column/>
</#list>
}
