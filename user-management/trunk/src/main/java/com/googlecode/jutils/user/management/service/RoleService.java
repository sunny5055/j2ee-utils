package com.googlecode.jutils.user.management.service;

import java.util.List;

import com.googlecode.jutils.dal.service.GenericService;
import com.googlecode.jutils.user.management.model.Role;

/**
 * The Interface RoleService.
 */
public interface RoleService extends GenericService<Integer, Role> {
	
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
	Role findByCode(String code);

    /**
     * Count the number of elements corresponding to a specific user.
     * 
     * @param userId the user primary key
     * @return the number of elements found
     */
	Integer countByUserId(Integer userId);

    /**
     * Finds all elements corresponding to a specific user.
     * 
     * @param userId the user primary key
     * @return the elements
     */
	List<Role> findAllByUserId(Integer userId);
	
    /**
     * Count the number of elements corresponding to a specific right.
     * 
     * @param right the right primary key
     * @return the number of elements found
     */
	Integer countByRightId(Integer rightId);

}
