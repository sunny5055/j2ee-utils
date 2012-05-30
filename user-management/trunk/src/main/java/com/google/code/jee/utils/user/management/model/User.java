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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jasypt.hibernate4.type.EncryptedStringType;

import com.google.code.jee.utils.dal.dto.AbstractHibernateDto;
import com.google.code.jee.utils.user.management.dao.UserDao;

/**
 * The Class User
 */
@TypeDefs
({
@TypeDef ( 
   name="encryptedString",
   typeClass=EncryptedStringType.class,
   parameters={
     @Parameter(name="encryptorRegisteredName",
     value="strongHibernateStringEncryptor")
   }
  )
})
@Entity
@Table(name = "USE_USER")
@NamedQueries({
    @NamedQuery(name = UserDao.COUNT_BY_LOGIN, query = "select count(*) from User as u where u.login = :login"),
    @NamedQuery(name = UserDao.FIND_BY_LOGIN, query = "from User as u where u.login = :login") })
@SuppressWarnings("serial")
public class User extends AbstractHibernateDto<Integer> {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USE_ID", nullable = false)
	private Integer id;
	
	@Column(name = "USE_FIRST_NAME", nullable = false, length = 50)
	private String firstName;
	
	@Column(name = "USE_LAST_NAME", nullable = false, length = 50)
	private String lastName;
	
	@Column(name = "USE_LOGIN", nullable = false, unique = true, length = 50)
	private String login;
	
	@Column(name = "USE_PASSWORD", nullable = false, length = 50)
	@Type(type="encryptedString")
	private String password;
	
	@Column(name = "USE_MAIL", length = 100)
	private String mail;
	
	@Column(name = "USE_COMPANY", length = 100)
	private String company;
	
	@Column(name = "USE_PHONE1")
	private Integer phone1;
	
	@Column(name = "USE_PHONE2")
	private Integer phone2;
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_USER_ID ", nullable = false)
    private List<Role> roles;
	
    /**
     * Instantiates a new user.
     */
    public User() {
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
     * Getter : return the first name
     * 
     * @return the first name
     */
	public String getFirstName() {
		return firstName;
	}

	/**
     * Setter : affect the first name
     * 
     * @param first name the first name
     */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	 /**
     * Getter : return the last name
     * 
     * @return the last name
     */
	public String getLastName() {
		return lastName;
	}

	/**
     * Setter : affect the last name
     * 
     * @param last name the last name
     */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
     * Getter : return the login
     * 
     * @return the login
     */
	public String getLogin() {
		return login;
	}

	/**
     * Setter : affect the login
     * 
     * @param login the login
     */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
     * Getter : return the password
     * 
     * @return the password
     */
	public String getPassword() {
		return password;
	}

	/**
     * Setter : affect the password
     * 
     * @param password the password
     */
	public void setPassword(String password) {
		this.password = password;
	}
	
	 /**
     * Getter : return the mail
     * 
     * @return the mail
     */
	public String getMail() {
		return mail;
	}

	/**
     * Setter : affect the mail
     * 
     * @param mail the mail
     */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * Getter : return the company
	 * 
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Setter : affect the company
	 * 
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Getter : return the phone1
	 * 
	 * @return the phone1
	 */
	public Integer getPhone1() {
		return phone1;
	}

	/**
	 * Setter : affect the phone1
	 * 
	 * @param phone1 the phone1 to set
	 */
	public void setPhone1(Integer phone1) {
		this.phone1 = phone1;
	}

	/**
	 * Getter : return the phone2
	 * 
	 * @return the phone2
	 */
	public Integer getPhone2() {
		return phone2;
	}

	/**
	 * Setter : affect the phone2
	 * 
	 * @param phone2 the phone2 to set
	 */
	public void setPhone2(Integer phone2) {
		this.phone2 = phone2;
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
     * @param roles the roles
     */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", login=" + login + ", password=" + password
				+ ", mail=" + mail + ", company=" + company + ", phone1="
				+ phone1 + ", phone2=" + phone2 + /*", roles=" + roles +*/ "]";
	}
	
}
