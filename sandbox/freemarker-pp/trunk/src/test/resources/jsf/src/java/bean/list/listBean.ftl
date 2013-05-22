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
<#assign listBeanPackageName = util.getListBeanPackageName(entityPackageName) />
<#assign listBeanName = util.getListBeanName(entity.@name) />

<@resolveKey map=config key="listBeanFilePath" values=[projectName] assignTo="filePath"/>
<#assign filePath = filePath + "/" + packageToDir(listBeanPackageName) />

<@changeOutputFile name=filePath + "/"+ listBeanName + ".java" />

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


<#if listBeanPackageName?? && listBeanPackageName?length gt 0>
package ${listBeanPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.context.annotation.Scope" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />

<@addTo assignTo="imports" element="javax.faces.model.SelectItem" />

<@addTo assignTo="imports" element="${entityPackageName}.${entityName}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

${getImports(true, listBeanPackageName, imports)}

@Controller
@Scope("session")
@SuppressWarnings("serial")
public class ${listBeanName} extends AbstractListBean<${primaryKeyType}, ${entityName}, ${serviceName}> {
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
    protected SelectItem toSelectItem(${entityName} ${lowerEntityName}) {
        return new SelectItem(${lowerEntityName}.getPrimaryKey(), ${lowerEntityName}.toString());
    }
}
</#list>