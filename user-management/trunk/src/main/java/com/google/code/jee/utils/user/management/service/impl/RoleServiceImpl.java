package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RoleDao;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.service.RoleService;

/**
 * {@inheritedDoc}
 */
@Service
public class RoleServiceImpl extends AbstractGenericService<Integer, Role, RoleDao> implements RoleService {
	
	/**
     * {@inheritedDoc}
     */
    @Autowired
    @Override
    public void setDao(RoleDao roleDao) {
        this.dao = roleDao;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRoleCode(String roleCode) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(roleCode)) {
            final Integer count = this.dao.countByCode(roleCode);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Role findByRoleCode(String roleCode) {
    	Role role = null;
        if (!StringUtil.isEmpty(roleCode)) {
            role = this.dao.findByCode(roleCode);
        }
        return role;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithUserIdAndRoleCode(Integer userId, String roleCode) {
    	boolean exist = false;
        if (userId != null && !StringUtil.isEmpty(roleCode)) {
            final Integer count = this.dao.countForUserIdAndCode(userId, roleCode);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForUserId(Integer userId) {
    	return this.dao.countForUserId(userId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Role> findAllByUserId(Integer userId) {
    	List<Role> roles = null;
        if (userId != null) {
            roles = this.dao.findAllByUserId(userId);
        }
        return roles;
    }

}
