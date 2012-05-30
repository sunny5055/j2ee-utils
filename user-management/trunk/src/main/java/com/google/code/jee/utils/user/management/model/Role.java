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
    @NamedQuery(name = RoleDao.COUNT_BY_NAME, query = "select count(*) from Role as r where r.name = :name"),
    @NamedQuery(name = RoleDao.FIND_BY_NAME, query = "from Role as r where r.name = :name"),
    @NamedQuery(name = RoleDao.COUNT_FOR_USER_ID, query = "select count(*) from User as u where u.id = :id"),
    @NamedQuery(name = RoleDao.FIND_ALL_BY_USER_ID, query = "select role from User as u left join u.roles as role where u.id = :userId"),
    @NamedQuery(name = RoleDao.COUNT_FOR_USER_ID_AND_NAME, query = "select count(*) from User as u left join u.roles as role where u.id = :userId and role.name = :roleName"),
    @NamedQuery(name = RoleDao.FIND_BY_USER_ID_AND_NAME, query = "select role from User as u left join u.roles as role where u.id = :userId and role.name = :roleName") })
@SuppressWarnings("serial")
public class Role extends AbstractHibernateDto<Integer> {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROL_ID", nullable = false)
	private Integer id;
	
	@Column(name = "ROL_NAME", nullable = false, unique = true, length = 50)
	private String name;
	
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + /*", rights=" + rights + */"]";
	}
	
}
