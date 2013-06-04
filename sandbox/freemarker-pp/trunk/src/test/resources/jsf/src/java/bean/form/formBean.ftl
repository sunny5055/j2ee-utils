<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#if util.xml.getAttribute(entity.@readOnly, "false") != "true">
<#include "/common/assign.inc" />

<@resolveKey map=config key="formBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(formBeanPackageName) />
<@changeOutputFile name=filePath + "/"+ formBeanName + ".java" />

<#if formBeanPackageName?? && formBeanPackageName?length gt 0>
package ${formBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="java.io.IOException" />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />
<@addTo assignTo="imports" element="javax.faces.event.ActionEvent" />
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

<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.web.util.FacesUtils"/>

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
    	return (${primaryKeyType}) FacesUtils.getFlashAttribute("${lowerModelName}Id");
    }

    @Override
    protected ${modelName} getNewInstance() {
        return new ${modelName}();
    }

 	public void create(ActionEvent event) {
        if (model != null) {
            final Integer primaryKey = this.getService().create(model);
			if (primaryKey != null) {
				FacesUtils.addInfoMessage("${lowerModelName}_created");
				FacesUtils.setFlashAttribute("${lowerModelName}Id", primaryKey);

				try {
					FacesUtils.redirect("${util.getWebResource(updateXhtmlFilePath, updateXhtmlFileName)}", true);
				} catch (final IOException e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(e.getMessage(), e);
					}
				}
			} else {
				FacesUtils.addErrorMessage("error_create_failed");
			}
        }
    }

    public void update(ActionEvent event) {
        if (model != null) {
            this.getService().update(model);

            reInit();
        }
    }

    public void delete(ActionEvent event) {
		if (model != null) {
			if (service.isRemovable(model)) {
				final Integer deleted = this.getService().delete(model);
				if (deleted == 1) {
					FacesUtils.addInfoMessage("${lowerModelName}_deleted");

					try {
						FacesUtils.redirect("${util.getWebResource(listXhtmlFilePath, listXhtmlFileName)}", true);
					} catch (final IOException e) {
						if (LOGGER.isDebugEnabled()) {
							LOGGER.debug(e.getMessage(), e);
						}
					}
				} else {
					FacesUtils.addErrorMessage("error_delete_failed");
				}
			} else {
				FacesUtils.addErrorMessage("error_unable_to_delete");
			}
		}
	}
}
</#if>
</#list>