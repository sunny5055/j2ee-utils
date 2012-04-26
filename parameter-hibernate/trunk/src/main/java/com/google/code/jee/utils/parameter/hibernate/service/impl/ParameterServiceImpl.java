package com.google.code.jee.utils.parameter.hibernate.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.dal.SearchCriteria;
import com.google.code.jee.utils.parameter.hibernate.dao.ParameterDao;
import com.google.code.jee.utils.parameter.hibernate.model.AbstractParameter;
import com.google.code.jee.utils.parameter.hibernate.model.BooleanParameter;
import com.google.code.jee.utils.parameter.hibernate.model.DateParameter;
import com.google.code.jee.utils.parameter.hibernate.model.FloatParameter;
import com.google.code.jee.utils.parameter.hibernate.model.IntegerParameter;
import com.google.code.jee.utils.parameter.hibernate.model.StringParameter;
import com.google.code.jee.utils.parameter.hibernate.service.ParameterService;

@Service
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterDao dao;

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count() {
        return dao.count();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll() {
        return dao.findAll();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count(SearchCriteria searchCriteria) {
        return dao.count(searchCriteria);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
        return dao.findAll(searchCriteria);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithName(String name) {
        boolean exist = false;
        if (!StringUtil.isEmpty(name)) {
            final Integer count = dao.countByName(name);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <V> V getValue(String name) {
        V value = null;
        if (!StringUtil.isBlank(name)) {
            final AbstractParameter<?> parameter = dao.findByName(name);
            if (parameter != null) {
                value = (V) parameter.getValue();
            }
        }
        return value;
    }

    /**
     * {@inheritedDoc}
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public <V> void setValue(String name, V value) {
        if (!StringUtil.isBlank(name) && value != null) {
            AbstractParameter<?> parameter = null;
            if (!existWithName(name)) {
                if (value instanceof Boolean) {
                    parameter = new BooleanParameter();
                } else if (value instanceof Float) {
                    parameter = new FloatParameter();
                } else if (value instanceof Integer) {
                    parameter = new IntegerParameter();
                } else if (value instanceof Date) {
                    parameter = new DateParameter();
                } else {
                    parameter = new StringParameter();
                }

                if (parameter != null) {
                    parameter.setName(name);
                }

            } else {
                parameter = dao.findByName(name);
            }

            if (parameter != null) {
                if (parameter instanceof BooleanParameter && value instanceof Boolean) {
                    ((BooleanParameter) parameter).setValue((Boolean) value);
                } else if (parameter instanceof FloatParameter && value instanceof Float) {
                    ((FloatParameter) parameter).setValue((Float) value);
                } else if (parameter instanceof IntegerParameter && value instanceof Integer) {
                    ((IntegerParameter) parameter).setValue((Integer) value);
                } else if (parameter instanceof DateParameter && value instanceof Date) {
                    ((DateParameter) parameter).setValue((Date) value);
                } else if (parameter instanceof StringParameter) {
                    ((StringParameter) parameter).setValue(value.toString());
                }

                dao.save(parameter);
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer removeValue(String name) {
        Integer returnValue = 0;
        if (!StringUtil.isBlank(name)) {
            if (existWithName(name)) {
                final AbstractParameter<?> parameter = dao.findByName(name);
                if (parameter != null) {
                    returnValue = dao.delete(parameter);
                }
            }
        }
        return returnValue;
    }
}
