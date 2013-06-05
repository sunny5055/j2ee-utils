<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>

<#function getManyToOneAnnotation entity property>
	<#local jpaAnnotation= "">
	<#if property?node_name = "many-to-one">
    	<#local jpaAnnotation= "@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY">
		<#if xml.getAttribute(property.@nullable) == "false">
    		<#local jpaAnnotation= jpaAnnotation + ", optional = false">
		</#if>
      	<#local jpaAnnotation= jpaAnnotation + ")">
      	<#local jpaAnnotation= jpaAnnotation + " @JoinColumn(name = \"${getColumnName(entity.@columnPrefix, property)}\")">
  </#if>
  <#return jpaAnnotation>
</#function>

