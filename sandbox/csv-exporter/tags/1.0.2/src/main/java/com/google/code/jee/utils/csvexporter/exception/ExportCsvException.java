package com.google.code.jee.utils.csvexporter.exception;

/**
 * The Class ExportCsvException.
 */
@SuppressWarnings ("serial")
public class ExportCsvException extends Exception {

    /**
     * The Constructor.
     */
    public ExportCsvException() {
        super();
    }

    /**
     * The Constructor.
     * 
     * @param msg the msg
     * @param t the t
     */
    public ExportCsvException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * The Constructor.
     * 
     * @param msg the msg
     */
    public ExportCsvException(String msg) {
        super(msg);
    }

    /**
     * The Constructor.
     * 
     * @param t the t
     */
    public ExportCsvException(Throwable t) {
        super(t);
    }

}
