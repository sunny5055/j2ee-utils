<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly, "false") != "true">
<#include "/common/assign.inc" />

<@resolveKey map=config key="createFormBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(createFormBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ createFormBeanName + ".java" />

<#if createFormBeanPackageName?? && createFormBeanPackageName?length gt 0>
package ${createFormBeanPackageName};
</#if>

<#assign imports = [] />
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

${getImports(true, createFormBeanPackageName, imports)}

@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${createFormBeanName} extends AbstractCreateFormBean<${primaryKeyType}, ${modelName}, ${serviceName}> {
    @Autowired
    private ${serviceName} service;

    public ${createFormBeanName}() {
        super();
    }

    @Override
    public ${serviceName} getService() {
        return service;
    }

    @Override
    protected ${modelName} getNewModelInstance() {
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
    protected boolean prepareCreate() {
        boolean create = true;

        return create;
    }
}
</#if>
</#list>