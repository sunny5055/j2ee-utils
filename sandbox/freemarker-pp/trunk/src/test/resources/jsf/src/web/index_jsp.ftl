<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa"}>
<#import "/common/common.inc" as util />
<@dropOutputFile />
<#assign projectName = xml["//p:configuration/p:projectName"]>
<@resolveKey map=config key="indexJspFilePath" values=[projectName] assignTo="filePath"/>
<@resolveKey map=config key="indexJspFileName" values=[projectName] assignTo="fileName"/>
<@changeOutputFile name=filePath + "/"+ fileName />
<!DOCTYPE HTML>
<html>
<body>
<jsp:forward page="/faces/xhtml/index.xhtml"/>
</body>
</html>