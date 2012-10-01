package com.googlecode.jee.utils.oxm.exception;

/**
 * The Class OxmServiceException.
 */
@SuppressWarnings("serial")
public class OxmServiceException extends Exception {

	/**
	 * Instantiates a new oxm service exception.
	 */
	public OxmServiceException() {
		super();
	}

	/**
	 * Instantiates a new oxm service exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public OxmServiceException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new oxm service exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public OxmServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new oxm service exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public OxmServiceException(Throwable cause) {
		super(cause);
	}
}
