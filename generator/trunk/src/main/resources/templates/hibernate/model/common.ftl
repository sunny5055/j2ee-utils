<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/hibernate"}>
<#import "../../common/xml.ftl" as xml>
<#import "../../common/java.ftl" as java>


<#function getPrimaryKey class>
	<#if class["p:id"]?is_node>
		<#local primaryKey = class["p:id"]>
	<#else>
		<#local primaryKey = class["p:embedded-id"]>
	</#if>
	<#return primaryKey>
</#function>

<#function getPrimaryKeyType class>
	<#local primaryKeyType= "">
	<#if class["p:id"]?is_node>
		<#local primaryKeyType = java.getType(class["p:id/@type"])>
	<#else>
		<#local primaryKeyType = java.getType(class["p:embedded-id/@targetEntity"])>
	</#if>
	<#return primaryKeyType>
</#function>


<#function getParametersDeclaration parameters>
	<#local parametersDeclaration= "">
	<#list parameters as parameter>
		<#local parametersDeclaration = parametersDeclaration + java.getType(parameter.@type, xml.getAttribute(parameter.@value)) +" "+ parameter.@name>
		<#if parameter_has_next>
			<#local parametersDeclaration = parametersDeclaration + ", ">
		</#if>
	</#list>
	<#return parametersDeclaration>
</#function>


<#function getReturnType operation>
	<#local returnType= "">
	<#if operation["p:return/p:type"]?is_node>
		<#local returnType = java.getType(operation["p:return/p:type/@type"])>
	<#else>
		<#local typeList = operation["p:return/p:type-list"]>
		<#local returnType = java.getType(typeList.@type, xml.getAttribute(typeList.@value))>
	</#if>
	<#return returnType>
</#function>

<#function getModifiers node>
	<#return java.getModifiers(xml.getAttribute(node.@abstract), xml.getAttribute(node.@static), xml.getAttribute(node.@final))>
</#function>


<#function getColumnName columnPrefix property>
	<#local columnName= "">
	<#if xml.existAttribute(property.@column)>
		<#local columnName= "${property.@column}">
	<#else>
		<#if property?node_name = "id">
			<#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
		<#elseif property?node_name = "column">
			<#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name)}">
		<#elseif property?node_name = "many-to-one">
			<#local columnName= "${toUnderscoreCase(columnPrefix?lower_case+'_'+property.@name+'_id')}">
		</#if>
	</#if>
	<#return columnName>
</#function>

<#function getJoinColumnName class property>
	<#local joinColumnName= "">
	<#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
		<#local prefix = "${class.@name?substring(0,2)?lower_case+java.getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
		<#local joinColumnName= "${toUnderscoreCase(prefix+'_'+class.@name?lower_case+'_id')}">
	</#if>
	<#return joinColumnName>
</#function>

<#function getInverseJoinColumnName class property>
	<#local inverseJoinColumnName= "">
	<#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
		<#local prefix = "${class.@name?substring(0,2)?lower_case+java.getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
		<#local inverseJoinColumnName= "${toUnderscoreCase(prefix+'_'+java.getClassName(property.@targetEntity)?lower_case+'_id')}">
	</#if>
	<#return inverseJoinColumnName>
</#function>

<#function getJoinTableName class property>
	<#local joinTableName= "">
	<#if property?node_name = "many-to-one" || property?node_name = "many-to-many">
		<#if xml.existAttribute(property.@joinTable)>
			<#local joinTableName= "${property.@joinTable}">
		<#else>
			<#local prefix = "${class.@name?substring(0,2)?lower_case+java.getClassName(property.@targetEntity)?substring(0,1)?lower_case}">
			<#local joinTableName= "${toUnderscoreCase(prefix+'_'+class.@name?lower_case+'_'+java.getClassName(property.@targetEntity)?lower_case)}">
		</#if>
	</#if>
	<#return joinTableName>
</#function>

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
		<#if java.getType(property.@type) == "String" && xml.getAttribute(property.@length) != "">
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
		${visibility} ${java.getType(property.@type)} ${property.@name};
	<#elseif property?node_name = "embedded-id">
		${visibility} ${java.getType(property.@targetEntity)} ${property.@name};
	<#elseif property?node_name = "column">
		${visibility} ${java.getType(property.@type)} ${property.@name};
	<#elseif property?node_name = "many-to-one">
		${visibility} ${java.getType(xml.getAttribute(property.@targetEntity))} ${property.@name};
	<#elseif property?node_name = "one-to-many">
		${visibility} ${java.getType(property.@listType, xml.getAttribute(property.@targetEntity))} ${property.@name};
	<#elseif property?node_name = "many-to-many">
		${visibility} ${java.getType(property.@listType, xml.getAttribute(property.@targetEntity))} ${property.@name};
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
		<#local type = java.getType(property.@type)>
	<#elseif property?node_name = "embedded-id">
		<#local type = java.getType(property.@targetEntity)>
	</#if>
	@Override
	public ${type} getPrimaryKey() {
		return ${property.@name};
	}
</#macro>

<#macro setterPrimaryKey property>
	<#if property?node_name = "id">
		<#local type = java.getType(property.@type)>
	<#elseif property?node_name = "embedded-id">
		<#local type = java.getType(property.@targetEntity)>
	</#if>
	@Override
	public void setPrimaryKey(${type} primaryKey) {
		this.${property.@name} = primaryKey;
	}
</#macro>

<#macro getter property>
	<#if property?node_name = "id">
		<@java.getter type=java.getType(property.@type) name=property.@name />
	<#elseif property?node_name = "embedded-id">
		<@java.getter type=java.getType(property.@targetEntity) name=property.@name />
	<#elseif property?node_name = "column">
		<@java.getter type=java.getType(property.@type) name=property.@name />
	<#elseif property?node_name = "many-to-one">
		<@java.getter type=java.getType(property.@targetEntity) name=property.@name />
	<#elseif property?node_name = "one-to-many">
		<@java.getter type=java.getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
	<#elseif property?node_name = "many-to-many">
		<@java.getter type=java.getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
	</#if>
</#macro>


<#macro setter property>
	<#if property?node_name = "id">
		<@java.setter type=java.getType(property.@type) name=property.@name />
	<#elseif property?node_name = "embedded-id">
		<@java.setter type=java.getType(property.@targetEntity) name=property.@name />
	<#elseif property?node_name = "column">
		<@java.setter type=java.getType(property.@type) name=property.@name />
	<#elseif property?node_name = "many-to-one">
		<@java.setter type=java.getType(property.@targetEntity) name=property.@name />
	<#elseif property?node_name = "one-to-many">
		<@java.setter type=java.getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
	<#elseif property?node_name = "many-to-many">
		<@java.setter type=java.getType(property.@listType, xml.getAttribute(property.@targetEntity)) name=property.@name />
	</#if>
</#macro>


<#macro add property>
	<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
		<#local value= "${java.getClassName(property.@targetEntity)}">
		public void add${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.add(${value?uncap_first});
			}
		}
	</#if>
</#macro>

<#macro remove property>
	<#if property?node_name = "one-to-many" || property?node_name = "many-to-many">
		<#local value= "${java.getClassName(property.@targetEntity)}">
		public void remove${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.remove(${value?uncap_first});
			}
		}
	</#if>
</#macro>

<#macro constructor className constructor>
	<#assign parameters = constructor["p:parameters/*"]>
	<#local visibility= xml.getAttribute(constructor.@visibility, "public")>
	${visibility} ${className}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#compress>
	<#if constructor["p:content"]?is_node>
		${constructor["p:content"]}
	</#if>
	</#compress>
	}
</#macro>

<#macro operation operation>
	<#assign parameters = operation["p:parameters/*"]>
	<#local visibility= xml.getAttribute(operation.@visibility, "public")>
	${visibility} ${getModifiers(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#if operation["p:content"]?is_node>
		${operation["p:content"]}
	<#else>
		//TODO to complete
	</#if>
	}
</#macro>


<#macro interfaceOperation operation>
	<#assign parameters = operation["p:parameters/*"]>
	${getModifiers(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>);
</#macro>
