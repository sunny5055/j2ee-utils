package com.googlecode.jutils.user.management.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.googlecode.jutils.dal.dto.AbstractHibernateDto;
import com.googlecode.jutils.user.management.dao.RoleDao;

/**
 * The Class Role.
 */
@Entity
@Table(name = "ROL_ROLE")
@NamedQueries({
    @NamedQuery(name = RoleDao.COUNT_BY_CODE, query = "select count(*) from Role as r where r.code = :code"),
    @NamedQuery(name = RoleDao.FIND_BY_CODE, query = "select r from Role as r where r.code = :code"),
    @NamedQuery(name = RoleDao.COUNT_BY_USER_ID, query = "select count(r) from User as u left join u.roles as r where u.id = :userId"),
    @NamedQuery(name = RoleDao.FIND_ALL_BY_USER_ID, query = "select r from User as u left join u.roles as r where u.id = :userId"),
    @NamedQuery(name = RoleDao.COUNT_BY_RIGHT_ID, query = "select count(r) from Role as r left join r.rights as rig where rig.id = :rightId") })
@SuppressWarnings("serial")
public class Role extends AbstractHibernateDto<Integer> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROL_ID", nullable = false)
	private Integer id;
	
	@Column(name = "ROL_CODE", nullable = false, unique = true, length = 50)
	private String code;
	
	@Column(name = "ROL_DESCRIPTION", nullable = false, length = 255)
	private String description;
	
	@ManyToMany(fetch = FetchType.LAZY)   
    @JoinTable(name="RRI_ROLE_RIGHTS",
    		   joinColumns=@JoinColumn(name="RRI_ROLE_ID"),
    		   inverseJoinColumns=@JoinColumn(name="RRI_RIGHT_ID"))
    private List<Right> rights;
	
	/**
     * Instantiates a new role.
     */
    public Role() {
        super();
        this.rights = new ArrayList<Right>();
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
     * @param code the code
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

	/**
     * Getter : return the rights.
     * 
     * @return the rights
     */
	public List<Right> getRights() {
		return rights;
	}

	/**
     * Setter : affect the rights.
     * 
     * @param rights the rights
     */
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
	
}
