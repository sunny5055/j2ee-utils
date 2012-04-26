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
    private ParameterDao parameterDao;

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count() {
        return parameterDao.count();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll() {
        return parameterDao.findAll();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer count(SearchCriteria searchCriteria) {
        return parameterDao.count(searchCriteria);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<AbstractParameter<?>> findAll(SearchCriteria searchCriteria) {
        return parameterDao.findAll(searchCriteria);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithName(String name) {
        boolean exist = false;
        if (!StringUtil.isEmpty(name)) {
            final Integer count = parameterDao.countByName(name);
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
            final AbstractParameter<?> parameter = parameterDao.findByName(name);
            value = (V) parameter.getValue();
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
        if (value != null) {
            if (!existWithName(name)) {
                if (value instanceof String) {
                    StringParameter stringParameter = new StringParameter();
                    stringParameter.setName(name);
                    stringParameter.setValue((String) value);
                    parameterDao.save(stringParameter);
                } else if (value instanceof Boolean) {
                    BooleanParameter booleanParameter = new BooleanParameter();
                    booleanParameter.setName(name);
                    booleanParameter.setValue((Boolean) value);
                    parameterDao.save(booleanParameter);
                } else if (value instanceof Float) {
                    FloatParameter floatParameter = new FloatParameter();
                    floatParameter.setName(name);
                    floatParameter.setValue((Float) value);
                    parameterDao.save(floatParameter);
                } else if (value instanceof Integer) {
                    IntegerParameter integerParameter = new IntegerParameter();
                    integerParameter.setName(name);
                    integerParameter.setValue((Integer) value);
                    parameterDao.save(integerParameter);
                } else if (value instanceof Date) {
                    DateParameter dateParameter = new DateParameter();
                    dateParameter.setName(name);
                    dateParameter.setValue((Date) value);
                    parameterDao.save(dateParameter);
                }
            } else {
                if (value instanceof String) {
                    StringParameter stringParameter = (StringParameter) parameterDao.findByName(name);
                    stringParameter.setValue((String) value);
                    parameterDao.save(stringParameter);
                } else if (value instanceof Boolean) {
                    BooleanParameter booleanParameter = (BooleanParameter) parameterDao.findByName(name);
                    booleanParameter.setValue((Boolean) value);
                    parameterDao.save(booleanParameter);
                } else if (value instanceof Float) {
                    FloatParameter floatParameter = (FloatParameter) parameterDao.findByName(name);
                    floatParameter.setValue((Float) value);
                    parameterDao.save(floatParameter);
                } else if (value instanceof Integer) {
                    IntegerParameter integerParameter = (IntegerParameter) parameterDao.findByName(name);
                    integerParameter.setValue((Integer) value);
                    parameterDao.save(integerParameter);
                } else if (value instanceof Date) {
                    DateParameter dateParameter = (DateParameter) parameterDao.findByName(name);
                    dateParameter.setValue((Date) value);
                    parameterDao.save(dateParameter);
                }
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
                returnValue = parameterDao.delete(parameterDao.findByName(name));
            }
        }
        return returnValue;
    }

}
