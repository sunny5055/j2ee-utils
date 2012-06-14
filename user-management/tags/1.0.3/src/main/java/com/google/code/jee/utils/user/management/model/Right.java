package com.google.code.jee.utils.user.management.model;

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

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;
import com.google.code.jee.utils.user.management.dao.RightDao;

/**
 * The Class Right.
 */
@Entity
@Table(name = "RIG_RIGHT")
@NamedQueries({
    @NamedQuery(name = RightDao.COUNT_BY_CODE, query = "select count(*) from Right as rig where rig.code = :code"),
    @NamedQuery(name = RightDao.FIND_BY_CODE, query = "from Right as rig where rig.code = :code"),
    @NamedQuery(name = RightDao.COUNT_FOR_ROLE_ID_AND_RIGHT_CODE, query = "select count(rig) from Role as r left join r.rights as rig where r.id = :roleId and rig.code = :rightCode"),
    @NamedQuery(name = RightDao.COUNT_RIGHTS_FOR_ROLE_ID, query = "select count(rig) from Role as r left join r.rights as rig where r.id = :roleId"),
    @NamedQuery(name = RightDao.FIND_ALL_RIGHTS_BY_ROLE_ID, query = "select rig from Role as r left join r.rights as rig where r.id = :roleId") })
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
	
	@ManyToMany(fetch = FetchType.LAZY)   
    @JoinTable(name="ROLE_RIG",
    		   joinColumns=@JoinColumn(name="RIG_ID"),
    		   inverseJoinColumns=@JoinColumn(name="ROL_ID"))
	private List<Role> roles;
	
	/**
     * Instantiates a new right.
     */
    public Right() {
        super();
        this.roles = new ArrayList<Role>();
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
	 * Getter : return the code
	 * 
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Setter : affect the code
	 * 
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Getter : return the description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter : affect the description
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter : return the roles
	 * 
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Setter : affect the roles
	 * 
	 * @param roles the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}
