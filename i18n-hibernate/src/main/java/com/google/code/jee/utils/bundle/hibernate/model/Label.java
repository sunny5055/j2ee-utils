package com.google.code.jee.utils.bundle.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;
import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;

/**
 * The Class Parameter.
 */
@Entity
@Table(name = "LAB_LABEL")
@NamedQueries({
        @NamedQuery(name = LabelDao.COUNT_BY_KEY, query = "select count(*) from Label as l where l.key = :key"),
        @NamedQuery(name = LabelDao.FIND_BY_KEY, query = "from Label as l where l.key = :key"),
        @NamedQuery(name = LabelDao.COUNT_BY_LANGUAGE, query = "select count(*) from Label as l where l.language = :language"),
        @NamedQuery(name = LabelDao.FIND_BY_LANGUAGE, query = "from Label as l where l.language = :language") })
@SuppressWarnings("serial")
public class Label extends AbstractHibernateDto<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LAB_ID", nullable = false)
    private Integer id;

    @Column(name = "LAB_KEY", length = 100, nullable = false, unique = true)
    private String key;

    @Column(name = "LAB_VALUE", length = 500, nullable = false)
    private String value;

    @Column(name = "LAB_LANGUAGE", length = 2, nullable = false, unique = true)
    private String language;

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
     * Gets the id.
     * 
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
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
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the language.
     * 
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language.
     * 
     * @param language the language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
