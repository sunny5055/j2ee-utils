package com.google.code.jee.utils.bundle.hibernate.service;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.dal.service.GenericService;

public interface LabelService extends GenericService<Integer, Label> {

    /**
     * Test the existence of an element with the parameter 'key'.
     * 
     * @param key the key
     * @return true, if success
     */
    boolean existWithKey(String key);

    /**
     * Search an element by its key.
     * 
     * @param key the key
     * @return the label
     */
    Label findByKey(String key);

    /**
     * Test the existence of an element with the parameter 'language'.
     * 
     * @param language the language
     * @return true, if success
     */
    boolean existWithLanguage(String language);

    /**
     * Search an element by its language.
     * 
     * @param language the language
     * @return the label
     */
    Label findByLanguage(String language);
}
