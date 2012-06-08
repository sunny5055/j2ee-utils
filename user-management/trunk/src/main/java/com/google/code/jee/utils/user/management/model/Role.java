package com.google.code.jee.utils.user.management.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = RoleDao.COUNT_FOR_USER_ID_AND_CODE, query = "select count(*) from User as u left join u.roles as role where u.id = :userId and role.code = :roleCode"),
    @NamedQuery(name = RoleDao.COUNT_FOR_ROLE_ID_AND_RIGHT_CODE, query = "select count(rig) from Role as r left join r.rights as rig where r.id = :roleId and rig.code = :rightCode"),
    @NamedQuery(name = RoleDao.COUNT_RIGHTS_FOR_ROLE_ID, query = "select count(rig) from Role as r left join r.rights as rig where r.id = :roleId"),
    @NamedQuery(name = RoleDao.FIND_ALL_RIGHTS_BY_ROLE_ID, query = "select rig from Role as r left join r.rights as rig where r.id = :roleId") })
@SuppressWarnings("serial")
public class Role extends AbstractHibernateDto<Integer> {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROL_ID", nullable = false)
	private Integer id;
	
	@Column(name = "ROL_CODE", nullable = false, unique = true, length = 50)
	private String code;
	
	@Column(name = "ROL_DESCRIPTION", nullable = false, length = 100)
	private String description;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "RIG_ROLE_ID", nullable = false)
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
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
