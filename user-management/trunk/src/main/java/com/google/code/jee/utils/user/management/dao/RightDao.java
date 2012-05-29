package com.google.code.jee.utils.user.management.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Right;

public interface RightDao extends GenericDao<Integer, Right> {
	
	public final String COUNT_BY_NAME = "right.countByName";
	public final String FIND_BY_NAME = "right.findByName";
	public final String COUNT_FOR_ROLE_ID = "right.countForRoleId";
	public final String FIND_ALL_BY_ROLE_ID = "right.findAllByRoleId";
	public final String COUNT_FOR_ROLE_ID_AND_NAME = "right.countForRoleIdAndName";
	public final String FIND_BY_ROLE_ID_AND_NAME = "right.findByRoleIdAndName";
	
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
     * @return the right
     */
	public Right findByName(String name);

    /**
     * Count the number of rights of a specific role
     * 
     * @param roleId the role id
     * @return the number of rights
     */
	public Integer countForRoleId(Integer roleId);

    /**
     * Finds all rights by roleId.
     * 
     * @param roleId the role primary key
     * @return the list
     */
	public List<Right> findAllByRoleId(Integer roleId);

    /**
     * Count the number of rights with a specific name and corresponding to
     * a specific role
     * 
     * @param roleId the roleId
     * @param rightName the right name
     * @return the number of rights
     */
	public Integer countForRoleIdAndName(Integer roleId, String rightName);

    /**
     * Finds the right.
     * 
     * @param roleId the role primary key
     * @param rightName the right name
     * @return the right
     */
	public Right findByRoleIdAndName(Integer roleId, String rightName);

}
