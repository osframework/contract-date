/*
 * File: Assert.java
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
package org.osframework.contract.date.testng;

import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

/**
 * Assert tool class. Provides additional assertion methods for Date and
 * Calendar comparisons beyond the methods exposed by {@link org.testng.Assert}.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class Assert {

	/**
	 * Protected constructor since it is a static only class.
	 */
	protected Assert() {}

	/**
	 * Assert that two Dates represent the same calendar date. If they do not,
	 * an AssertionError is thrown.
	 * 
	 * @param actual the actual date
	 * @param expected the expected date
	 * @param message the assertion error message
	 */
	public static void assertSameDate(Date actual, Date expected) {
		assertSameDate(actual, expected, null);
	}

	/**
	 * Assert that two Dates represent the same calendar date. If they do not,
	 * an AssertionError, with the given message, is thrown.
	 * 
	 * @param actual the actual date
	 * @param expected the expected date
	 * @param message the assertion error message
	 */
	public static void assertSameDate(Date actual, Date expected, String message) {
		Calendar a = Calendar.getInstance(), e = (Calendar)a.clone();
		a.setTime(actual);
		e.setTime(expected);
		forceToMidnight(a);
		forceToMidnight(e);
		Date actualMidnight = a.getTime(), expectedMidnight = e.getTime();
		assertEquals(actualMidnight, expectedMidnight, message);
	}

	/**
	 * Assert that two Calendars represent the same calendar date. If they do
	 * not, an AssertionError is thrown.
	 * 
	 * @param actual the actual date
	 * @param expected the expected date
	 * @param message the assertion error message
	 */
	public static void assertSameDate(Calendar actual, Calendar expected) {
		assertSameDate(actual, expected, null);
	}

	/**
	 * Assert that two Calendars represent the same calendar date. If they do
	 * not, an AssertionError, with the given message, is thrown.
	 * 
	 * @param actual the actual date
	 * @param expected the expected date
	 * @param message the assertion error message
	 */
	public static void assertSameDate(Calendar actual, Calendar expected, String message) {
		forceToMidnight(actual);
		forceToMidnight(expected);
		assertEquals(actual, expected, message);
	}

	/**
	 * Force the specified <code>Calendar</code> object to represent
	 * 00:00:00.000 hours (ie, midnight) of its date. This effectively "strips
	 * off" the time portion, for purposes of date-only equality comparison.
	 * 
	 * @param c Calendar object to be set to midnight of its stored date
	 */
	private static void forceToMidnight(Calendar c) {
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
	}

}
