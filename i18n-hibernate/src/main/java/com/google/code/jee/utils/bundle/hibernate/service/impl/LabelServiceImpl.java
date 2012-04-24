package com.google.code.jee.utils.bundle.hibernate.service.impl;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;
import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.service.LabelService;
import com.google.code.jee.utils.dal.service.AbstractGenericService;

/**
 * The Class LabelServiceImpl.
 */
public class LabelServiceImpl extends AbstractGenericService<Integer, Label, LabelDao> implements LabelService {

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithKey(String key) {
        boolean exist = false;
        if (!StringUtil.isEmpty(key)) {
            final Integer count = dao.countByKey(key);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Label findByKey(String key) {
        Label label = null;
        if (!StringUtil.isEmpty(key)) {
            label = dao.findByKey(key);
        }
        return label;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public boolean existWithLanguage(String language) {
        boolean exist = false;
        if (!StringUtil.isEmpty(language)) {
            final Integer count = dao.countByKey(language);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Label findByLanguage(String language) {
        Label label = null;
        if (!StringUtil.isEmpty(language)) {
            label = dao.findByKey(language);
        }
        return label;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setDao(LabelDao dao) {
        this.dao = dao;
    }

}
