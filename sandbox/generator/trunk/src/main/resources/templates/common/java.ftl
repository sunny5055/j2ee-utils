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

<#function convertFromString type varName>
	<#local converted= "">

	<#local typeClassName= getClassName(type)>
	<#if typeClassName == "String">
  		<#local converted = varName>
	<#elseif typeClassName == "Double"  || type == "double">
		<#local converted = "Double.valueOf(${varName})">
	<#elseif typeClassName == "Float" || type == "float">
  		<#local converted = "Float.valueOf(${varName})">
    <#elseif typeClassName == "Integer" || type == "int">
    	<#local converted = "Integer.valueOf(${varName})">
    <#elseif typeClassName == "Long" || type == "long">
    	<#local converted = "Long.valueOf(${varName})">
    <#elseif typeClassName == "Byte" || type == "byte">
    	<#local converted = "Byte.valueOf(${varName})">
    <#elseif typeClassName == "Short" || type == "short">
  		<#local converted = "Short.valueOf(${varName})">
    <#elseif typeClassName == "Date">
  		<#local converted = "DateUtil.parseDate(${varName}, \"dd/MM/yyyy\")">
  	<#elseif typeClassName == "Boolean" || type == "boolean">
  		<#local converted = "BooleanUtil.toBoolean(${varName})">
	</#if>
	<#return converted>
</#function>

<#function getRandomValue type length="10">
	<#local randomValue= "">

	<#local typeClassName= getClassName(type)>
	<#if typeClassName == "String">
  		<#local randomValue = "RandomStringUtils.randomAlphabetic(${length})">
	<#elseif typeClassName == "Double"  || type == "double">
		<#local randomValue = "new Random().nextDouble()">
	<#elseif typeClassName == "Float" || type == "float">
  		<#local randomValue = "new Random().nextFloat()">
    <#elseif typeClassName == "Integer" || type == "int">
    	<#if length == "">
    		<#local randomValue = "new Random().nextInt(1000)">
    	<#else>
    		<#local randomValue = "new Random().nextInt(${length})">
    	</#if>
    <#elseif typeClassName == "Long" || type == "long">
    	<#local randomValue = "new Random().nextLong()">
  	<#elseif typeClassName == "Boolean" || type == "boolean">
  		<#local randomValue = "new Random().nextBoolean()">
	</#if>
	<#return randomValue>
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


<#function getConstant name class="">
	<#local constant = ""/>
	<#local varName = name/>
	<#if !(varName?matches("[A-Z_0-9]+"))>
  		<#local varName = toUnderscoreCase(varName)/>
  	</#if>
  	<#if class?length gt 0>
  		<#local constant = "${class}.${varName}" />
  	<#else>
  		<#local constant = "${varName}" />
  	</#if>
  	<#return constant>
</#function>


<#macro getInterfaceProperty type name value>
  <#local varName = getConstant(name) />
  <#if type == "String">
  ${type} ${varName} =  "${value}";
  <#else>
  ${type} ${varName} =  ${value};
  </#if>
</#macro>


<#macro getProperty visibility type name>
  ${visibility} ${type} ${name};
</#macro>


<#macro initProperties type name value="" key="">
	<@assign name=name value=resolveDefaults(type, value, key) />
</#macro>


<#macro interfaceGetter type name methodName="get${name?cap_first}">
	${type} ${methodName}();
</#macro>


<#macro getter type name methodName="get${name?cap_first}">
	public ${type} ${methodName}() {
		return ${name};
	}
</#macro>


<#macro interfaceSetter type name methodName="set${name?cap_first}" argName=name>
	void ${methodName}(${type} ${argName});
</#macro>


<#macro setter type name methodName="set${name?cap_first}" argName=name>
	public void ${methodName}(${type} ${argName}) {
		<@assign name=name value=argName />
	}
</#macro>


<#macro assign name value>
	this.${name} = ${value};
</#macro>

<#macro interfaceAddListMethod type name methodName="add${name?substring(0, name?length-1)?cap_first}" argName=name?substring(0, name?length-1)>
    void ${methodName}(${type} ${argName});
</#macro>


<#macro addListMethod type name methodName="add${name?substring(0, name?length-1)?cap_first}" argName=name?substring(0, name?length-1)>
    public void ${methodName}(${type} ${argName}) {
      if (${checkNotNull(type, argName)}) {
        this.${name}.add(${argName});
      }
    }
</#macro>


<#macro interfaceAddMapMethod name keyType valueType keyName="key" valueName=name?substring(0, name?length-1) methodName="add${name?substring(0, name?length-1)?cap_first}">
    void ${methodName}(${keyType} ${keyName}, ${valueType} ${valueName});
</#macro>


<#macro addMapMethod name keyType valueType keyName="key" valueName=name?substring(0, name?length-1) methodName="add${name?substring(0, name?length-1)?cap_first}">
    public void ${methodName}(${keyType} ${keyName}, ${valueType} ${valueName}) {
      if(${checkNotNull(keyType, keyName)} && ${checkNotNull(valueType, valueName)}) {
        this.${name}.put(${keyName}, ${valueName});
      }
    }
</#macro>


<#macro interfaceRemoveListMethod type name methodName="remove${name?substring(0, name?length-1)?cap_first}" argName=name?substring(0, name?length-1)>
    void ${methodName}(${type} ${argName});
</#macro>


<#macro removeListMethod type name methodName="remove${name?substring(0, name?length-1)?cap_first}" argName=name?substring(0, name?length-1)>
    public void ${methodName}(${type} ${argName}) {
      if (${checkNotNull(type, argName)}) {
        this.${name}.remove(${argName});
      }
    }
</#macro>


<#macro interfaceRemoveMapMethod name keyType keyName="key" methodName="remove${name?substring(0, name?length-1)?cap_first}">
    void ${methodName}(${keyType} ${keyName});
</#macro>


<#macro removeMapMethod name keyType keyName="key" methodName="remove${name?substring(0, name?length-1)?cap_first}">
    public void ${methodName}(${keyType} ${keyName}) {
      if(${checkNotNull(keyType, keyName)}) {
        this.${name}.remove(${keyName});
      }
    }
</#macro>


<#macro constructor visibility className parameters="" content="">
  ${visibility} ${className}(<@compress single_line=true>${parameters}</@compress>) {
  <#compress>
  <#if content != "">
    ${content}
  <#else>
  	<#local content><#nested/></#local>
  	<#if content != "">
    	${content}
  	<#else>
  		//TODO to complete
	</#if>
  </#if>
  </#compress>
  }
</#macro>


<#macro interfaceOperation returnType methodName modifiers="" parameters="">
  ${modifiers} ${returnType} ${methodName}(<@compress single_line=true>${parameters}</@compress>);
</#macro>

<#macro operation visibility returnType methodName modifiers="" parameters="" content="">
  ${visibility} ${modifiers} ${returnType} ${methodName}(<@compress single_line=true>${parameters}</@compress>) {
  <#compress>
  <#if content != "">
    ${content}
  <#else>
  	<#local content><#nested/></#local>
  	<#if content != "">
    	${content}
  	<#else>
  		//TODO to complete
	</#if>
  </#if>
  </#compress>

  }
</#macro>



