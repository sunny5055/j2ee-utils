package com.google.code.jee.utils.user.management.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Role;

public interface RoleDao extends GenericDao<Integer, Role> {
    
	public final String COUNT_BY_NAME = "role.countByName";
	public final String FIND_BY_NAME = "role.findByName";
	public final String COUNT_FOR_USER_ID = "role.countForUserId";
	public final String FIND_ALL_BY_USER_ID = "role.findAllByUserId";
	public final String COUNT_FOR_USER_ID_AND_NAME = "role.countForUserIdAndName";
	public final String FIND_BY_USER_ID_AND_NAME = "role.findByUserIdAndName";
    
    /**
     * Search the number of elements with the 'name' parameter.
     * 
     * @param name the name
     * @return the number of element found
     */
	public Integer countByName(String name);

    /**
     * Search an element by its name.
     * 
     * @param name the name
     * @return the user
     */
	public Role findByName(String name);

    /**
     * Count the number of roles of a specific user
     * 
     * @param userId the user id
     * @return the number of roles
     */
	public Integer countForUserId(Integer userId);

    /**
     * Finds all roles by user id.
     * 
     * @param userId the user primary key
     * @return the list
     */
	public List<Role> findAllByUserId(Integer userId);

    /**
     * Count the number of roles with a specific name and corresponding to
     * a specific user
     * 
     * @param userId the userId
     * @param roleName the role name
     * @return the number of roles
     */
	public Integer countForUserIdAndName(Integer userId, String roleName);

    /**
     * Finds the role.
     * 
     * @param userId the user primary key
     * @param roleName the role name
     * @return the role
     */
	public Role findByUserIdAndName(Integer userId, String roleName);

}
