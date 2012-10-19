package com.googlecode.jee.utils.generator.hibernate.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("serial")
public class Property implements Serializable {
	@XmlAttribute(required = true)
	private String name;

	// @XmlElement(required = true)
	@XmlElementRefs({ @XmlElementRef(name = "value", type = SimpleValue.class) })
	private Value value;

	@XmlTransient
	private Entity entity;

	public Property() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
}
