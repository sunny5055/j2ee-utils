package com.google.code.jee.utils.bundle.hibernate.dao;

import java.util.List;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.model.LabelId;
import com.google.code.jee.utils.dal.dao.GenericDao;

/**
 * The Interface LabelDao.
 */
public interface LabelDao extends GenericDao<LabelId, Label> {
    String COUNT_FOR_KEY = "label.countForKey";
    String FIND_ALL_BY_KEY = "label.findAllByKey";
    String COUNT_FOR_LANGUAGE = "label.countForLanguage";
    String FIND_ALL_BY_LANGUAGE = "label.findAllByLanguage";

    /**
     * Search the number of elements with the 'key' parameter.
     * 
     * @param key the key
     * @return the number of elements found
     */
    Integer countForKey(String key);

    /**
     * Search an element by its key.
     * 
     * @param key the key
     * @return the mail
     */
    List<Label> findAllByKey(String key);

    /**
     * Count by language.
     *
     * @param language the language
     * @return the number of elements found
     */
    Integer countForLanguage(String language);

    /**
     * Finds the labels by language.
     *
     * @param language the language
     * @return the label
     */
    List<Label> findAllByLanguage(String language);
}