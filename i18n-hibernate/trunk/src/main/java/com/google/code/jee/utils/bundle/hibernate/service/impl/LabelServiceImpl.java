package com.google.code.jee.utils.bundle.hibernate.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.bundle.hibernate.dao.LabelDao;
import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.model.LabelId;
import com.google.code.jee.utils.bundle.hibernate.service.LabelService;
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
    public void exportBundle(OutputStream outputStream, String language) {
        List<Label> labels = dao.findAllByLanguage(language);
        final PrintStream printStream = new PrintStream(outputStream);
        for (Label label : labels) {
            printStream.println(label.getPrimaryKey().getKey() + "=" + label.getValue());
        }
        printStream.close();
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void importBundle(InputStream inputStream, String language) {
        try {
            // Load properties
            final Properties properties = new Properties();
            properties.load(inputStream);

            // Create a label for each element contained in the file
            for(Enumeration<Object> keys = properties.keys(); keys.hasMoreElements();) {
                String key = (String) keys.nextElement();
                
                // LabelId creation
                LabelId labelId = new LabelId();
                labelId.setKey(key);
                labelId.setLanguage(language);
                
                // Label creation
                Label label = new Label();
                label.setPrimaryKey(labelId);
                label.setValue(properties.getProperty(key));
                
                // Database insertion
                dao.create(label);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void toCsvFileExport(InputStream file, String language) {
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public void fromCsvFileImport(InputStream file, String language) {
    }

}
