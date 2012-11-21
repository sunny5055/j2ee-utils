<#function getClassName type>
  <#local className = "">
  <#if type?index_of(".") gt 0>
    <#local className = "${type?substring(type?last_index_of('.')+1)}">
  <#else>
    <#local className = "${type}">
  </#if>
  <#return className>
</#function>

<#function getType type value="" key="">
  <#local fullType= "">

  <#local typeClassName= getClassName(type)>
  <#if value != "">
    <#local valueClassName = getClassName(value)>
    <#if typeClassName="array">
      <#local fullType = valueClassName + "[]">
    <#elseif key != "">
      <#local keyClassName = getClassName(key)>
      <#local fullType = typeClassName + "<" + keyClassName + ", " + valueClassName + ">">
    <#else>
      <#local fullType = typeClassName + "<" + valueClassName + ">">
    </#if>
  <#else>
    <#local fullType = typeClassName>
  </#if>
  <#return fullType>
</#function>

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


<#function isPrimitive type>
	<#local primitive= "false">
	<#if type == "byte" || type == "short"
		|| type == "int" || type == "long"
		|| type == "float" || type == "double"
		|| type == "char" || type == "boolean">
		<#local primitive= "true">
	</#if>
	<#return primitive>
</#function>

