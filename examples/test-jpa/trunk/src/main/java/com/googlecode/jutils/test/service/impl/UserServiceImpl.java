package com.googlecode.jutils.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.jutils.dal.service.AbstractGenericService;
import com.googlecode.jutils.test.dao.UserDao;
import com.googlecode.jutils.test.model.User;
import com.googlecode.jutils.test.service.UserService;

@Service
public class UserServiceImpl extends AbstractGenericService<Integer, User, UserDao> implements UserService {

	@Autowired
	@Override
	public void setDao(UserDao dao) {
		this.dao = dao;
	}
}
