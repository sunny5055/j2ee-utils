package com.google.code.jee.utils.bundle.hibernate.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;
import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.model.LabelId;
import com.google.code.jee.utils.bundle.hibernate.service.LabelService;
import com.google.code.jee.utils.collection.CollectionUtil;
import com.google.code.jee.utils.dal.service.AbstractGenericService;

/**
 * The Class LabelServiceImpl.
 */
@Service
public class LabelServiceImpl extends AbstractGenericService<LabelId, Label, LabelDao> implements LabelService {

    /**
     * {@inheritedDoc}
     */
    @Override
    @Autowired
    public void setDao(LabelDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForKey(String key) {
        return dao.countForKey(key);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public List<Label> findAllByKey(String key) {
        List<Label> label = null;
        if (!StringUtil.isEmpty(key)) {
            label = dao.findAllByKey(key);
        }
        return label;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public Integer countForLanguage(String language) {
        return dao.countForLanguage(language);
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
    public void exportBundle(OutputStream outputStream, String language) throws IOException {
        if (outputStream != null && !StringUtil.isBlank(language)) {
            final List<Label> labels = dao.findAllByLanguage(language);
            if (!CollectionUtil.isEmpty(labels)) {
                final Properties properties = new Properties();
                for (final Label label : labels) {
                    properties.put(label.getPrimaryKey().getKey(), label.getValue());
                }

                properties.store(outputStream, null);
            }
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void importBundle(InputStream inputStream, String language) throws IOException {
        if (inputStream != null && !StringUtil.isBlank(language)) {
            // Load properties
            final Properties properties = new Properties();
            properties.load(inputStream);

            // Create a label for each element contained in the file
            for (final Object key : properties.keySet()) {
                final String keyString = (String) key;
                final String value = properties.getProperty(keyString);

                final LabelId labelId = new LabelId(keyString, language);
                final Label label = new Label();
                label.setPrimaryKey(labelId);
                label.setValue(value);

                // Database insertion
                dao.create(label);
            }
        }
    }
}
