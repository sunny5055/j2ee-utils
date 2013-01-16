package com.googlecode.jutils.generator.formatter.exception;

/**
 * The Class FormatterServiceException.
 */
@SuppressWarnings("serial")
public class FormatterException extends Exception {
	/**
	 * Instantiates a new formatter service exception.
	 */
	public FormatterException() {
		super();
	}

	/**
	 * Instantiates a new formatter service exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public FormatterException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new formatter service exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public FormatterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new formatter service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public FormatterException(Throwable cause) {
		super(cause);
	}
}
