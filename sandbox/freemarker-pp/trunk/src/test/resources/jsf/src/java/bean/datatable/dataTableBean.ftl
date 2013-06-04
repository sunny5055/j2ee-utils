<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#include "/common/assign.inc" />
<@resolveKey map=config key="dataTableBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(dataTableBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ dataTableBeanName + ".java" />

<#if dataTableBeanPackageName?? && dataTableBeanPackageName?length gt 0>
package ${dataTableBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
<@addTo assignTo="imports" element="javax.faces.event.ActionEvent" />
</#if>

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />
<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.web.util.FacesUtils"/>
</#if>
<@addTo assignTo="imports" element="${dataModelBeanPackageName}.${dataModelBeanName}" />

${getImports(false, dataTableBeanPackageName, imports)}


@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${dataTableBeanName} extends AbstractSingleRowSelectionDataTableBean<${primaryKeyType}, ${modelName}> {
   	@Autowired
    private ${serviceName} service;

	@Autowired
    private  ${dataModelBeanName} dataModel;

    public ${dataTableBeanName}() {
        super();
    }

    public ${dataModelBeanName} getDataModel() {
        return dataModel;
    }

	<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
	public void delete(ActionEvent event) {
		if (selectedObject != null) {
			if (service.isRemovable(selectedObject)) {
				final Integer deleted = service.delete(selectedObject);
				if (deleted == 1) {
					FacesUtils.addInfoMessage("${toUnderscoreCase(lowerModelName)?lower_case}_deleted");
				} else {
					FacesUtils.addErrorMessage("error_delete_failed");
				}
			} else {
				FacesUtils.addErrorMessage("error_unable_to_delete");
			}
		}
	}
	</#if>
}
</#list>