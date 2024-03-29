package com.googlecode.jutils.user.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.googlecode.jutils.dal.dto.AbstractDto;
import com.googlecode.jutils.user.management.dao.RightDao;

/**
 * The Class Right.
 */
@Entity
@Table(name = "RIG_RIGHT")
@NamedQueries({
    @NamedQuery(name = RightDao.COUNT_BY_CODE, query = "select count(*) from Right as rig where rig.code = :code"),
    @NamedQuery(name = RightDao.FIND_BY_CODE, query = "select rig from Right as rig where rig.code = :code"),
    @NamedQuery(name = RightDao.COUNT_BY_ROLE_ID, query = "select count(rig) from Role as r left join r.rights as rig where r.id = :roleId"),
    @NamedQuery(name = RightDao.FIND_ALL_BY_ROLE_ID, query = "select rig from Role as r left join r.rights as rig where r.id = :roleId") })
@SuppressWarnings("serial")
public class Right extends AbstractDto<Integer> {

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
     * @param id the id
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
	 * @param code the code to set
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
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
