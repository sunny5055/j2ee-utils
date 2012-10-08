package com.googlecode.jutils.templater.exception;

/**
 * The Class TemplateServiceException.
 */
@SuppressWarnings("serial")
public class TemplateServiceException extends Exception {

    /**
     * Instantiates a new template service exception.
     */
    public TemplateServiceException() {
        super();
    }

    /**
     * Instantiates a new template service exception.
     * 
     * @param errorMessage the error message
     */
    public TemplateServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Instantiates a new template service exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public TemplateServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new template service exception.
     * 
     * @param cause the cause
     */
    public TemplateServiceException(Throwable cause) {
        super(cause);
    }
}
