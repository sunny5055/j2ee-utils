package com.googlecode.jutils.pp.settings.exception;

/**
 * The Class SettingsBuilderException.
 */
@SuppressWarnings("serial")
public class SettingsBuilderException extends Exception {

	/**
	 * Instantiates a new settings builder exception.
	 */
	public SettingsBuilderException() {
		super();
	}

	/**
	 * Instantiates a new settings builder exception.
	 * 
	 * @param errorMessage
	 *            the error message
	 */
	public SettingsBuilderException(String errorMessage) {
		super(errorMessage);
	}

	/**
	 * Instantiates a new settings builder exception.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the cause
	 */
	public SettingsBuilderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new settings builder exception.
	 * 
	 * @param cause
	 *            the cause
	 */
	public SettingsBuilderException(Throwable cause) {
		super(cause);
	}
}
