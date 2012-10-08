package com.googlecode.jutils.user.management.dao;

import java.util.List;

import com.googlecode.jutils.dal.dao.GenericDao;
import com.googlecode.jutils.user.management.model.Right;

/**
 * The Interface RightDao.
 */
public interface RightDao extends GenericDao<Integer, Right> {
	
	String COUNT_BY_CODE = "right.countByCode";
	String FIND_BY_CODE = "right.findByCode";
	String COUNT_BY_ROLE_ID = "right.countByRoleId";
	String FIND_ALL_BY_ROLE_ID ="right.findAllByRoleId";
	
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
     * @return the element
     */
	public Right findByCode(String code);
	
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
