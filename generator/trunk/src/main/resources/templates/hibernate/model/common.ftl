<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#function getHibernateAnnotation class property>
  <#local hibernateAnnotation= "">
  <#if property?node_name = "id">
    <#local hibernateAnnotation= "@Id">
    <#local hibernateAnnotation= hibernateAnnotation + " @GeneratedValue(strategy = GenerationType.AUTO)">
    <#local hibernateAnnotation= hibernateAnnotation + " @Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(class.@columnPrefix, property)}\"">
    <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "embedded-id">
    <#local hibernateAnnotation= "@EmbeddedId">
  <#elseif property?node_name = "column">
    <#local hibernateAnnotation= "@Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(class.@columnPrefix, property)}\"">
    <#if xml.getAttribute(property.@unique) == "true">
      <#local hibernateAnnotation= hibernateAnnotation + ", unique = true">
    </#if>
    <#if xml.getAttribute(property.@nullable) == "false">
      <#local hibernateAnnotation= hibernateAnnotation + ", nullable = false">
    </#if>
    <#if getType(property.@type) == "String" && xml.getAttribute(property.@length) != "">
      <#local hibernateAnnotation= hibernateAnnotation + ", length = ${xml.getAttribute(property.@length)}">
    </#if>
    <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "many-to-one">
      <#local hibernateAnnotation= "@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY">
      <#if xml.getAttribute(property.@nullable) == "false">
      <#local hibernateAnnotation= hibernateAnnotation + ", optional = false)">
    </#if>
      <#local hibernateAnnotation= hibernateAnnotation + ")">
      <#local hibernateAnnotation= hibernateAnnotation + " @JoinColumn(name = \"${getColumnName(class.@columnPrefix, property)}\")">
  <#elseif property?node_name = "one-to-many">
    <#local hibernateAnnotation= "@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(class, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "many-to-many">
    <#local hibernateAnnotation= "@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(class, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(class, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  </#if>
  <#return hibernateAnnotation>
</#function>


<#macro getProperty property>
  <#local visibility= xml.getAttribute(property.@visibility, "private")>
  <#if property?node_name = "id">
    ${visibility} ${getType(property.@type)} ${property.@name};
  <#elseif property?node_name = "embedded-id">
    ${visibility} ${getType(property.@targetEntity)} ${property.@name};
  <#elseif property?node_name = "column">
    ${visibility} ${getType(property.@type)} ${property.@name};
  <#elseif property?node_name = "many-to-one">
    ${visibility} ${getType(xml.getAttribute(property.@targetEntity))} ${property.@name};
  <#elseif property?node_name = "one-to-many">
    ${visibility} ${getType(property.@listType, xml.getAttribute(property.@targetEntity))} ${property.@name};
  <#elseif property?node_name = "many-to-many">
    ${visibility} ${getType(property.@listType, xml.getAttribute(property.@targetEntity))} ${property.@name};
  </#if>
</#macro>


<#macro initProperties property>
  <#if property?node_name = "one-to-many">
    this.${property.@name} = ${java.resolveDefaults(property.@listType, xml.getAttribute(property.@targetEntity))};
  <#elseif property?node_name = "many-to-many">
    this.${property.@name} = ${java.resolveDefaults(property.@listType, xml.getAttribute(property.@targetEntity))};
  </#if>
</#macro>


<#macro getterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  public ${type} getPrimaryKey() {
    return ${property.@name};
  }
</#macro>

<#macro setterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  public void setPrimaryKey(${type} primaryKey) {
    this.${property.@name} = primaryKey;
  }
</#macro>

<#macro getter property>
  <#if property?node_name = "id">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.getter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro setter property>
  <#if property?node_name = "id">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.setter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.setter type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.setter type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.setter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.setter type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro add property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local value= "${getClassName(property.@targetEntity)}">
    public void add${value?cap_first}(${value} ${value?uncap_first}) {
      if (${java.checkNotNull(value, value?uncap_first)}) {
        this.${property.@name}.add(${value?uncap_first});
      }
    }
  </#if>
</#macro>

<#macro remove property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local value= "${getClassName(property.@targetEntity)}">
    public void remove${value?cap_first}(${value} ${value?uncap_first}) {
      if (${java.checkNotNull(value, value?uncap_first)}) {
        this.${property.@name}.remove(${value?uncap_first});
      }
    }
  </#if>
</#macro>

<#macro constructor className constructor>
  <#assign parameters = constructor["h:parameters/*"]>
  <#local visibility= xml.getAttribute(constructor.@visibility, "public")>
  ${visibility} ${className}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
  <#compress>
  <#if constructor["h:content"]?is_node>
    ${constructor["h:content"]}
  </#if>
  </#compress>
  }
</#macro>

<#macro operation operation>
  <#assign parameters = operation["h:parameters/*"]>
  <#local visibility= xml.getAttribute(operation.@visibility, "public")>
  ${visibility} ${getModifiersFrom(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
  <#if operation["h:content"]?is_node>
    ${operation["h:content"]}
  <#else>
    //TODO to complete
  </#if>
  }
</#macro>


<#macro interfaceOperation operation>
  <#assign parameters = operation["h:parameters/*"]>
  ${getModifiersFrom(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>);
</#macro>
