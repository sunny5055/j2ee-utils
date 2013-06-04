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
<#if util.xml.getAttribute(entity.@readOnly, "false") == "false">
<@addTo assignTo="imports" element="javax.faces.event.ActionEvent" />
</#if>
<@addTo assignTo="imports" element="org.primefaces.model.LazyDataModel" />
<@addTo assignTo="imports" element="org.primefaces.model.SortMeta" />

<@addTo assignTo="imports" element="${modelPackageName}.${modelName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />
<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.util.SearchCriteriaUtil" />

<@addTo assignTo="imports" element="${util.getBasePackageName(entityPackageName)}.web.util.FacesUtils"/>
<@addTo assignTo="imports" element="${filtersBeanPackageName}.${filtersBeanName}" />

${getImports(false, dataTableBeanPackageName, imports)}


@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${dataTableBeanName} extends AbstractSingleRowSelectionDataTableBean<${primaryKeyType}, ${modelName}> {
   	@Autowired
    private ${serviceName} service;

	@Autowired
	private ${filtersBeanName} filtersBean;

    private LazyDataModel<${modelName}> dataModel;

    public ${dataTableBeanName}() {
        super();
        dataModel = new LazyDataModel<${modelName}>() {
            @Override
            public List<${modelName}> load(int first, int pageSize, List<SortMeta> sortFields, Map<String, String> filters) {
                List<${modelName}> ${lowerModelName}s = null;

                final SearchCriteria searchCriteria = SearchCriteriaUtil.toSearchCriteria(first, pageSize, sortFields, filters);
				if (filtersBean != null && filtersBean.hasFilters()) {
					searchCriteria.getFilters().putAll(filtersBean.getFilters());
				}

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

    public String getListEmpty() {
		String msg = null;
		if (filtersBean != null && filtersBean.hasFilters()) {
			msg = FacesUtils.getLabel("list_result_empty");
		} else {
			msg = FacesUtils.getLabel("${lowerModelName}_list_empty");
		}
		return msg;
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