package com.google.code.jee.utils.user.management.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Right;

public interface RightDao extends GenericDao<Integer, Right> {
	
	String COUNT_BY_CODE = "right.countByCode";
	String FIND_BY_CODE = "right.findByCode";
	String COUNT_FOR_ROLE_ID_AND_RIGHT_CODE = "right.countForRoleIdAndRightCode";
	String COUNT_RIGHTS_FOR_ROLE_ID = "right.countRightsForRoleId";
	String FIND_ALL_RIGHTS_BY_ROLE_ID ="right.findAllRightsByRoleId";
	
    /**
     * Search the number of elements with the 'code' parameter.
     * 
     * @param code the code
     * @return the number of element found
     */
	public Integer countByCode(String code);

    /**
     * Search an element by its code.
     * 
     * @param code the code
     * @return the right
     */
	public Right findByCode(String code);
	
	/**
	 * Count the number of rights with a specific code and corresponding to
	 * a specific role
	 * 
	 * @param roleId the role id
	 * @param rightCode the right code
	 * @return the number of rights
	 */
	Integer countForRoleIdAndRightCode(Integer roleId, String rightCode);
	
	/**
	 * Count the number of rights corresponding to a specific role
	 * 
	 * @param roleId the role id
	 * @return the number of rights
	 */
	Integer countRightsForRoleId(Integer roleId);
	
	/**
	 * Finds all rights by role id
	 * 
	 * @param roleId the role id
	 * @return the list
	 */
	List<Right> findAllRightsByRoleId(Integer roleId);

}
