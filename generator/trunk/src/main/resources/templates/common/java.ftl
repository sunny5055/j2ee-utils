<#function resolveDefaults type value="" key="">
	<#local defaults= "">

	<#local typeClassName= getClassName(type)>
	<#if value != "">
		<#local valueClassName = getClassName(value)>
		<#if type == "java.util.Collection" || type == "java.util.List" || typeClassName == "Collection" || typeClassName == "List">
			<#local defaults = "new ArrayList<" + valueClassName + ">()">
		<#elseif type == "java.util.Set"  || typeClassName == "Set">
			<#local defaults = "new HashSet<" + valueClassName + ">()">
		<#elseif key != "" && type == "java.util.Map"  || typeClassName == "Map">
			<#local keyClassName = getClassName(key)>
  			<#local defaults = "new HashMap<" + keyClassName + ", " + valueClassName + ">()">
  		</#if>
	<#else>
		<#if typeClassName == "String">
      		<#local defaults = "null">
		<#elseif typeClassName == "Double"  || type == "double"
			|| typeClassName == "Float" || type == "float">
      		<#local defaults = "0.0">
        <#elseif typeClassName == "Integer" || type == "int"
			|| typeClassName == "Long" || type == "long"
			|| typeClassName == "Byte" || type == "byte"
			|| typeClassName == "Short" || type == "short">
      		<#local defaults = "0">
        <#elseif typeClassName == "Date">
      		<#local defaults = "new Date()">
  		<#elseif typeClassName == "Character" || type == "char">
  			<#local defaults = "''">
      	<#elseif typeClassName == "Boolean" || type == "boolean">
      		<#local defaults = "false">
    	<#else>
      		<#local defaults = "null">
    	</#if>
	</#if>
	<#return defaults>
</#function>


<#function checkNotNull type name>
	<#local checkNotNull= "">

	<#local typeClassName= getClassName(type)>
	<#if type == "array">
		<#local checkNotNull = "!ArrayUtil.isEmpty(${name})">
	<#elseif type == "java.util.Collection" || typeClassName == "Collection"
		|| type == "java.util.List"  || typeClassName == "List"
		|| type == "java.util.Set"  || typeClassName == "Set">
		<#local checkNotNull = "!CollectionUtil.isEmpty(${name})">
	<#elseif type == "java.util.Map"  || typeClassName == "Map">
		<#local checkNotNull = "!MapUtil.isEmpty(${name})">
	<#elseif type == "String">
  		<#local checkNotNull = "!StringUtil.isBlank(${name})">
	<#else>
  		<#local checkNotNull = "${name} != null">
	</#if>
	<#return checkNotNull>
</#function>


<#macro getter type name>
	public ${type} get${name?cap_first}() {
		return ${name};
	}
</#macro>


<#macro setter type name>
	public void set${name?cap_first}(${type} ${name}) {
		this.${name} = ${name};
	}
</#macro>
