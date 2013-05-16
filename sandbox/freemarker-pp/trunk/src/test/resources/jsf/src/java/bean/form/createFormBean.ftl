<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/java/bean/includes/bean.inc" as util>

<@dropOutputFile />

<#assign entities = xml["//j:entity"]/>
<#assign projectName = xml["//p:configuration/p:projectName"]/>

<#list entities as entity>
<#assign entityPackageName = entity["ancestor::p:package/@name"] />
<#assign entityName = util.getEntityName(entity.@name) />
<#assign servicePackageName = util.getServicePackageName(entityPackageName) />
<#assign serviceName = util.getServiceName(entity.@name) />
<#assign createFormBeanPackageName = util.getCreateFormBeanPackageName(entityPackageName) />
<#assign createFormBeanName = util.getCreateFormBeanName(entity.@name) />
<@format format=config.listXhtmlFilePath values=[entityName?uncap_first] assignTo="listXhtmlFilePath"/>
<@format format=config.listXhtmlFileName values=[projectName] assignTo="listXhtmlFileName"/>

<@resolveKey map=config key="createFormBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(createFormBeanPackageName) />

<@changeOutputFile name=filePath + "/"+ createFormBeanName + ".java" />

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


<#if createFormBeanPackageName?? && createFormBeanPackageName?length gt 0>
package ${createFormBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="javax.faces.context.FacesContext" />

<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${util.getEmbeddedIdPackageName(entityPackageName)}.${primaryKeyType}" />
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
public class ${createFormBeanName} extends AbstractCreateFormBean<${primaryKeyType}, ${entityName}, ${serviceName}> {
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
    protected ${entityName} getNewEntityInstance() {
        return new ${entityName}();
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
        final FacesContext facesContext = FacesContext.getCurrentInstance();

        return create;
    }
}
</#list>