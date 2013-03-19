package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RightDao;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.service.RightService;

/**
 * {@inheritedDoc}
 */
@Service
public class RightServiceImpl extends AbstractGenericService<Integer, Right, RightDao> implements RightService {

	/**
     * {@inheritedDoc}
     */
    @Autowired
    @Override
    public void setDao(RightDao rightDao) {
        this.dao = rightDao;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRightCode(String rightCode) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(rightCode)) {
            final Integer count = this.dao.countByCode(rightCode);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByRightCode(String rightCode) {
    	Right right = null;
        if (!StringUtil.isEmpty(rightCode)) {
            right = this.dao.findByCode(rightCode);
        }
        return right;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRoleIdAndRightCode(Integer roleId, String rightCode) {
    	boolean exist = false;
        if (roleId != null && !StringUtil.isEmpty(rightCode)) {
            final Integer count = this.dao.countForRoleIdAndRightCode(roleId, rightCode);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countRightsForRoleId(Integer roleId) {
    	return this.dao.countRightsForRoleId(roleId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Right> findAllRightsByRoleId(Integer roleId) {
    	List<Right> rights = null;
        if (roleId != null) {
            rights = this.dao.findAllRightsByRoleId(roleId);
        }
        return rights;
    }
    
    /**
     * {@inheritedDoc}
     */   
    @Override
    public boolean isRemovable(Integer rightId) {
        if (this.dao.countRolesForRightId(rightId) != 0)
        	return false;
        else
        	return true;
    }
	
}
