/*
 * File: ConfigurationException.java
 * 
 * Copyright 2012 OSFramework Project.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.osframework.contract.date.fincal.config;

/**
 * Base runtime exception type thrown for a configuration error.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class ConfigurationException extends RuntimeException {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = 3086140880181463951L;

	/**
     * Constructor - constructs a new configuration exception with the specified
     * detail message. The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later
     *                retrieval by the {@link #getMessage()} method.
     */
	public ConfigurationException(String message) {
		super(message);
	}

    /**
     * Constructor - constructs a new configuration exception with the specified
     * detail message and cause.
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this configuration exception's
     * detail message.</p>
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method). (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
