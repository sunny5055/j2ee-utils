package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RoleDao;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.service.RoleService;
import com.google.code.jee.utils.user.management.service.UserService;

/**
 * The Class RoleServiceImpl.
 */
@Service
public class RoleServiceImpl extends AbstractGenericService<Integer, Role, RoleDao> implements RoleService {
	
	@Autowired
	private UserService userService;
	
	/**
     * {@inheritedDoc}
     */
    @Autowired
    @Override
    public void setDao(RoleDao dao) {
        this.dao = dao;
    }
    
    /**
     * {@inheritedDoc}
     */   
    @Override
    public boolean isRemovable(Integer pk) {
        if (this.userService.countByRoleId(pk) != 0)
        	return false;
        else
        	return true;
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
    public Role findByCode(String code) {
    	Role role = null;
        if (!StringUtil.isEmpty(code))
            role = this.dao.findByCode(code);
        return role;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countByUserId(Integer userId) {
    	return this.dao.countByUserId(userId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Role> findAllByUserId(Integer userId) {
    	List<Role> roles = null;
        if (userId != null)
            roles = this.dao.findAllByUserId(userId);
        return roles;
    }

    /**
     * {@inheritedDoc}
     */   
	@Override
	public Integer countByRightId(Integer rightId) {
		return this.dao.countByRightId(rightId);
	}

}
