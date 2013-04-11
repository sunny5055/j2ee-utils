<#ftl ns_prefixes={"p":"http://code.google.com/p/j2ee-utils/schema/project","j":"http://code.google.com/p/j2ee-utils/schema/jpa","g":"http://code.google.com/p/j2ee-utils/schema/gui"}>
<#import "common.ftl" as util>
<#assign varName = className?uncap_first />

<#if controllerPackageName?? && controllerPackageName?length gt 0>
package ${controllerPackageName};
</#if>


<#assign imports = [] />

import java.util.HashMap;
import java.util.List;
import java.util.Map;

<@addTo assignTo="imports" element="org.springframework.beans.factory.annotation.Autowired" />
<@addTo assignTo="imports" element="org.springframework.stereotype.Controller" />
<@addTo assignTo="imports" element="org.springframework.web.bind.annotation.RequestBody" />
<@addTo assignTo="imports" element="org.springframework.web.bind.annotation.RequestMapping" />
<@addTo assignTo="imports" element="org.springframework.web.bind.annotation.RequestMethod" />
<@addTo assignTo="imports" element="org.springframework.web.bind.annotation.ResponseBody" />

<@addTo assignTo="imports" element="${packageName}.${className}" />
<@addTo assignTo="imports" element="${servicePackageName}.${serviceName}" />

${getImports(false, controllerPackageName, imports)}

@Controller
@RequestMapping(value = "/${varName}")
public class ${controllerName} {
	@Autowired
	private ${serviceName} service;

	@RequestMapping(value = "view.action", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Map<String, Object> view() {
		final Map<String, Object> model = new HashMap<String, Object>();

		List<${className}> ${varName}s = null;
		try {
			${varName}s = service.findAll();
			model.put("total", ${varName}s.size());
			model.put("data", ${varName}s);
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to retrieve ${varName}s : " + e.getMessage());
		}

		return model;
	}

	@RequestMapping(value = "create.action", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> create(@RequestBody ${className} ${varName}) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			service.create(${varName});
			model.put("data", ${varName});
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to create the ${varName} : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "update.action", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> update(@RequestBody ${className} ${varName}) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			service.update(${varName});
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to update the ${varName} : " + e.getMessage());
		}
		return model;
	}

	@RequestMapping(value = "delete.action", method = RequestMethod.POST, consumes = "application/json")
	@ResponseBody
	public Map<String, Object> delete(@RequestBody ${className} ${varName}) {
		final Map<String, Object> model = new HashMap<String, Object>();

		try {
			service.delete(${varName});
			model.put("success", true);
		} catch (final Exception e) {
			model.put("success", false);
			model.put("errorMsg", "Unable to delete the ${varName} : " + e.getMessage());
		}
		return model;
	}
}
