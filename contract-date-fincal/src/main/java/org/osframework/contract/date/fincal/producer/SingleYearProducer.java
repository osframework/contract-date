/**
 * File: SingleYearProducer.java
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;
import org.osframework.contract.date.fincal.expression.HolidayExpression;
import org.osframework.contract.date.fincal.expression.centralbank.CentralBankDecoratorLocator;
import org.osframework.contract.date.fincal.model.FinancialCalendar;
import org.osframework.contract.date.fincal.model.Holiday;
import org.osframework.contract.date.fincal.model.HolidayDefinition;

/**
 * Produces holidays for a single year.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SingleYearProducer implements HolidayProducer<FinancialCalendar> {

	private final int year;


	/**
	 * Construct a <code>SingleYearCalendarProducer</code> for the
	 * specified year.
	 *
	 * @param year year for which to produce holidays
	 * @throws IllegalArgumentException if year is <code>null</code>
	 */
	public SingleYearProducer(Integer year) {
		Validate.notNull(year, "Integer year argument cannot be null");
		this.year = year.intValue();
	}

	@Override
	public Holiday[] produce(FinancialCalendar... calendars) {
		// Sort calendars alphabetically by ID
		Arrays.sort(calendars, new Comparator<FinancialCalendar>() {
			public int compare(FinancialCalendar c1, FinancialCalendar c2) {
				return c1.getId().compareTo(c2.getId());
			}
		});
		LinkedList<Holiday> holidays = new LinkedList<Holiday>();
		for (FinancialCalendar calendar : calendars) {
			for (HolidayDefinition hd : calendar) {
				HolidayExpression expr = CentralBankDecoratorLocator.decorate(hd, calendar.getCentralBank());
				Date date = expr.evaluate(year);
				holidays.add(new Holiday(calendar, date, hd));
			}
		}
		return holidays.toArray(EMPTY_ARRAY);
	}

}
