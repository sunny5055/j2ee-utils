<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#include "/common/common.inc" />

<#macro getXhtmlOutput entityName property id="" path=entityName styleClass="">
	<#local value = "${path}.${property.@name}" />
	<#local type = "" />
	<#if property?node_name == "id" || property?node_name == "column">
    	<#local type = getClassName(property.@type) />
  	<#elseif property?node_name == "many-to-one">
    	<#local type = getType(property.@targetEntity) />
  	<#elseif property?node_name == "one-to-many" || property?node_name == "many-to-many">
    	<#local type = getType(property.@listType, xml.getAttribute(property.@targetEntity)) />
  	</#if>
	<#compress>
		<#if property?node_name == "one-to-many" || property?node_name == "many-to-many">
			<#local propertyName = getClassName(property.@targetEntity)?uncap_first/>
			<p:dataList <#if id?length != 0>id="${id}"</#if>
				value="${sharp}{${value}}"
				var="${propertyName}"
				type="none"
				<#if styleClass?length != 0>styleClass="${styleClass}"</#if>
				>
				<h:outputText value="${sharp}{${propertyName}}" />
			</p:dataList>
		<#else>
			<#if type == "String" || type == "Character">
				<h:outputText <#if id?length != 0>id="${id}"</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0>styleClass="${styleClass}"</#if>
					/>
			<#elseif type == "Double" || type == "Float">
				<h:outputText
					<#if id?length != 0>id="${id}"</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0>styleClass="${styleClass}"</#if>
					/>
		    <#elseif type == "Integer"
				|| type == "Long"
				|| type == "Byte"
				|| type == "Short">
				<h:outputText
					<#if id?length != 0>id="${id}"</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
		    <#elseif type == "Date">
				<h:outputText
					<#if id?length != 0>id="${id}"</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0>styleClass="${styleClass}"</#if>>
					<f:convertDateTime pattern="${sharp}{localeBean.datePattern}" timeZone="${sharp}{localeBean.timeZone}" />
				</h:outputText>
		  	<#elseif type == "Boolean">
		  		<h:panelGroup <#if id?length != 0>id="${id}"</#if>>
					<h:outputText value="${sharp}{bundle.yes}"<#if styleClass?length != 0> styleClass="${styleClass}"</#if> rendered="${sharp}{${value}}"/>
					<h:outputText value="${sharp}{bundle.no}"<#if styleClass?length != 0> styleClass="${styleClass}"</#if> rendered="${sharp}{not ${value}}"/>
				</h:panelGroup>
			<#else>
				<h:outputText <#if id?length != 0>id="${id}"</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
			</#if>
		</#if>
	</#compress>
</#macro>

<#macro getXhtmlInput entityName property path=entityName id="${property.@name}Value" useRequired=true styleClass="">
	<#local value = "${path}.${property.@name}" />
	<#local type= "" />
	<#if property?node_name == "id" || property?node_name == "column">
    	<#local type=getClassName(property.@type) />
  	<#elseif property?node_name == "many-to-one">
    	<#local type=getType(property.@targetEntity) />
  	<#elseif property?node_name == "one-to-many" || property?node_name == "many-to-many">
    	<#local type=getType(property.@listType, xml.getAttribute(property.@targetEntity)) />
  	</#if>
	<#compress>
		<#if property?node_name == "id">
			<h:outputText <#if id?length != 0>id="${id}"</#if>
				value="${sharp}{${value}}"
				<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
			/>
		<#elseif property?node_name == "many-to-one">
			<p:selectOneMenu <#if id?length != 0>id="${id}"</#if>
				<#if useRequired && xml.getAttribute(property.@nullable) == "false">
				required="true"
				requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
				</#if>
				value="${sharp}{${value}}"
				<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
				>
				<f:selectItem itemLabel="${sharp}{bundle.select}" itemValue="" />
				<t:selectItems value="${sharp}{${property.@name}ListBean.list}" var="${property.@name}" itemLabel="${sharp}{${property.@name}}" itemValue="${sharp}{${property.@name}}" />
			</p:selectOneMenu>
		<#elseif property?node_name == "one-to-many" || property?node_name == "many-to-many">
			<#local propertyName = getClassName(property.@targetEntity)?uncap_first/>
			<p:pickList <#if id?length != 0>id="${id}"</#if>
				<#if useRequired && xml.getAttribute(property.@nullable) == "false">
				required="true"
				requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
				</#if>
				value="${sharp}{${value}}"
				var="${propertyName}"
				itemLabel="${sharp}{${propertyName}}" itemValue="${sharp}{${propertyName}}"
				<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
				/>
		<#else>
			<#if type == "String" || type == "Character">
				<#if xml.getAttribute(property.@length) == "" || xml.getAttribute(property.@length)?number lte 255>
					<p:inputText <#if id?length != 0>id="${id}"</#if>
						<#if useRequired && xml.getAttribute(property.@nullable) == "false">
						required="true"
						requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
						</#if>
						<#if xml.getAttribute(property.@length) != "">
						maxlength="${property.@length}"
						</#if>
						value="${sharp}{${value}}"
						<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
						/>
				<#else>
					<p:inputTextarea <#if id?length != 0>id="${id}"</#if>
						rows="10" cols="50"
						<#if useRequired && xml.getAttribute(property.@nullable) == "false">
						required="true"
						requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
						</#if>
						<#if xml.getAttribute(property.@length) != "">
						maxlength="${property.@length}"
						</#if>
						value="${sharp}{${value}}"
						<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
						/>
				</#if>
			<#elseif type == "Double" || type == "Float">
				<p:inputText <#if id?length != 0>id="${id}"</#if>
					<#if useRequired && xml.getAttribute(property.@nullable) == "false">
					required="true"
					requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
					</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
		    <#elseif type == "Integer"
				|| type == "Long"
				|| type == "Byte"
				|| type == "Short">
				<p:inputText <#if id?length != 0>id="${id}"</#if>
					<#if useRequired && xml.getAttribute(property.@nullable) == "false">
					required="true"
					requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
					</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
		    <#elseif type == "Date">
		    	<p:calendar <#if id?length != 0>id="${id}"</#if>
		    		pattern="${sharp}{localeBean.datePattern}"
		    		timeZone="${sharp}{localeBean.timeZone}"
					autocomplete="off"
					showOn="both"
					<#if useRequired && xml.getAttribute(property.@nullable) == "false">
					required="true"
					requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
					</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
		  	<#elseif type == "Boolean">
		  		<#if useRequired && xml.getAttribute(property.@nullable) == "false">
				<p:selectOneMenu <#if id?length != 0>id="${id}"</#if>
						value="${sharp}{${value}}"
						<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
						>
						<f:selectItem itemLabel="${sharp}{bundle.select}" itemValue="" />
						<f:selectItem itemLabel="${sharp}{bundle.yes}"
							itemValue="${sharp}{true}" />
						<f:selectItem itemLabel="${sharp}{bundle.no}"
							itemValue="${sharp}{false}" />
				</p:selectOneMenu>
				<#else>
			 		<p:selectOneRadio <#if id?length != 0>id="${id}"</#if>
						value="${sharp}{${value}}"
						<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
						>
						<f:selectItem itemLabel="${sharp}{bundle.yes}"
							itemValue="${sharp}{true}" />
						<f:selectItem itemLabel="${sharp}{bundle.no}"
							itemValue="${sharp}{false}" />
					</p:selectOneRadio>
				</#if>
			<#else>
				<p:inputText <#if id?length != 0>id="${id}"</#if>
					<#if useRequired && xml.getAttribute(property.@nullable) == "false">
					required="true"
					requiredMessage="${sharp}{bundle.error_${toUnderscoreCase(entityName)?lower_case}_${toUnderscoreCase(property.@name)?lower_case}_required}"
					</#if>
					value="${sharp}{${value}}"
					<#if styleClass?length != 0> styleClass="${styleClass}"</#if>
					/>
			</#if>
		</#if>
	</#compress>
</#macro>