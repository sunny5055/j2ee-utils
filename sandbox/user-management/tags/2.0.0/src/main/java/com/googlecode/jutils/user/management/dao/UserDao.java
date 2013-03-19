package com.googlecode.jutils.user.management.dao;

import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.user.management.model.User;

/**
 * The Interface UserDao.
 */
public interface UserDao extends GenericDao<Integer, User> {

	String COUNT_BY_LOGIN = "user.countByLogin";
	String FIND_BY_LOGIN = "user.findByLogin";
	String COUNT_BY_ROLE_ID = "user.countByRoleId";
	
	/**
     * Search the number of elements with the 'login' parameter.
     * 
     * @param login the login
     * @return the number of elements found
     */
	Integer countByLogin(String login);

    /**
     * Search an element by its login.
     * 
     * @param login the login
     * @return the element
     */
	User findByLogin(String login);
	
    /**
     * Count the number of elements corresponding to a specific role.
     * 
     * @param role the role primary key
     * @return the number of elements found
     */
	Integer countByRoleId(Integer roleId);
	
}
