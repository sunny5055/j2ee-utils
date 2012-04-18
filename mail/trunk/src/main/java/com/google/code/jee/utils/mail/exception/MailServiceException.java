package com.google.code.jee.utils.mail.exception;

/**
 * The Class MailServiceException.
 */
@SuppressWarnings("serial")
public class MailServiceException extends Exception {

    /**
     * Instantiates a new mail service exception.
     */
    public MailServiceException() {
        super();
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param errorMessage the error message
     */
    public MailServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public MailServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param cause the cause
     */
    public MailServiceException(Throwable cause) {
        super(cause);
    }

}
