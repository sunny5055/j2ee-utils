package com.googlecode.jutils.bundle.jpa.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.googlecode.jutils.bundle.jpa.dao.LabelDao;
import com.googlecode.jutils.dal.dto.AbstractDto;

/**
 * The Class Parameter.
 */
@Entity
@Table(name = "LAB_LABEL")
@NamedQueries({ @NamedQuery(name = LabelDao.COUNT_FOR_KEY, query = "select count(*) from Label as l where l.primaryKey.key = :key"),
		@NamedQuery(name = LabelDao.FIND_ALL_BY_KEY, query = "from Label as l where l.primaryKey.key = :key"),
		@NamedQuery(name = LabelDao.COUNT_FOR_LANGUAGE, query = "select count(*) from Label as l where l.primaryKey.language = :language"),
		@NamedQuery(name = LabelDao.FIND_ALL_BY_LANGUAGE, query = "from Label as l where l.primaryKey.language = :language") })
@SuppressWarnings("serial")
public class Label extends AbstractDto<LabelId> {
	@EmbeddedId
	private LabelId primaryKey;

	@Column(name = "LAB_VALUE", length = 250, nullable = false)
	private String value;

	/**
	 * The Constructor.
	 */
	public Label() {
		super();
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public LabelId getPrimaryKey() {
		return this.primaryKey;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void setPrimaryKey(LabelId primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value
	 *            the value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
