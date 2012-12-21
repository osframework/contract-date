/**
 * 
 */
package org.osframework.contract.date.fincal.config;

/**
 * @author kzk55j
 *
 */
public class ConfigurationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3287116248579691070L;

	/**
	 * 
	 */
	public ConfigurationException() {
		super();
	}

	/**
	 * @param message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
