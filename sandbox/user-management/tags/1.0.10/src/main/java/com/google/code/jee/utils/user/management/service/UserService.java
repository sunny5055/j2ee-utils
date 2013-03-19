package com.google.code.jee.utils.user.management.service;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.User;

/**
 * The Interface UserService.
 */
public interface UserService extends GenericService<Integer, User> {

	/**
     * Test the existence of an element with the parameter 'login'.
     * 
     * @param login the login
     * @return true, if success
     */
	boolean existWithLogin(String login);

    /**
     * Search an element by its login.
     * 
     * @param login the login
     * @return the user
     */
	User findByLogin(String login);
	
}
