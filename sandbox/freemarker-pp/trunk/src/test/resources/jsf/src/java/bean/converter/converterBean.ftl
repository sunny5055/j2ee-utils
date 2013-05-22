<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign lowerEntityName = entityName?uncap_first />
<#assign servicePackageName = util.getServicePackageName(entityPackageName) />
<#assign serviceName = util.getServiceName(entity.@name) />
<#assign converterBeanPackageName = util.getConverterBeanPackageName(entityPackageName) />
<#assign converterBeanName = util.getConverterBeanName(entity.@name) />

<@resolveKey map=config key="converterBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(converterBeanPackageName) />

<@changeOutputFile name=filePath + "/"+ converterBeanName + ".java" />
<#assign interfaces = entity["./j:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign primaryKeyType = util.getPrimaryKeyType(entity) />
<#assign columns = entity["./j:properties/j:column"]>
<#assign manyToOnes = entity["./j:properties/j:many-to-one"]>
<#assign oneToManys = entity["./j:properties/j:one-to-many"]>
<#assign manyToManys = entity["./j:properties/j:many-to-many"]>
<#assign uniqueColumns = entity["./j:properties/j:column[@unique='true']"] />
<#assign constructors = entity["./j:constructor"]>
<#assign operations = entity["./j:operation"]>
<#if converterBeanPackageName?? && converterBeanPackageName?length gt 0>
package ${converterBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="javax.faces.convert.FacesConverter" />

<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(entityPackageName)}.${primaryKeyType}" />
</#if>
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />

${getImports(false, converterBeanPackageName, imports)}


@FacesConverter(forClass = ${entityName}.class)
public class ${converterBeanName} extends AbstractConverter<${primaryKeyType}, ${entityName}, ${serviceName}> {

	@Override
    public Class<${serviceName}> getServiceClass() {
        return ${serviceName}.class;
    }

    @Override
    protected ${primaryKeyType} getPrimaryKey(String value) {
        ${primaryKeyType} primaryKey = null;
        if (!StringUtil.isBlank(value)) {
	    	<#if primaryKey?node_name == "id">
	        primaryKey = ${util.java.convertFromString(primaryKeyType, "value")};
			<#else>
			//TODO need to be completed
			</#if>
        }
        return primaryKey;
    }
}
</#list>