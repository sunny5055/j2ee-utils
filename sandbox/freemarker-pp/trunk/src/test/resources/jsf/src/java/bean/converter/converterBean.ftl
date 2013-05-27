<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />
<@resolveKey map=config key="converterBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(converterBeanPackageName) />

<@changeOutputFile name=filePath + "/"+ converterBeanName + ".java" />

<#if converterBeanPackageName?? && converterBeanPackageName?length gt 0>
package ${converterBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="javax.faces.convert.FacesConverter" />

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />

${getImports(false, converterBeanPackageName, imports)}


@FacesConverter(forClass = ${modelName}.class)
public class ${converterBeanName} extends AbstractConverter<${primaryKeyType}, ${modelName}, ${serviceName}> {

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