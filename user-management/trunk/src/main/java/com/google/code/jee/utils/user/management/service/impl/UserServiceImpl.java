package com.google.code.jee.utils.user.management.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;
import com.google.code.jee.utils.user.management.dao.UserDao;
import com.google.code.jee.utils.user.management.model.User;
import com.google.code.jee.utils.user.management.service.UserService;

/**
 * {@inheritedDoc}
 */
@Service
public class UserServiceImpl extends AbstractGenericService<Integer, User, UserDao> implements UserService {

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

}
