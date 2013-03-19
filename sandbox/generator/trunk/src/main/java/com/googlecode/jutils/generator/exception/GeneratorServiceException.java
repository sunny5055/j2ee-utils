package com.googlecode.jutils.generator.exception;

/**
 * The Class GeneratorServiceException.
 */
@SuppressWarnings("serial")
public class GeneratorServiceException extends Exception {
	/**
	 * Instantiates a new generator service exception.
	 */
	public GeneratorServiceException() {
		super();
	}

	/**
	 * Instantiates a new generator service exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public GeneratorServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new generator service exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public GeneratorServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new generator service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public GeneratorServiceException(Throwable cause) {
		super(cause);
	}
}
