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
import com.google.code.jee.utils.user.management.dao.RoleDao;

/**
 * The Class Role.
 */
@Entity
@Table(name = "ROL_ROLE")
@NamedQueries({
    @NamedQuery(name = RoleDao.COUNT_BY_CODE, query = "select count(*) from Role as r where r.code = :code"),
    @NamedQuery(name = RoleDao.FIND_BY_CODE, query = "from Role as r where r.code = :code"),
    @NamedQuery(name = RoleDao.COUNT_FOR_USER_ID, query = "select count(role) from User as u left join u.roles as role where u.id = :userId"),
    @NamedQuery(name = RoleDao.FIND_ALL_BY_USER_ID, query = "select role from User as u left join u.roles as role where u.id = :userId"),
    @NamedQuery(name = RoleDao.COUNT_FOR_USER_ID_AND_CODE, query = "select count(*) from User as u left join u.roles as role where u.id = :userId and role.code = :roleCode") })
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
    @JoinTable(name="USER_ROLE",
    		   joinColumns=@JoinColumn(name="ROL_ID"),
    		   inverseJoinColumns=@JoinColumn(name="USE_ID"))
	private List<User> users;
	
	@ManyToMany(fetch = FetchType.LAZY)   
    @JoinTable(name="ROLE_RIG",
    		   joinColumns=@JoinColumn(name="ROL_ID"),
    		   inverseJoinColumns=@JoinColumn(name="RIG_ID"))
    private List<Right> rights;
	
	/**
     * Instantiates a new role.
     */
    public Role() {
        super();
        this.users = new ArrayList<User>();
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
     * @param code the code
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
	 * Getter : return the users
	 * 
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Setter : affect the users
	 * 
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
     * Getter : return the rights
     * 
     * @return the rights
     */
	public List<Right> getRights() {
		return rights;
	}

	/**
     * Setter : affect the rights
     * 
     * @param rights the rights
     */
	public void setRights(List<Right> rights) {
		this.rights = rights;
	}
	
}
