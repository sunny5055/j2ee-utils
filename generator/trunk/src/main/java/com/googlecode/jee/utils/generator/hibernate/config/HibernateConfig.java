package com.googlecode.jee.utils.generator.hibernate.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.googlecode.jee.utils.generator.hibernate.model.Entity;

@XmlRootElement(name = "hibernate-config")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class HibernateConfig implements Serializable {
	@XmlAttribute(required = true)
	private String projectName;

	@XmlAttribute
	private String version;

	@XmlElement
	private String basePackageName;

	@XmlElementWrapper(name = "entities")
	@XmlElement(name = "entity", type = Entity.class)
	private List<Entity> entities;

	public HibernateConfig() {
		super();
		this.entities = new ArrayList<Entity>();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBasePackageName() {
		return basePackageName;
	}

	public void setBasePackageName(String basePackageName) {
		this.basePackageName = basePackageName;
	}

	public List<Entity> getEntities() {
		return entities;
	}

	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}

	public void addEntity(Entity entity) {
		if (entity != null) {
			entities.add(entity);
		}
	}
}
