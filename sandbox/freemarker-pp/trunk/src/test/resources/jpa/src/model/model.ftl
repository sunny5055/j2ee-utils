<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "includes/model.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="modelFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(modelPackageName) />

<@changeOutputFile name=filePath + "/"+ modelName + ".java" />

<#if modelPackageName?? && modelPackageName?length gt 0>
package ${modelPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="java.io.Serializable" />

<#list columns as column>
  	<@addTo assignTo="imports" element=util.getImportsFor(column) />
</#list>
<#if manyToOnes?size gt 0>
	<#list manyToOnes as manyToOne>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToOne) />
	</#list>
</#if>
<#if oneToManys?size gt 0>
	<#list oneToManys as oneToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(oneToMany) />
	</#list>
</#if>
<#if manyToManys?size gt 0>
	<#list manyToManys as manyToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToMany) />
	</#list>
</#if>

${getImports(true, modelPackageName, imports)}


@SuppressWarnings("serial")
<#compress>
public ${util.getModifiersFrom(entity)} class ${modelName} implements Serializable
{</#compress>
<@util.getProperty property=primaryKey/>
<#list columns as column>
<@util.getProperty property=column/>
</#list>
<#list manyToOnes as manyToOne>
<@util.getProperty property=manyToOne/>
</#list>
<#list oneToManys as oneToMany>
<@util.getProperty property=oneToMany/>
</#list>
<#list manyToManys as manyToMany>
<@util.getProperty property=manyToMany/>
</#list>

	public ${modelName}() {
		super();
		<#list oneToManys as oneToMany>
			<@util.initProperties property=oneToMany/>
		</#list>
		<#list manyToManys as manyToMany>
			<@util.initProperties property=manyToMany/>
		</#list>
  	}

<@util.getter property=primaryKey/>

<@util.setter property=primaryKey/>

<#list columns as column>
  <@util.getter property=column/>

  <@util.setter property=column/>
</#list>

<#list manyToOnes as manyToOne>
  <@util.getter property=manyToOne/>

  <@util.setter property=manyToOne/>
</#list>

<#list oneToManys as oneToMany>
  <@util.getter property=oneToMany/>

  <@util.setter property=oneToMany/>
</#list>

<#list manyToManys as manyToMany>
  <@util.getter property=manyToMany/>

  <@util.setter property=manyToMany/>
</#list>
}
</#list>
