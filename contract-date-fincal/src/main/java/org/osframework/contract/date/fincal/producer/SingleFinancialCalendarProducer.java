/*
 * File: SingleFinancialCalendarProducer.java
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
package org.osframework.contract.date.fincal.producer;

import java.util.LinkedList;

import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Produces holidays for a single financial calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SingleFinancialCalendarProducer implements HolidayProducer {

	private final FinancialCalendar calendar;

	/**
	 * Construct a <code>SingleFinancialCalendarProducer</code> for the
	 * specified calendar.
	 *
	 * @param calendar financial calendar definition from which to produce
	 *                 holidays
	 * @throws IllegalArgumentException if calendar is <code>null</code>
	 */
	public SingleFinancialCalendarProducer(final FinancialCalendar calendar) {
		Validate.notNull(calendar, "FinancialCalendar argument cannot be null");
		this.calendar = calendar;
	}

	@Override
	public Holiday[] produce(int... years) {
		LinkedList<Holiday> buffer = new LinkedList<Holiday>();
		for (int year : years) {
			for (HolidayDefinition def : calendar) {
				buffer.push(new Holiday(calendar, def.evaluate(year), def));
			}
		}
		return buffer.toArray(EMPTY_ARRAY);
	}

}
