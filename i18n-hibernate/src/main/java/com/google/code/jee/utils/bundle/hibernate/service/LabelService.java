package com.google.code.jee.utils.bundle.hibernate.service;

import java.io.InputStream;
import java.util.List;

import com.google.code.jee.utils.bundle.hibernate.model.Label;
import com.google.code.jee.utils.dal.service.GenericService;

// TODO: Auto-generated Javadoc
/**
 * The Interface LabelService.
 */
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
    void toPropertiesFileExportByLanguage(InputStream file, String Language);

    /**
     * Import all the labels by a specific language.
     *
     * @param file the file
     * @param Language the Language
     */
    void fromPropertiesFileImportByLanguage(InputStream file, String Language);

    /**
     * To csv file export.
     *
     * @param file the file
     */
    void toCsvFileExport(InputStream file);

    /**
     * From csv file import.
     *
     * @param file the file
     */
    void fromCsvFileImport(InputStream file);
}
