<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","b":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "../common/xml.ftl" as xml>
<#import "../common/java.ftl" as java>

<#function getTypes node>
	<#assign types = [] />
	<#if node?node_name = "property" || node?node_name = "type" || node?node_name = "parameter">
  		<#assign types = types + [ node.@type ] />
	<#elseif node?node_name = "property-list" || node?node_name = "type-list" || node?node_name = "parameter-list">
  		<#assign types = types + [ node.@type ] />
  		<#assign types = types + [ xml.getAttribute(node.@value) ] />
	<#elseif node?node_name = "property-map" || node?node_name = "type-map" || node?node_name = "parameter-map">
		<#assign types = types + [ node.@type ] />
  		<#assign types = types + [ xml.getAttribute(node.@value) ] />
  		<#assign types = types + [ xml.getAttribute(node.@key) ] />
	<#elseif node?node_name = "constructor">
		<#assign parameters = node["b:parameters/*"]>
		<#list parameters as parameter>
			<#assign types = types +  getTypes(parameter) />
		</#list>
	<#elseif node?node_name = "operation">
		<#if node["b:return/b:type"]?is_node>
			<#assign types = types + getTypes(node["b:return/b:type"]) />
		<#elseif node["b:return/b:type-list"]?is_node>
			<#assign types = types + getTypes(node["b:return/b:type-list"]) />
		<#else>
			<#assign types = types + getTypes(node["b:return/b:type-map"]) />
		</#if>

		<#assign parameters = node["b:parameters/*"]>
		<#list parameters as parameter>
			<#assign types = types + getTypes(parameter) />
		</#list>
	</#if>

	<#return types>
</#function>

<#function getParametersDeclaration parameters>
	<@myList list=parameters var="parameter" assignTo="parametersDeclaration">
	${getType(parameter.@type, xml.getAttribute(parameter.@value))} ${parameter.@name}
	</@myList>
	<#return parametersDeclaration>
</#function>


<#function getReturnType operation>
	<#local returnType= "">
	<#if operation["b:return/b:type"]?is_node>
		<#local returnType = getType(operation["b:return/b:type/@type"])>
	<#else>
		<#local typeList = operation["b:return/b:type-list"]>
		<#local returnType = getType(typeList.@type, xml.getAttribute(typeList.@value))>
	</#if>
	<#return returnType>
</#function>

<#function getModifiersFrom node>
	<#return getModifiers(xml.getAttribute(node.@abstract), xml.getAttribute(node.@static), xml.getAttribute(node.@final))>
</#function>


<#macro getInterfaceProperty property>
	<#local type = getType(property.@type)>
	<#if type == "String">
	${type} ${property.@name} =  "${property.@value}";
	<#else>
	${type} ${property.@name} =  ${property.@value};
	</#if>
</#macro>


<#macro getProperty property>
	<#local visibility= xml.getAttribute(property.@visibility, "private")>
	${visibility} ${getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key))} ${property.@name};
</#macro>


<#macro initProperties property>
	this.${property.@name} = ${java.resolveDefaults(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key))};
</#macro>


<#macro getter property>
	<@java.getter type=getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro setter property>
	<@java.setter type=getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro add property>
	<#if property?node_name = "property-list">
		<#local value= "${getClassName(property.@value)}">
		public void add${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.add(${value?uncap_first});
			}
		}
	<#elseif property?node_name = "property-map">
		<#local name = "${property.@name?substring(0, property.@name?length-1)}">
		<#local key= "${getClassName(property.@key)}">
		<#local value= "${getClassName(property.@value)}">
		public void add${name?cap_first}(${key} key, ${value} ${name?uncap_first}) {
			if(${java.checkNotNull(key, "key")} && ${java.checkNotNull(value, name?uncap_first)}) {
				this.${property.@name}.put(key, ${name?uncap_first});
			}
		}
	</#if>
</#macro>

<#macro remove property>
	<#if property?node_name = "property-list">
		<#local value= "${getClassName(property.@value)}">
		public void remove${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.remove(${value?uncap_first});
			}
		}
	<#elseif property?node_name = "property-map">
		<#local name = "${property.@name?substring(0, property.@name?length-1)}">
		<#local key= "${getClassName(property.@key)}">
		public void remove${name?cap_first}(${key} key) {
			if (${java.checkNotNull(key, "key")}) {
				this.${property.@name}.remove(key);
			}
		}
	</#if>
</#macro>

<#macro constructor className constructor>
	<#assign parameters = constructor["b:parameters/*"]>
	<#local visibility= xml.getAttribute(constructor.@visibility, "public")>
	${visibility} ${className}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#compress>
	<#if constructor["b:content"]?is_node>
		${constructor["b:content"]}
	</#if>
	</#compress>
	}
</#macro>

<#macro operation operation>
	<#assign parameters = operation["b:parameters/*"]>
	<#local visibility= xml.getAttribute(operation.@visibility, "public")>
	${visibility} ${getModifiersFrom(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#if operation["b:content"]?is_node>
		${operation["b:content"]}
	<#else>
		//TODO to complete
	</#if>
	}
</#macro>


<#macro interfaceOperation operation>
	<#assign parameters = operation["b:parameters/*"]>
	${getModifiersFrom(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>);
</#macro>
