<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/java-beans"}>
<#import "../common/xml.ftl" as xml>
<#import "../common/java.ftl" as java>

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


<#macro getInterfaceProperty property>
	<#local type = java.getType(property.@type)>
	<#if type == "String">
	${type} ${property.@name} =  "${property.@value}";
	<#else>
	${type} ${property.@name} =  ${property.@value};
	</#if>
</#macro>


<#macro getProperty property>
	<#local visibility= xml.getAttribute(property.@visibility, "private")>
	${visibility} ${java.getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key))} ${property.@name};
</#macro>


<#macro initProperties property>
	this.${property.@name} = ${java.resolveDefaults(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key))};
</#macro>


<#macro getter property>
	<@java.getter type=java.getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro setter property>
	<@java.setter type=java.getType(property.@type, xml.getAttribute(property.@value), xml.getAttribute(property.@key)) name=property.@name />
</#macro>


<#macro add property>
	<#if property?node_name = "property-list">
		<#local value= "${java.getClassName(property.@value)}">
		public void add${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.add(${value?uncap_first});
			}
		}
	<#elseif property?node_name = "property-map">
		<#local name = "${property.@name?substring(0, property.@name?length-1)}">
		<#local key= "${java.getClassName(property.@key)}">
		<#local value= "${java.getClassName(property.@value)}">
		public void add${name?cap_first}(${key} key, ${value} ${name?uncap_first}) {
			if(${java.checkNotNull(key, "key")} && ${java.checkNotNull(value, name?uncap_first)}) {
				this.${property.@name}.put(key, ${name?uncap_first});
			}
		}
	</#if>
</#macro>

<#macro remove property>
	<#if property?node_name = "property-list">
		<#local value= "${java.getClassName(property.@value)}">
		public void remove${value?cap_first}(${value} ${value?uncap_first}) {
			if (${java.checkNotNull(value, value?uncap_first)}) {
				this.${property.@name}.remove(${value?uncap_first});
			}
		}
	<#elseif property?node_name = "property-map">
		<#local name = "${property.@name?substring(0, property.@name?length-1)}">
		<#local key= "${java.getClassName(property.@key)}">
		public void remove${name?cap_first}(${key} key) {
			if (${java.checkNotNull(key, "key")}) {
				this.${property.@name}.remove(key);
			}
		}
	</#if>
</#macro>

<#macro constructor className constructor>
	<#assign parameters = constructor["p:parameters/*"]>
	<#local visibility= xml.getAttribute(constructor.@visibility, "public")>
	${visibility} ${className}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#compress>
	<#if constructor.content?is_node>
		${constructor.content}
	</#if>
	</#compress>
	}
</#macro>

<#macro operation operation>
	<#assign parameters = operation["p:parameters/*"]>
	<#local visibility= xml.getAttribute(operation.@visibility, "public")>
	${visibility} ${getModifiers(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>) {
	<#if operation.content?is_node>
		${operation.content}
	<#else>
		//TODO to complete
	</#if>
	}
</#macro>


<#macro interfaceOperation operation>
	<#assign parameters = operation["p:parameters/*"]>
	${getModifiers(operation)} ${getReturnType(operation)} ${operation.@name}(<@compress single_line=true>${getParametersDeclaration(parameters)}</@compress>);
</#macro>
