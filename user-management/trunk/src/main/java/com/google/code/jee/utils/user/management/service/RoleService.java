package com.google.code.jee.utils.user.management.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.Role;

/**
 * The Interface RoleService.
 */
public interface RoleService extends GenericService<Integer, Role> {
	
	/**
     * Test the existence of an element with the parameter 'roleCode'.
     * 
     * @param roleCode the roleCode
     * @return true, if success
     */
	boolean existWithRoleCode(String roleCode);

    /**
     * Search an element by its roleCode.
     * 
     * @param roleCode the roleCode
     * @return the role
     */
	Role findByRoleCode(String roleCode);
	
	/**
     * Test the existence of a role with the parameter 'code' and its
     * user id.
     * 
     * @param userId the userId
     * @param roleCode the role code
     * @return true, if success
     */
	boolean existWithUserIdAndRoleCode(Integer userId, String roleCode);

    /**
     * Count an user numbers of roles.
     * 
     * @param userId the userId
     * @return the number of roles corresponding to the user id
     */
	Integer countForUserId(Integer userId);

    /**
     * Search all the user's roles.
     * 
     * @param userPrimaryKey the user primary key
     * @return the list
     */
    List<Role> findAllByUserId(Integer userId);

}
