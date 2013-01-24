/*
 * File: FinancialCalendarRuntimeException.java
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
 * generation exceptions that can be thrown during the normal operation of the
 * Java Virtual Machine. Just as with other runtime exceptions, it is not
 * reasonable for an application to catch these.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class FinancialCalendarRuntimeException extends RuntimeException {

	/**
	 * Serializable UID.
	 */
	private static final long serialVersionUID = -8333521219779086395L;

	/**
	 * Construct a new financial calendar runtime exception with the specified
	 * detail message.
	 *
	 * @param message the detail message
	 */
	public FinancialCalendarRuntimeException(String message) {
		super(message);
	}

	/**
	 * Construct a new financial calendar runtime exception with the specified
	 * detail message and cause.
	 *
	 * @param message the detail message
	 * @param cause the cause 
	 */
	public FinancialCalendarRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

}
