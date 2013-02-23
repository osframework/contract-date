/*
 * File: FinancialCalendarException.java
 * 
 * Copyright 2013 OSFramework Project.
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
package org.osframework.contract.date.fincal;

/**
 * This exception class and its subclasses indicate financial calendar
 * generation conditions that a reasonable application might want to catch.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FinancialCalendarException extends Exception {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -9148219395693557033L;

	/**
	 * Construct a new financial calendar exception with the specified detail
	 * message.
	 *
	 * @param message the detail message
	 */
	public FinancialCalendarException(String message) {
		super(message);
	}

	/**
	 * Construct a new financial calendar exception with the specified detail
	 * message and cause.
	 *
	 * @param message the detail message
	 * @param cause the cause 
	 */
	public FinancialCalendarException(String message, Throwable cause) {
		super(message, cause);
	}

}
