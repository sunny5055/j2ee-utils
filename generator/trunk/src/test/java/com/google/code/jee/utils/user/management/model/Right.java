package com.google.code.jee.utils.user.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.googlecode.jutils.dal.dto.AbstractHibernateDto;

/**
 * The Class Right.
 */
@Entity
@Table(name = "RIG_RIGHT")
@SuppressWarnings("serial")
public class Right extends AbstractHibernateDto<Integer> {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RIG_ID", nullable = false)
	private Integer id;

	@Column(name = "RIG_CODE", nullable = false, unique = true, length = 50)
	private String code;

	@Column(name = "RIG_DESCRIPTION", nullable = false, length = 255)
	private String description;

	/**
	 * Instantiates a new right.
	 */
	public Right() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Integer getPrimaryKey() {
		return this.id;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void setPrimaryKey(Integer primaryKey) {
		this.id = primaryKey;
	}

	/**
	 * Getter : return the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter : affect the id.
	 * 
	 * @param id
	 *            the id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Getter : return the code.
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Setter : affect the code.
	 * 
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Getter : return the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter : affect the description.
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
