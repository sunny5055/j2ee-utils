package com.google.code.jee.utils.user.management.dao;

import java.util.List;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Role;

/**
 * The Interface RoleDao.
 */
public interface RoleDao extends GenericDao<Integer, Role> {
    
	String COUNT_BY_CODE = "role.countByCode";
	String FIND_BY_CODE = "role.findByCode";
	String COUNT_BY_USER_ID = "role.countByUserId";
	String FIND_ALL_BY_USER_ID = "role.findAllByUserId";
	String COUNT_BY_RIGHT_ID = "role.countByRightId";
    
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
