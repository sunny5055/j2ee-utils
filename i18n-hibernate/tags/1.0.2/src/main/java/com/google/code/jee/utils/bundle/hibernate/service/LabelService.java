package com.google.code.jee.utils.bundle.hibernate.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.model.LabelId;
import com.google.code.jee.utils.dal.service.GenericService;

/**
 * The Interface LabelService.
 */
public interface LabelService extends GenericService<LabelId, Label> {

    /**
     * Gets the entity.
     * 
     * @param pk the primary key
     * @return the entity
     */
    Label get(String key, String language);

    /**
     * Exist primary key.
     * 
     * @param pk the primary key
     * @return true, if successful
     */
    boolean exist(String key, String language);

    /**
     * Test the existence of an element with the parameter 'key'.
     * 
     * @param key the key
     * @return number of elements
     */
    Integer countForKey(String key);

    /**
     * Search all elements by their key.
     * 
     * @param key the key
     * @return the label
     */
    List<Label> findAllByKey(String key);

    /**
     * Test the existence of an element with the parameter 'language'.
     * 
     * @param language the language
     * @return number of elements
     */
    Integer countForLanguage(String language);

    /**
     * Search all elements by their language.
     * 
     * @param language the language
     * @return the label
     */
    List<Label> findAllByLanguage(String language);

    /**
     * Export all the labels for a specific language.
     * 
     * @param outputStream the output stream
     * @param language the language
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void exportBundle(OutputStream outputStream, String language) throws IOException;

    /**
     * Import all the labels for a specific language.
     * 
     * @param inputStream the input stream
     * @param language the language
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void importBundle(InputStream inputStream, String language) throws IOException;
}
