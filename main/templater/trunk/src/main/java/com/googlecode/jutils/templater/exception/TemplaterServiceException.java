package com.googlecode.jutils.templater.exception;

/**
 * The Class TemplaterServiceException.
 */
@SuppressWarnings("serial")
public class TemplaterServiceException extends Exception {

	/**
	 * Instantiates a new templater service exception.
	 */
	public TemplaterServiceException() {
		super();
	}

	/**
	 * Instantiates a new templater service exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public TemplaterServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new templater service exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public TemplaterServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new templater service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public TemplaterServiceException(Throwable cause) {
		super(cause);
	}
}
