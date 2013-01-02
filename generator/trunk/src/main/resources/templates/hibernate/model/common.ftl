<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","h":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#include "../common.ftl">

<#function hasNamedQuery doc entity>
	<#local primaryKey = getPrimaryKey(entity)>
	<#if primaryKey?? && primaryKey?node_name == "embedded-id">
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:column[@unique='true']" assignTo="uniqueColumns" />
	<#if uniqueColumns?? && uniqueColumns?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:many-to-one" assignTo="manyToOnes" />
	<#if manyToOnes?? && manyToOnes?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:one-to-many" assignTo="oneToManys" />
	<#if oneToManys?? && oneToManys?size gt 0>
		<#return "true">
	</#if>
	<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:many-to-many" assignTo="manyToManys" />
	<#if manyToManys?? && manyToManys?size gt 0>
		<#return "true">
	</#if>

	<#return "false">
</#function>


<#macro getNamedQueries doc daoName entity>
	<#if hasNamedQuery(doc, entity) == "true">
		<#local primaryKey = getPrimaryKey(entity) />
		<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:column[@unique='true']" assignTo="uniqueColumns" />
		<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:many-to-one" assignTo="manyToOnes" />
		<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:one-to-many" assignTo="oneToManys" />
		<@xPath xml=doc expression="//h:entity[@name='${entity.@name}']/h:properties/h:many-to-many" assignTo="manyToManys" />

		<#local namedQueries = "" />
		<#if primaryKey?node_name = "embedded-id">
			<#local namedQueries = namedQueries + getNamedQuery(doc, daoName, entity, primaryKey) />
		</#if>
		<#if uniqueColumns??>
			<#list uniqueColumns as column>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getNamedQuery(doc, daoName, entity, column) />
			</#list>
		</#if>
		<#if manyToOnes??>
			<#list manyToOnes as manyToOne>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getNamedQuery(doc, daoName, entity, manyToOne) />
			</#list>
		</#if>
		<#if oneToManys??>
			<#list oneToManys as oneToMany>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getNamedQuery(doc, daoName, entity, oneToMany) />
			</#list>
		</#if>
		<#if manyToManys??>
			<#list manyToManys as manyToMany>
				<#if namedQueries?length gt 0>
					<#local namedQueries = namedQueries + "," />
				</#if>
				<#local namedQueries = namedQueries + getNamedQuery(doc, daoName, entity, manyToMany) />
			</#list>
		</#if>

		@NamedQueries({
			${namedQueries}
		})
	</#if>
</#macro>

<#function getNamedQuery doc daoName entity property>
  <#local namedQuery= "">
  <#local columnPrefix = entity.@columnPrefix?lower_case />
    <#if property?node_name = "embedded-id">
    	<#assign columns = property["h:properties/h:column"]>
      	<#list columns as column>
      		<#if column_index != 0>
      			<#local namedQuery= namedQuery + ", " />
      		</#if>

        	<#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(column.@name, false, daoName) + ", query = \"select count(*) from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${column.@name} = :${column.@name}\")," />
        	<#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(column.@name, false, daoName) + ", query = \"from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${column.@name} = :${column.@name}\")" />
    	</#list>
    <#elseif property?node_name = "column" && xml.getAttribute(property.@unique) == "true">
        <#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(property.@name, true, daoName) + ", query = \"select count(*) from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${property.@name} = :${property.@name}\"), " />
        <#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(property.@name, true, daoName) + ", query = \"from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${property.@name} = :${property.@name}\")" />
    <#elseif property?node_name = "many-to-one" || property?node_name = "one-to-many" || property?node_name = "many-to-many">
      <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
          <#local propertyName = property.@name?substring(0, property.@name?length-1)>
      <#else>
          <#local propertyName = property.@name>
      </#if>
      <@xPath xml=doc expression="//h:entity[@name='${getType(property.@targetEntity)}']" assignTo="targetEntity" />
      <#if targetEntity??>
      	<#local targetColumnPrefix = targetEntity.@columnPrefix?lower_case />
        <#local targetColumn = getPrimaryKey(targetEntity)>
        <#local propertyName = "${propertyName}Id">
      </#if>

      <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    	  <#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(propertyName, false, daoName) + ", query = \"select count(*) from ${entity.@name} as ${columnPrefix} left join ${columnPrefix}.${property.@name} as ${targetColumnPrefix} where ${targetColumnPrefix}.${targetColumn.@name} = :${propertyName}\")," />
	      <#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(propertyName, false, daoName) + ", query = \"from ${entity.@name} as ${columnPrefix} left join ${columnPrefix}.${property.@name} as ${targetColumnPrefix} where ${targetColumnPrefix}.${targetColumn.@name} = :${propertyName}\")" />
      <#else>
	      <#local namedQuery= namedQuery + "@NamedQuery(name = " + getCountQueryConstant(propertyName, false, daoName) + ", query = \"select count(*) from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${targetColumn.@name} = :${propertyName}\")," />
	      <#local namedQuery= namedQuery + "@NamedQuery(name = " + getFindQueryConstant(propertyName, false, daoName) + ", query = \"from ${entity.@name} as ${columnPrefix} where ${columnPrefix}.${property.@name}.${targetColumn.@name} = :${propertyName}\")" />
      </#if>
    </#if>
    <#return namedQuery />
</#function>

<#function getHibernateAnnotation entity property>
  <#local hibernateAnnotation= "">
  <#if property?node_name = "id">
    <#local hibernateAnnotation= "@Id">
    <#local hibernateAnnotation= hibernateAnnotation + " @GeneratedValue(strategy = GenerationType.AUTO)">
    <#local hibernateAnnotation= hibernateAnnotation + " @Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(entity.@columnPrefix, property)}\"">
    <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "embedded-id">
    <#local hibernateAnnotation= "@EmbeddedId">
  <#elseif property?node_name = "column">
    <#local hibernateAnnotation= "@Column(">
    <#local hibernateAnnotation= hibernateAnnotation + "name = \"${getColumnName(entity.@columnPrefix, property)}\"">
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
      <#local hibernateAnnotation= hibernateAnnotation + " @JoinColumn(name = \"${getColumnName(entity.@columnPrefix, property)}\")">
  <#elseif property?node_name = "one-to-many">
    <#local hibernateAnnotation= "@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(entity, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(entity, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(entity, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  <#elseif property?node_name = "many-to-many">
    <#local hibernateAnnotation= "@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)">
      <#local hibernateAnnotation= hibernateAnnotation + "@JoinTable(name = \"${getJoinTableName(entity, property)}\"">
      <#local hibernateAnnotation= hibernateAnnotation + ", joinColumns = { @JoinColumn(name = \"${getJoinColumnName(entity, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ", inverseJoinColumns = { @JoinColumn(name = \"${getInverseJoinColumnName(entity, property)}\") }">
      <#local hibernateAnnotation= hibernateAnnotation + ")">
  </#if>
  <#return hibernateAnnotation>
</#function>


<#macro getProperty property>
  <#local visibility= xml.getAttribute(property.@visibility, "private")>
  <#if property?node_name = "id">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "embedded-id">
    <@java.getProperty visibility=visibility type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "column">
    <@java.getProperty visibility=visibility type=getType(property.@type) name=property.@name />
  <#elseif property?node_name = "many-to-one">
    <@java.getProperty visibility=visibility type=getType(property.@targetEntity) name=property.@name />
  <#elseif property?node_name = "one-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  <#elseif property?node_name = "many-to-many">
    <@java.getProperty visibility=visibility type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
  </#if>
</#macro>


<#macro initProperties property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <@java.initProperties type=property.@listType name=property.@name value=xml.getAttribute(property.@targetEntity) />
  </#if>
</#macro>


<#macro getterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  <@java.getter type=type name=property.@name methodName="getPrimaryKey" />
</#macro>

<#macro setterPrimaryKey property>
  <#if property?node_name = "id">
    <#local type = getType(property.@type)>
  <#elseif property?node_name = "embedded-id">
    <#local type = getType(property.@targetEntity)>
  </#if>
  @Override
  <@java.setter type=type name=property.@name methodName="setPrimaryKey" argName="primaryKey"/>
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


<#macro addMethod property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local type = "${getClassName(property.@targetEntity)}">
    <@java.addListMethod type=type name=property.@name />
  </#if>
</#macro>

<#macro removeMethod property>
  <#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
    <#local type = "${getClassName(property.@targetEntity)}">
    <@java.removeListMethod type=type name=property.@name />
  </#if>
</#macro>

<#macro constructor className constructor>
  <#assign parameters = constructor["h:parameters/*"]>
  <#local visibility= xml.getAttribute(constructor.@visibility, "public")>
  <#if constructor["h:content"]?is_node>
    <@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) content=constructor["h:content"] />
  <#else>
  <@java.constructor visibility=visibility className=className parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>


<#macro interfaceOperation operation>
  <#assign parameters = operation["h:parameters/*"]>
  <@java.interfaceOperation modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
</#macro>


<#macro operation operation>
  <#assign parameters = operation["h:parameters/*"]>
  <#local visibility= xml.getAttribute(operation.@visibility, "public")>
  <#if operation["h:content"]?is_node>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) content=operation["h:content"] />
  <#else>
    <@java.operation visibility=visibility modifiers=getModifiersFrom(operation) returnType=getReturnType(operation) methodName=operation.@name parameters=getParametersDeclaration(parameters) />
  </#if>
</#macro>

