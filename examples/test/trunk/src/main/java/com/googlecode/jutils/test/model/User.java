package com.googlecode.jutils.test.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.googlecode.jutils.dal.dto.AbstractDto;

@Entity
@Table(name = "USR_USER")
@SuppressWarnings("serial")
public class User extends AbstractDto<Integer> {
	@Id
	@Column(name = "USR_USER_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "USR_NAME", nullable = false)
	private String name;

	@Column(name = "USR_LASTNAME", nullable = false)
	private String lastname;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "URO_USER_ROLE", joinColumns = @JoinColumn(name = "URO_USER_ID"), inverseJoinColumns = @JoinColumn(name = "URO_ROLE_ID"))
	private List<Role> roles;

	public User() {
		super();
		this.roles = new ArrayList<Role>();
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

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
