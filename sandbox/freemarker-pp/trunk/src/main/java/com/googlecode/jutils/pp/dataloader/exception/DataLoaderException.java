package com.googlecode.jutils.pp.dataloader.exception;

/**
 * The Class DataLoaderException.
 */
@SuppressWarnings("serial")
public class DataLoaderException extends Exception {

	/**
	 * Instantiates a new data loader exception.
	 */
	public DataLoaderException() {
		super();
	}

	/**
	 * Instantiates a new data loader exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public DataLoaderException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new data loader exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public DataLoaderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new data loader exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public DataLoaderException(Throwable cause) {
		super(cause);
	}
}
