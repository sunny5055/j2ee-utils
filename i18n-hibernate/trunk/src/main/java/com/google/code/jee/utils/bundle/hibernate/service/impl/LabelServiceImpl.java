package com.google.code.jee.utils.bundle.hibernate.service.impl;

import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;
import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.service.LabelService;
import com.google.code.jee.utils.dal.service.AbstractGenericService;

/**
 * The Class LabelServiceImpl.
 */
@Service
public class LabelServiceImpl extends AbstractGenericService<Integer, Label, LabelDao> implements LabelService {

    /**
     * {@inheritedDoc}
     */
    @Override
    public void setDao(LabelDao dao) {
        this.dao = dao;
    }

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
            final Integer count = dao.countByLanguage(language);
            exist = count != 0;
        }
        return exist;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Label> findAllByLanguage(String language) {
        List<Label> labels = null;
        if (!StringUtil.isEmpty(language)) {
            labels = dao.findAllByLanguage(language);
        }
        return labels;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void toPropertiesFileExportByLanguage(InputStream file, String language) {
        List<Label> labels = dao.findAllByLanguage(language);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void fromPropertiesFileImportByLanguage(InputStream file, String Language) {
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void toCsvFileExport(InputStream file) {
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void fromCsvFileImport(InputStream file) {
    }

}
