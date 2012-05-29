package com.google.code.jee.utils.user.management.dao;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.User;

public interface UserDao extends GenericDao<Integer, User> {

	public final String COUNT_BY_LOGIN = "user.countByLogin";
	public final String FIND_BY_LOGIN = "user.findByLogin";
	
	/**
     * Search the number of elements with the 'login' parameter.
     * 
     * @param login the login
     * @return the number of elements found
     */
	public Integer countByLogin(String login);

    /**
     * Search an element by its login.
     * 
     * @param login the login
     * @return the user
     */
	public User findByLogin(String login);
	
}
