package com.googlecode.jutils.xslt.exception;

/**
 * The Class XsltServiceException.
 */
@SuppressWarnings("serial")
public class XsltServiceException extends Exception {

	/**
	 * Instantiates a new xslt service exception.
	 */
	public XsltServiceException() {
		super();
	}

	/**
	 * Instantiates a new xslt service exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public XsltServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new xslt service exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public XsltServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new xslt service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public XsltServiceException(Throwable cause) {
		super(cause);
	}
}
