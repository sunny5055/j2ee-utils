<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "common.ftl" as util>
<#assign entity = xml["//h:entity[@name=$className]"]>
<#assign interfaces = xml["//h:entity[@name=$className]//h:interface"]>
<#assign primaryKey = util.getPrimaryKey(entity)>
<#assign columns = xml["//h:entity[@name=$className]/h:properties/h:column"]>
<#assign manyToOnes = xml["//h:entity[@name=$className]/h:properties/h:many-to-one"]>
<#assign oneToManys = xml["//h:entity[@name=$className]/h:properties/h:one-to-many"]>
<#assign manyToManys = xml["//h:entity[@name=$className]/h:properties/h:many-to-many"]>

<#if daoImplPackageName??>
package ${daoImplPackageName};
</#if>

<#assign imports = [] />
<@addTo assignTo="imports" element="org.springframework.stereotype.Repository" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.dao.AbstractGenericDaoHibernate" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.util.QueryUtil" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.Search" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SearchCriteria" />
<@addTo assignTo="imports" element="com.googlecode.jutils.dal.SortOrder" />
<@addTo assignTo="imports" element="${packageName}.${entity.@name}" />
<@addTo assignTo="imports" element="${daoPackageName}.${daoName}" />

<#if primaryKey?node_name == "embedded-id">
	<@addTo assignTo="imports" element="${packageName}.${primaryKey.@targetEntity}" />
</#if>

<@addTo assignTo="imports" element="java.util.List" />
<@addTo assignTo="imports" element="java.util.Map" />


${getImports(false, daoImplPackageName, imports)}

@Repository
public class ${daoImplName} extends AbstractGenericDaoHibernate<${util.getPrimaryKeyType(entity)}, ${entity.@name}> implements ${daoName} {

    public ${daoImplName}() {
        super(${entity.@name}.class);
    }

	<@util.getMethod doc=xml daoName=daoName entity=entity property=primaryKey/>
	<#list columns as column>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=column/>
	</#list>
	<#list manyToOnes as manyToOne>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=manyToOne/>
	</#list>
	<#list oneToManys as oneToMany>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=oneToMany/>
	</#list>
	<#list manyToManys as manyToMany>
	<@util.getMethod doc=xml daoName=daoName entity=entity property=manyToMany/>
	</#list>

    @Override
    protected Search getSearch(SearchCriteria searchCriteria) {
        Search search = null;
        if (searchCriteria != null) {
            search = new Search();

            final StringBuilder buffer = new StringBuilder();
            buffer.append("from Camera ca ");

            if (searchCriteria.hasFilters()) {
                buffer.append("where ");
                int index = 0;
                for (final Map.Entry<String, Object> entry : searchCriteria.getFilters().entrySet()) {
                    if (entry.getValue() != null) {
                        if (index != 0) {
                            buffer.append("AND ");
                        }
                        if (entry.getKey().equals("code")) {
                            buffer.append("upper(ca.code) like upper(:code) ");
                            search.addStringParameter("code", entry.getValue());
                        } else if (entry.getKey().equals("axe")) {
                            buffer.append("ca.axe.code = :axe ");
                            search.addStringParameter("axe", entry.getValue());
                        } else if (entry.getKey().equals("departement")) {
                            buffer.append("ca.departement.code = :departement ");
                            search.addStringParameter("departement", entry.getValue());
                        } else if (entry.getKey().equals("nom")) {
                            buffer.append("upper(ca.nom) like upper(:nom) ");
                            search.addStringParameter("nom", entry.getValue());
                        } else if (entry.getKey().equals("etat")) {
                            buffer.append("ca.etat.code = :etat ");
                            search.addStringParameter("etat", entry.getValue());
                        }

                        index++;
                    }
                }
            }

            search.setCountQuery("select count(*) " + buffer.toString());

            buffer.append("order by ");
            if (searchCriteria.hasSorts()) {
                int index = 0;
                for (final Map.Entry<String, SortOrder> entry : searchCriteria.getSorts().entrySet()) {
                    if (index != 0) {
                        buffer.append(", ");
                    }
                    if (entry.getKey().equals("code")) {
                        buffer.append("ca.code ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("axe")) {
                        buffer.append("ca.axe.code ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("departement")) {
                        buffer.append("ca.departement.libelle ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("pr")) {
                        buffer.append("ca.pr ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                        buffer.append(", ca.abscisse ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("nom")) {
                        buffer.append("ca.nom ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    } else if (entry.getKey().equals("etat")) {
                        buffer.append("ca.etat.libelle ");
                        if (entry.getValue() == SortOrder.DESCENDING) {
                            buffer.append("desc ");
                        }
                    }

                    index++;
                }
            }

            if (searchCriteria.hasSorts()) {
                buffer.append(", ");
            }
            buffer.append("ca.axe.code asc, ");
            buffer.append("ca.departement.code asc, ");
            buffer.append("ca.pr asc, ");
            buffer.append("ca.abscisse asc ");

            search.setQuery(buffer.toString());
        }
        return search;
    }
}
