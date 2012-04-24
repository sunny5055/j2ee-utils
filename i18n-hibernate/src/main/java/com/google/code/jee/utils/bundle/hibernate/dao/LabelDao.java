package com.google.code.jee.utils.bundle.hibernate.dao;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.dal.dao.GenericDao;

/**
 * The Interface LabelDao.
 */
public interface LabelDao extends GenericDao<Integer, Label> {
    String COUNT_BY_KEY = "label.countByKey";
    String FIND_BY_KEY = "label.findByKey";
    String COUNT_BY_LANGUAGE = "label.countByLanguage";
    String FIND_BY_LANGUAGE = "label.findByLanguage";

    /**
     * Search the number of elements with the 'key' parameter.
     * 
     * @param key the key
     * @return the number of element found
     */
    Integer countByKey(String key);

    /**
     * Search an element by its key.
     * 
     * @param key the key
     * @return the mail
     */
    Label findByKey(String key);

    /**
     * Count by language.
     *
     * @param language the language
     * @return the number of element found
     */
    Integer countByLanguage(String language);

    /**
     * Finds the by language.
     *
     * @param language the language
     * @return the label
     */
    Label findByLanguage(String language);
}
