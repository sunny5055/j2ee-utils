<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />
<@resolveKey map=config key="filtersBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(filtersBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ filtersBeanName + ".java" />

<#if filtersBeanPackageName?? && filtersBeanPackageName?length gt 0>
package ${filtersBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<@addTo assignTo="imports" element="java.util.Map" />

<#list columns as column>
  	<@addTo assignTo="imports" element=util.getImportsFor(column, false) />
</#list>
<#if manyToOnes?size gt 0>
	<#list manyToOnes as manyToOne>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToOne, false) />
	</#list>
</#if>
<#if oneToManys?size gt 0>
	<#list oneToManys as oneToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(oneToMany, false) />
	</#list>
</#if>
<#if manyToManys?size gt 0>
	<#list manyToManys as manyToMany>
		<@addTo assignTo="imports" element=util.getImportsFor(manyToMany, false) />
	</#list>
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
</#if>

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.collection.MapUtil" />
<@addTo assignTo="imports" element="javax.faces.event.ActionEvent" />

${getImports(true, filtersBeanPackageName, imports)}


@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${filtersBeanName} extends AbstractFiltersBean {
<#list allProperties as property>
  <#if property?node_name = "column">
    <@util.java.getProperty visibility="private" type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#assign propertyName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#assign propertyName = property.@name>
	  </#if>
      <@util.java.getProperty visibility="private" type=util.getModelName(property.@targetEntity) name=propertyName />
  </#if>
</#list>

	public ${filtersBeanName}() {
		super();
	}

<#list allProperties as property>
  <#if property?node_name = "column">
    <@util.java.getter type=getType(property.@type) name=property.@name />

    <@util.java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  	<#assign propertyName = property.@name?substring(0, property.@name?length-1)>
	  <#else>
	  	<#assign propertyName = property.@name>
	  </#if>
      <@util.java.getter type=util.getModelName(property.@targetEntity) name=propertyName />

      <@util.java.setter type=util.getModelName(property.@targetEntity) name=propertyName />
  </#if>
</#list>


	@Override
	public boolean hasFilters() {
		return !MapUtil.isEmpty(getFilters());
	}

	@Override
	public Map<String, String> getFilters() {
		final Map<String, String> filters = new HashMap<String, String>();
		//TODO to complete
		<#list allProperties as property>
		<#if property?node_name = "column">
			<#assign propertyType = getType(property.@type)/>
		if(${util.java.checkNotNull(propertyType, property.@name)}) {
			<#if propertyType == "Double"  || propertyType == "double">
			filters.put("${property.@name}", ${property.@name}.toString());
			<#elseif propertyType == "Integer" || propertyType == "int"
			|| propertyType == "Byte" || propertyType == "byte"
			|| propertyType == "Short" || propertyType == "short">
			filters.put("${property.@name}", ${property.@name}.toString());
			<#elseif propertyType == "Long" || propertyType == "long">
			filters.put("${property.@name}", ${property.@name}.toString());
			<#elseif propertyType == "Date">
			filters.put("${property.@name}", ${property.@name}.toString());
			<#elseif propertyType == "Boolean" || propertyType == "boolean">
			filters.put("${property.@name}", ${property.@name}.toString());
			<#else>
			filters.put("${property.@name}", ${property.@name});
			</#if>
		}
		<#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
		<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  		<#assign propertyName = property.@name?substring(0, property.@name?length-1)>
	  	<#else>
	  		<#assign propertyName = property.@name>
	  	</#if>
		if(${util.java.checkNotNull(getType(property.@targetEntity), propertyName)}) {
			filters.put("${propertyName}", ${propertyName}.toString());
		}
		</#if>
		</#list>

		return filters;
	}

	@Override
	protected void reInit() {
		<#list allProperties as property>
		<#if property?node_name = "column">
		this.${property.@name} = null;
		<#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
		<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
	  		<#assign propertyName = property.@name?substring(0, property.@name?length-1)>
	  	<#else>
	  		<#assign propertyName = property.@name>
	  	</#if>
		this.${propertyName} = null;
		</#if>
		</#list>

		init();
	}

	@Override
	public void clearFilters(ActionEvent event) {
		reInit();
	}
}
</#list>
