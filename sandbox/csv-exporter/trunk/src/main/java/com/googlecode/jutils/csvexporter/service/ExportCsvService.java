package com.googlecode.jutils.csvexporter.service;

import java.io.OutputStream;
import java.util.List;

import com.googlecode.jutils.csvexporter.exception.ExportCsvException;

/**
 * The Interface ExportCsvService.
 */
public interface ExportCsvService {

    /**
     * Export.
     * 
     * @param elements the elements
     * @return the list< string>
     * @throws ExportCsvException the export csv exception
     */
    List<String> export(List<?> elements) throws ExportCsvException;

    /**
     * Export.
     * 
     * @param outputStream the output stream
     * @param elements the elements
     * @throws ExportCsvException the export csv exception
     */
    void export(OutputStream outputStream, List<?> elements) throws ExportCsvException;
}
