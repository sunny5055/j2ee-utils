package com.google.code.jee.utils.bundle.hibernate.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.bundle.hibernate.model.LabelId;
import com.google.code.jee.utils.dal.service.GenericService;

// TODO: Auto-generated Javadoc
/**
 * The Interface LabelService.
 */
public interface LabelService extends GenericService<LabelId, Label> {

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
     * Export all the labels by a specific language.
     * 
     * @param outputStream the output stream
     * @param language the language
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void exportBundle(OutputStream outputStream, String language) throws IOException;

    /**
     * Import all the labels by a specific language.
     * 
     * @param inputStream the input stream
     * @param language the language
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void importBundle(InputStream inputStream, String language) throws IOException;
}
