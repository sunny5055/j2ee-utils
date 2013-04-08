package com.googlecode.jutils.test.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.googlecode.jutils.dal.dto.AbstractDto;

@Entity
@Table(name = "ROL_ROLE")
@SuppressWarnings("serial")
public class Role extends AbstractDto<Integer> {
	@Id
	@Column(name = "ROL_ROLE_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "ROL_NAME", nullable = false)
	private String name;

	@Column(name = "ROL_DESCRIPTION", nullable = false)
	private String description;

	public Role() {
		super();
	}

	@Override
	public Integer getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Integer primaryKey) {
		this.id = primaryKey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
