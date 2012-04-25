package com.google.code.jee.utils.bundle.hibernate.service;

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
     * @return true, if success
     */
    boolean existWithKey(String key);

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
     * @return true, if success
     */
    boolean existWithLanguage(String language);

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
     * @param file the file
     * @param Language the Language
     */
    void exportBundle(OutputStream outputStream, String language);

    /**
     * Import all the labels by a specific language.
     *
     * @param file the file
     * @param Language the Language
     */
    void importBundle(InputStream inputStream, String language);

    /**
     * To csv file export.
     *
     * @param file the file
     */
    void toCsvFileExport(InputStream file, String Language);

    /**
     * From csv file import.
     *
     * @param file the file
     */
    void fromCsvFileImport(InputStream file, String Language);
}
