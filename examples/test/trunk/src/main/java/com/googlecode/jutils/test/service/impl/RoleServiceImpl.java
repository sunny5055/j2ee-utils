package com.googlecode.jutils.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.dal.service.AbstractGenericService;
import com.googlecode.jutils.test.dao.RoleDao;
import com.googlecode.jutils.test.model.Role;
import com.googlecode.jutils.test.service.RoleService;

@Service
public class RoleServiceImpl extends AbstractGenericService<Integer, Role, RoleDao> implements RoleService {

	@Autowired
	@Override
	public void setDao(RoleDao dao) {
		this.dao = dao;
	}
}
