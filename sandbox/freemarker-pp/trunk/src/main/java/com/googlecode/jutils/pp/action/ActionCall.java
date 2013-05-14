package com.googlecode.jutils.pp.action;

import java.util.HashMap;
import java.util.Map;

import com.googlecode.jutils.StringUtil;

public class ActionCall {
	private String name;
	private String applyTo;
	private Map<String, Object> parameters;

	public ActionCall() {
		super();
		this.parameters = new HashMap<String, Object>();
	}

	public ActionCall(String name, String applyTo) {
		this();
		this.name = name;
		this.applyTo = applyTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplyTo() {
		return applyTo;
	}

	public void setApplyTo(String applyTo) {
		this.applyTo = applyTo;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public boolean isFileRegex() {
		boolean file = false;
		if (!StringUtil.isBlank(applyTo)) {
			file = applyTo.matches("^[^\\.]+\\.[a-zA-Z]+$");
		}
		return file;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ActionCall [name=");
		builder.append(name);
		builder.append(", applyTo=");
		builder.append(applyTo);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append("]");
		return builder.toString();
	}
}
