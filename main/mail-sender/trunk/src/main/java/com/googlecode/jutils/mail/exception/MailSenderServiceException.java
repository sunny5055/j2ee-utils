package com.googlecode.jutils.mail.exception;

/**
 * The Class MailServiceException.
 */
@SuppressWarnings("serial")
public class MailSenderServiceException extends Exception {

    /**
     * Instantiates a new mail service exception.
     */
    public MailSenderServiceException() {
        super();
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param errorMessage the error message
     */
    public MailSenderServiceException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public MailSenderServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new mail service exception.
     * 
     * @param cause the cause
     */
    public MailSenderServiceException(Throwable cause) {
        super(cause);
    }

}
