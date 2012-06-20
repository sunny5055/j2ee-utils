package com.google.code.jee.utils.user.management.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.Right;

/**
 * The Interface RightService.
 */
public interface RightService extends GenericService<Integer, Right> {

	/**
     * Test the existence of an element with the parameter 'rightCode'.
     * 
     * @param rightCode the rightCode
     * @return true, if success
     */
	boolean existWithRightCode(String rightCode);

    /**
     * Search an element by its rightCode.
     * 
     * @param rightCode the rightCode
     * @return the right
     */
	Right findByRightCode(String rightCode);
	
	/**
     * Test the existence of a right with the parameter 'code' and its
     * role id.
     * 
     * @param roleId the roleId
     * @param rightCode the right code
     * @return true, if success
     */
    boolean existWithRoleIdAndRightCode(Integer roleId, String rightCode);

    /**
     * Count a role numbers of rights.
     * 
     * @param roleId the roleId
     * @return the number of rights corresponding to the role id
     */
    Integer countRightsForRoleId(Integer roleId);

    /**
     * Search all the role's rights.
     * 
     * @param rolePrimaryKey the role primary key
     * @return the list
     */
    List<Right> findAllRightsByRoleId(Integer roleId);
	
}
