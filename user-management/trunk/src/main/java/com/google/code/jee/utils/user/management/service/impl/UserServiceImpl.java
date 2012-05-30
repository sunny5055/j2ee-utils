package com.google.code.jee.utils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.RightDao;
import com.google.code.jee.utils.user.management.dao.RoleDao;
import com.google.code.jee.utils.user.management.dao.UserDao;
import com.google.code.jee.utils.user.management.model.Right;
import com.google.code.jee.utils.user.management.model.Role;
import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.UserService;

@Service
public class UserServiceImpl extends AbstractGenericService<Integer, User, UserDao> implements UserService {
	
	@Autowired
    private RoleDao roleDao;
	
	@Autowired
	private RightDao rightDao;

    /**
     * {@inheritedDoc}
     */
    @Autowired
    @Override
    public void setDao(UserDao userDao) {
        this.dao = userDao;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithLogin(String login) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(login)) {
            final Integer count = this.dao.countByLogin(login);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public User findByLogin(String login) {
    	User user = null;
        if (!StringUtil.isEmpty(login)) {
            user = this.dao.findByLogin(login);
        }
        return user;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRoleName(String roleName) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(roleName)) {
            final Integer count = this.roleDao.countByName(roleName);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Role findByRoleName(String roleName) {
    	Role role = null;
        if (!StringUtil.isEmpty(roleName)) {
            role = this.roleDao.findByName(roleName);
        }
        return role;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRightName(String rightName) {
    	boolean exist = false;
        if (!StringUtil.isEmpty(rightName)) {
            final Integer count = this.rightDao.countByName(rightName);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByRightName(String rightName) {
    	Right right = null;
        if (!StringUtil.isEmpty(rightName)) {
            right = this.rightDao.findByName(rightName);
        }
        return right;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Role> findAllRoles() {
    	return this.roleDao.findAll();
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
    public boolean existWithUserIdAndRoleName(Integer userId, String roleName) {
    	boolean exist = false;
        if (userId != null && !StringUtil.isEmpty(roleName)) {
            final Integer count = this.roleDao.countForUserIdAndName(userId, roleName);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Role findByUserIdAndRoleName(Integer userId, String roleName) {
    	Role role = null;
        if (!StringUtil.isEmpty(roleName) && userId != null) {
            role = this.roleDao.findByUserIdAndName(userId, roleName);
        }
        return role;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForUserId(Integer userId) {
    	return this.roleDao.countForUserId(userId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Role> findAllByUserId(Integer userId) {
    	List<Role> roles = null;
        if (userId != null) {
            roles = this.roleDao.findAllByUserId(userId);
        }
        return roles;
    }
    
    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithRoleIdAndRightName(Integer roleId, String rightName) {
    	boolean exist = false;
        if (roleId != null && !StringUtil.isEmpty(rightName)) {
            final Integer count = this.rightDao.countForRoleIdAndName(roleId, rightName);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Right findByRoleIdAndRightName(Integer roleId, String rightName) {
    	Right right = null;
        if (!StringUtil.isEmpty(rightName) && roleId != null) {
            right = this.rightDao.findByRoleIdAndName(roleId, rightName);
        }
        return right;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForRoleId(Integer roleId) {
    	return this.rightDao.countForRoleId(roleId);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Right> findAllByRoleId(Integer roleId) {
    	List<Right> rights = null;
        if (roleId != null) {
            rights = this.rightDao.findAllByRoleId(roleId);
        }
        return rights;
    }

}
