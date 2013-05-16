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
<#assign dataTableBeanPackageName = util.getDataTableBeanPackageName(entityPackageName) />
<#assign dataTableBeanName = util.getDataTableBeanName(entity.@name) />

<@resolveKey map=config key="dataTableBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(dataTableBeanPackageName) />

<@changeOutputFile name=filePath + "/"+ dataTableBeanName + ".java" />

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
<@addTo assignTo="imports" element="org.primefaces.model.SortOrder" />

<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<@addTo assignTo="imports" element="com.googlecode.jutils.StringUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />

${getImports(true, dataTableBeanPackageName, imports)}


@Controller
@Scope("view")
@SuppressWarnings("serial")
public class ${dataTableBeanName} extends AbstractDataTableBean<${primaryKeyType}, ${entityName}> {
   	@Autowired
    private ${serviceName} service;

    private LazyDataModel<${entityName}> dataModel;

    public ${dataTableBeanName}() {
        super();
        dataModel = new LazyDataModel<${entityName}>() {
            @Override
            public List<${entityName}> load(int first, int pageSize, String sortField, SortOrder sortOrder,
                    Map<String, String> filters) {
                List<${entityName}> ${lowerEntityName}s = null;

                final SearchCriteria searchCriteria = SearchCriteriaUtil.toSearchCriteria(first, pageSize, sortField,
                        sortOrder, filters);
                final Integer count = service.count(searchCriteria);
                this.setRowCount(count);
                if (count > 0) {
                    ${lowerEntityName}s = service.findAll(searchCriteria);
                }
                return ${lowerEntityName}s;
            }

            @Override
            public ${entityName} getRowData(String rowKey) {
                ${entityName} ${lowerEntityName} = null;
                if (!StringUtil.isBlank(rowKey)) {
                    final Integer primaryKey = Integer.valueOf(rowKey);
                    ${lowerEntityName} = service.get(primaryKey);
                }
                return ${lowerEntityName};
            }

            @Override
            public Object getRowKey(${entityName} ${lowerEntityName}) {
                String rowKey = null;
                if (${lowerEntityName} != null) {
                    rowKey = ${lowerEntityName}.getStringPrimaryKey();
                }
                return rowKey;
            }
        };

        // fix
        dataModel.setRowCount(1);
        dataModel.setPageSize(1);
    }

    public LazyDataModel<${entityName}> getDataModel() {
        return dataModel;
    }

    public void setDataModel(LazyDataModel<${entityName}> dataModel) {
        this.dataModel = dataModel;
    }

    @Override
    protected String getViewPage() {
        return null;
    }

    public String getListEmpty() {
        String msg = null;
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        return msg;
    }

    public void view() {
        if (selectedObject != null) {
            final FacesContext facesContext = FacesContext.getCurrentInstance();

        }
    }
}
</#list>