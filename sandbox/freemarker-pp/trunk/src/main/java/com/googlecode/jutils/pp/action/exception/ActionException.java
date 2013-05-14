package com.googlecode.jutils.pp.action.exception;

/**
 * The Class ActionException.
 */
@SuppressWarnings("serial")
public class ActionException extends Exception {

	/**
	 * Instantiates a new action exception.
	 */
	public ActionException() {
		super();
	}

	/**
	 * Instantiates a new action exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public ActionException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new action exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new action exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public ActionException(Throwable cause) {
		super(cause);
	}
}
