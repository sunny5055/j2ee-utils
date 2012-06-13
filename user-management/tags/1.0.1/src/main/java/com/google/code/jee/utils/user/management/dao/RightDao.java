package com.google.code.jee.utils.user.management.dao;

import com.google.code.jee.utils.dal.dao.GenericDao;
import com.google.code.jee.utils.user.management.model.Right;

public interface RightDao extends GenericDao<Integer, Right> {
	
	String COUNT_BY_CODE = "right.countByCode";
	String FIND_BY_CODE = "right.findByCode";
	
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

}
