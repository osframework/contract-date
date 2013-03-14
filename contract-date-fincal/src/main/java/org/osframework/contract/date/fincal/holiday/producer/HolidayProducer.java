/*
 * File: HolidayProducer.java
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
package org.osframework.contract.date.fincal.holiday.producer;

import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.holiday.Holiday;

/**
 * Core behavior of objects which produce <code>Holiday</code> objects.
 *
 * @param <T> type of argument(s)
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public interface HolidayProducer<T> {

	/**
	 * Produce the array of <code>Holiday</code> objects for the specified
	 * arguments. This specification makes no requirements for the order of
	 * elements in the returned array. It is the responsibility of an
	 * implementor and/or its client to define whether the produced array is
	 * sorted or unsorted.
	 * 
	 * @param args one or more objects which define limits of holidays to be
	 *             produced
	 * @return array of <code>Holiday</code> objects
	 */
	public Holiday[] produce(T... args);

	/**
	 * Determine if this holiday producer includes weekend days (Saturday,
	 * Sunday) as holidays.
	 * 
	 * @return <code>true</code> if weekend days are included as holidays,
	 *         <code>false</code> otherwise
	 */
	public boolean includesWeekends();

	/**
	 * Special holiday definition for weekend "holidays".
	 */
	public static final HolidayDefinition WEEKEND_HOLIDAY_DEFINITION = new HolidayDefinition("weekend", "Weekend", "Weekend", null, null);

	/**
	 * Empty <code>Holiday</code> array. Used in conversion of collections
	 * to type arrays.
	 */
	public static final Holiday[] EMPTY_ARRAY = {};

}
