package com.googlecode.jutils.user.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.dal.service.AbstractGenericService;
import com.googlecode.jutils.user.management.dao.UserDao;
import com.googlecode.jutils.user.management.model.User;
import com.googlecode.jutils.user.management.service.UserService;

/**
 * The Class UserServiceImpl.
 */
@Service
public class UserServiceImpl extends AbstractGenericService<Integer, User, UserDao> implements UserService {

	/**
	 * {@inheritedDoc}
	 */
	@Autowired
	@Override
	public void setDao(UserDao dao) {
		this.dao = dao;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public Boolean existWithLogin(String login) {
		Boolean exist = false;
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
	public Integer countByRoleId(Integer roleId) {
		return this.dao.countByRoleId(roleId);
	}

}
