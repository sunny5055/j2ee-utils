package com.google.code.jee.utils.parameter.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;

/**
 * The Class Parameter.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "PAR_PARAMETER")
@SuppressWarnings("serial")
public abstract class AbstractParameter<V> extends AbstractHibernateDto<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PAR_ID", nullable = false)
    protected Integer id;

    @Column(name = "PAR_NAME", length = 50, nullable = false, unique = true)
    protected String name;

    @Column(name = "PAR_TYPE", length = 50, nullable = false)
    protected String type;

    /**
     * Instantiates a new parameter.
     */
    public AbstractParameter() {
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
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type the new type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter : return the value
     * 
     * @return the value
     */
    public abstract V getValue();

    /**
     * Setter : affect the value
     * 
     * @param value the value
     */
    public abstract void setValue(V value);
}
