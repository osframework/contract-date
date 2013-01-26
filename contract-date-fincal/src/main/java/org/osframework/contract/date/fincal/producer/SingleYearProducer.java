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
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
	private final boolean weekendsAsHolidays;
	private final Calendar c;

	/**
	 * Construct a <code>SingleYearCalendarProducer</code> for the
	 * specified year, enabling or disabling inclusion of weekend days as
	 * holidays.
	 *
	 * @param year year for which to produce holidays
	 * @param weekendsAsHolidays flag indicating inclusion/exclusion of weekend
	 *                           days as holidays
	 * @throws IllegalArgumentException if year is <code>null</code>
	 */
	public SingleYearProducer(final Integer year, final boolean weekendsAsHolidays) {
		Validate.notNull(year, "Integer year argument cannot be null");
		this.year = year.intValue();
		this.weekendsAsHolidays = weekendsAsHolidays;
		this.c = Calendar.getInstance();
	}

	/**
	 * Construct a <code>SingleYearCalendarProducer</code> for the
	 * specified year. Excludes weekend days.
	 *
	 * @param year year for which to produce holidays
	 * @throws IllegalArgumentException if year is <code>null</code>
	 */
	public SingleYearProducer(final Integer year) {
		this(year, false);
	}

	public boolean includesWeekends() {
		return weekendsAsHolidays;
	}

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
			if (weekendsAsHolidays) {
				addWeekends(calendar, holidays);
			}
		}
		return holidays.toArray(EMPTY_ARRAY);
	}

	private void addWeekends(FinancialCalendar calendar, List<Holiday> holidays) {
		c.clear();
		c.set(year, Calendar.JANUARY, 1);
		while (c.get(Calendar.YEAR) == year) {
			if (Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK) ||
				Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)) {
				holidays.add(new Holiday(calendar, c.getTime(), WEEKEND_HOLIDAY_DEFINITION));
			}
			c.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

}
