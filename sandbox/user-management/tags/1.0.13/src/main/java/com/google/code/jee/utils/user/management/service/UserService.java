package com.google.code.jee.utils.user.management.service;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.User;

/**
 * The Interface UserService.
 */
public interface UserService extends GenericService<Integer, User> {

	/**
     * Test the existence of an element with the 'login' parameter.
     * 
     * @param login the login
     * @return true if success
     */
	Boolean existWithLogin(String login);

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
