package com.google.code.jee.utils.parameter.hibernate.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.dao.ParameterDao;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;
import com.google.code.jee.utils.parameter.hibernate.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterDao parameterDao;

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count() {
        return null;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll() {
        return null;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count(SearchCriteria searchCriteria) {
        return null;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
        return null;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithName(String name) {
        return false;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V> V getValue(String name) {
        V value = null;
        if (!StringUtil.isBlank(name)) {
            final AbstractParameter<?> parameter = parameterDao.findByName(name);
            value = (V) parameter.getValue();
        }
        return value;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public <V> void setValue(String name, V value) {
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer removeValue(String name) {
        return null;
    }

}
