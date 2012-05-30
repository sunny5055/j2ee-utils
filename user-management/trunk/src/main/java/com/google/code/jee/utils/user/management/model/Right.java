package com.google.code.jee.utils.user.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;
import com.google.code.jee.utils.user.management.dao.RightDao;

/**
 * The Class Right.
 */
@Entity
@Table(name = "RIG_RIGHT")
@NamedQueries({
    @NamedQuery(name = RightDao.COUNT_BY_NAME, query = "select count(*) from Right as rig where rig.name = :name"),
    @NamedQuery(name = RightDao.FIND_BY_NAME, query = "from Right as rig where rig.name = :name"),
    @NamedQuery(name = RightDao.COUNT_FOR_ROLE_ID, query = "select count(*) from Role as r where r.id = :id"),
    @NamedQuery(name = RightDao.FIND_ALL_BY_ROLE_ID, query = "select r from Role as role left join role.rights as r where role.id = :roleId"),
    @NamedQuery(name = RightDao.COUNT_FOR_ROLE_ID_AND_NAME, query = "select count(*) from Role as role left join role.rights as r where role.id = :roleId and r.name = :rightName"),
    @NamedQuery(name = RightDao.FIND_BY_ROLE_ID_AND_NAME, query = "select r from Role as role left join role.rights as r where role.id = :roleId and r.name = :rightName") })
@SuppressWarnings("serial")
public class Right extends AbstractHibernateDto<Integer> {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "RIG_ID", nullable = false)
	private Integer id;
	
	@Column(name = "RIG_NAME", nullable = false, unique = true, length = 50)
	private String name;
	
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
     * Getter : return the id
     * 
     * @return the id
     */
	public Integer getId() {
		return id;
	}

	/**
     * Setter : affect the id
     * 
     * @param id the id
     */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
     * Getter : return the name
     * 
     * @return the name
     */
	public String getName() {
		return name;
	}

	/**
     * Setter : affect the name
     * 
     * @param name the name
     */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Right [id=" + id + ", name=" + name + "]";
	}
	
}
