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

<@addTo assignTo="imports" element="java.util.List" />
<@addTo assignTo="imports" element="java.util.Map" />
<@addTo assignTo="imports" element="javax.faces.context.FacesContext" />
<@addTo assignTo="imports" element="org.primefaces.model.LazyDataModel" />
<@addTo assignTo="imports" element="org.primefaces.model.SortMeta" />

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />
<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.util.SearchCriteriaUtil" />

<@addTo assignTo="imports" element="java.io.IOException"/>
<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.web.util.FacesUtils"/>

${getImports(false, dataTableBeanPackageName, imports)}


@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${dataTableBeanName} extends AbstractDataTableBean<${primaryKeyType}, ${modelName}> {
   	@Autowired
    private ${serviceName} service;

    private LazyDataModel<${modelName}> dataModel;

    public ${dataTableBeanName}() {
        super();
        dataModel = new LazyDataModel<${modelName}>() {
            @Override
            public List<${modelName}> load(int first, int pageSize, List<SortMeta> sortFields, Map<String, String> filters) {
                List<${modelName}> ${lowerModelName}s = null;

                final SearchCriteria searchCriteria = SearchCriteriaUtil.toSearchCriteria(first, pageSize, sortFields, filters);
                final Integer count = service.count(searchCriteria);
                this.setRowCount(count);
                if (count > 0) {
                    ${lowerModelName}s = service.findAll(searchCriteria);
                }
                return ${lowerModelName}s;
            }

            @Override
            public ${modelName} getRowData(String rowKey) {
                ${modelName} ${lowerModelName} = null;
                if (!StringUtil.isBlank(rowKey)) {
                    final ${primaryKeyType} primaryKey = ${util.java.convertFromString(primaryKeyType, "rowKey")};
                    ${lowerModelName} = service.get(primaryKey);
                }
                return ${lowerModelName};
            }

            @Override
            public Object getRowKey(${modelName} ${lowerModelName}) {
                String rowKey = null;
                if (${lowerModelName} != null) {
                    rowKey = ${lowerModelName}.getStringPrimaryKey();
                }
                return rowKey;
            }
        };

        // fix
        dataModel.setRowCount(1);
        dataModel.setPageSize(1);
    }

    public LazyDataModel<${modelName}> getDataModel() {
        return dataModel;
    }

    public void setDataModel(LazyDataModel<${modelName}> dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    protected String getViewPage() {
    	<#if util.xml.getAttribute(entity.@readOnly) != "true">
        return "${util.getWebResource(updateXhtmlFilePath, updateXhtmlFileName)}";
		<#else>
        return "${util.getWebResource(viewXhtmlFilePath, viewXhtmlFileName)}";
        </#if>
    }

    public String getListEmpty() {
        String msg = null;
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        return msg;
    }

    public void view() {
        if (selectedObject != null) {
			final FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesUtils.setFlashAttribute(facesContext, "${lowerModelName}Id", selectedObject.getPrimaryKey());

			try {
				FacesUtils.redirect(facesContext, getViewPage(), true);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
    }
}
</#list>