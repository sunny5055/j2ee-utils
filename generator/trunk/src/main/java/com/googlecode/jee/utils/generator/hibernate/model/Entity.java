package com.googlecode.jee.utils.generator.hibernate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.googlecode.jutils.StringUtil;

@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class Entity implements Serializable {
	@XmlAttribute
	private String packageName;

	@XmlAttribute(required = true)
	private String className;

	@XmlAttribute
	private String tableName;

	@XmlAttribute
	private boolean isAbstract;

	@XmlAttribute
	private boolean isInterface;

	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = Property.class)
	private List<Property> properties;

	public Entity() {
		super();
		this.properties = new ArrayList<Property>();
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getFullyQualifiedName() {
		String fullyQualifiedName = null;
		if (!StringUtil.isBlank(packageName)) {
			fullyQualifiedName = packageName;
		}
		if (!StringUtil.isBlank(className)) {
			if (StringUtil.isBlank(fullyQualifiedName)) {
				fullyQualifiedName = "";
			} else {
				fullyQualifiedName += ".";
			}
			fullyQualifiedName += className;
		}
		return fullyQualifiedName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public boolean isInterface() {
		return isInterface;
	}

	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public void addProperty(Property property) {
		if (property != null) {
			properties.add(property);
			property.setEntity(this);
		}
	}
}
