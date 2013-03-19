package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RightDao;
import com.google.code.jee.utils.user.management.dao.RoleDao;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.service.RoleService;

/**
 * {@inheritedDoc}
 */
@Service
public class RoleServiceImpl extends AbstractGenericService<Integer, Role, RoleDao> implements RoleService {
	
	@Autowired
	private RightDao rightDao;
	
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
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRightCode(String rightCode) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(rightCode)) {
            final Integer count = this.rightDao.countByCode(rightCode);
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
            right = this.rightDao.findByCode(rightCode);
        }
        return right;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Right> findAllRights() {
    	return this.rightDao.findAll();
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

}
