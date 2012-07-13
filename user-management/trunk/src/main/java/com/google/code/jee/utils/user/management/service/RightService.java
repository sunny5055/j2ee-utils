package com.google.code.jee.utils.user.management.service;

import java.util.List;

import com.google.code.jee.utils.dal.service.GenericService;
import com.google.code.jee.utils.user.management.model.Right;

/**
 * The Interface RightService.
 */
public interface RightService extends GenericService<Integer, Right> {

	/**
     * Test the existence of an element with the parameter 'code'.
     * 
     * @param code the code
     * @return true, if success
     */
	Boolean existWithCode(String code);

    /**
     * Search an element by its code.
     * 
     * @param code the code
     * @return the element
     */
	Right findByCode(String rightCode);

	/**
	 * Count the number of elements corresponding to a specific role.
	 * 
	 * @param roleId the role primary key
	 * @return the number of elements found
	 */
	Integer countByRoleId(Integer roleId);
	
	/**
	 * Finds all elements corresponding to a specific role.
	 * 
	 * @param roleId the role primary key
	 * @return the elements
	 */
	List<Right> findAllByRoleId(Integer roleId);
	
}
