<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />

<@resolveKey map=config key="listBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(listBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ listBeanName + ".java" />

<#if listBeanPackageName?? && listBeanPackageName?length gt 0>
package ${listBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<@addTo assignTo="imports" element="javax.faces.model.SelectItem" />

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

${getImports(true, listBeanPackageName, imports)}

@Controller
@Scope("session")
@SuppressWarnings("serial")
public class ${listBeanName} extends AbstractListBean<${primaryKeyType}, ${modelName}, ${serviceName}> {
    @Autowired
    private ${serviceName} service;

    public ${listBeanName}() {
        super();
    }

    @Override
    protected ${serviceName} getService() {
        return service;
    }

    @Override
    protected SelectItem toSelectItem(${modelName} ${lowerModelName}) {
        return new SelectItem(${lowerModelName}.getPrimaryKey(), ${lowerModelName}.toString());
    }
}
</#list>