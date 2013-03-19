package com.googlecode.jutils.user.management.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.StringUtil;
import com.googlecode.jutils.dal.service.AbstractGenericService;
import com.googlecode.jutils.user.management.dao.RightDao;
import com.googlecode.jutils.user.management.model.Right;
import com.googlecode.jutils.user.management.service.RightService;
import com.googlecode.jutils.user.management.service.RoleService;

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
	public boolean isRemovable(Integer pk) {
		if (this.roleService.countByRightId(pk) != 0) {
			return false;
		} else {
			return true;
		}
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
		if (!StringUtil.isEmpty(code)) {
			right = this.dao.findByCode(code);
		}
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
		if (roleId != null) {
			rights = this.dao.findAllByRoleId(roleId);
		}
		return rights;
	}

}
