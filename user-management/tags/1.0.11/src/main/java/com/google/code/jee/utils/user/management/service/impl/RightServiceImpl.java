package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RightDao;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.service.RightService;
import com.google.code.jee.utils.user.management.service.RoleService;

/**
 * The Class RightServiceImpl.
 */
@Service
public class RightServiceImpl extends AbstractGenericService<Integer, Right, RightDao> implements RightService {

	@Autowired
	private RoleService roleService;
	
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
    public Boolean existWithCode(String code) {
    	Boolean exist = false;
        if (!StringUtil.isEmpty(code)) {
            final Integer count = this.dao.countByCode(code);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByCode(String code) {
    	Right right = null;
        if (!StringUtil.isEmpty(code))
            right = this.dao.findByCode(code);
        return right;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByRoleId(Integer roleId) {
    	return this.dao.countByRoleId(roleId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Right> findAllByRoleId(Integer roleId) {
    	List<Right> rights = null;
        if (roleId != null)
            rights = this.dao.findAllByRoleId(roleId);
        return rights;
    }
    
    /**
     * {@inheritedDoc}
     */   
    @Override
    public boolean isRemovable(Integer rightId) {
        if (this.roleService.countByRightId(rightId) != 0)
        	return false;
        else
        	return true;
    }
	
}
