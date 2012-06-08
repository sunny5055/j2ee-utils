package com.google.code.jee.utils.user.management.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;

public interface RoleDao extends GenericDao<Integer, Role> {
    
	String COUNT_BY_CODE = "role.countByCode";
	String FIND_BY_CODE = "role.findByCode";
	String COUNT_FOR_USER_ID = "role.countForUserId";
	String FIND_ALL_BY_USER_ID = "role.findAllByUserId";
	String COUNT_FOR_USER_ID_AND_CODE = "role.countForUserIdAndCode";
	String COUNT_FOR_ROLE_ID_AND_RIGHT_CODE = "role.countForRoleIdAndRightCode";
	String COUNT_RIGHTS_FOR_ROLE_ID = "role.countRightsForRoleId";
	String FIND_ALL_RIGHTS_BY_ROLE_ID ="role.findAllRightsByRoleId";
    
    /**
     * Search the number of elements with the 'code' parameter.
     * 
     * @param code the code
     * @return the number of element found
     */
	Integer countByCode(String code);

    /**
     * Search an element by its code.
     * 
     * @param code the code
     * @return the user
     */
	Role findByCode(String code);

    /**
     * Count the number of roles of a specific user
     * 
     * @param userId the user id
     * @return the number of roles
     */
	Integer countForUserId(Integer userId);

    /**
     * Finds all roles by user id.
     * 
     * @param userId the user primary key
     * @return the list
     */
	List<Role> findAllByUserId(Integer userId);

    /**
     * Count the number of roles with a specific code and corresponding to
     * a specific user
     * 
     * @param userId the user id
     * @param roleCode the role code
     * @return the number of roles
     */
	Integer countForUserIdAndCode(Integer userId, String roleCode);
	
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
