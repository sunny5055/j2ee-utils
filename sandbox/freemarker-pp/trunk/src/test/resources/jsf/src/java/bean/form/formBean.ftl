<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly) != "true">
<#include "/common/assign.inc" />

<@resolveKey map=config key="formBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(formBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ formBeanName + ".java" />

<#if formBeanPackageName?? && formBeanPackageName?length gt 0>
package ${formBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="javax.faces.context.FacesContext" />

<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<#if uniqueConstraints?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>


<#if uniqueColumns?size gt 0>
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>
<#if manyToOnes?size gt 0 || oneToManys?size gt 0 || manyToManys?size gt 0>
	<@addTo assignTo="imports" element="java.util.List" />
	<@addTo assignTo="imports" element=" com.googlecode.jutils.StringUtil" />
</#if>

${getImports(true, formBeanPackageName, imports)}

@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${formBeanName} extends AbstractFormBean<${primaryKeyType}, ${modelName}, ${serviceName}> {
    @Autowired
    private ${serviceName} service;

    public ${formBeanName}() {
        super();
    }

    @Override
    public ${serviceName} getService() {
        return service;
    }

    @Override
    public ${primaryKeyType} getPrimaryKey() {
    	${primaryKeyType} pk = null;
    	//TODO to complete
    	return pk;
    }

    @Override
    protected ${modelName} getNewInstance() {
        return new ${modelName}();
    }

    @Override
    protected String getListPage() {
        return "${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}";
    }

    @Override
    protected String getViewPage() {
        return null;
    }

    @Override
    protected boolean prepareUpdate() {
        boolean update = true;
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        return update;
    }
}
</#if>
</#list>