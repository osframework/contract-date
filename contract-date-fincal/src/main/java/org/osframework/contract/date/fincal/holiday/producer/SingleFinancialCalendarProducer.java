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
package org.osframework.contract.date.fincal.holiday.producer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.osframework.contract.date.fincal.definition.FinancialCalendar;
import org.osframework.contract.date.fincal.definition.HolidayDefinition;
import org.osframework.contract.date.fincal.definition.HolidayExpression;
import org.osframework.contract.date.fincal.definition.expression.centralbank.CentralBankDecoratorLocator;
import org.osframework.contract.date.fincal.holiday.Holiday;

/**
 * Produces holidays for a single financial calendar.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class SingleFinancialCalendarProducer implements HolidayProducer<Integer> {

	private final FinancialCalendar calendar;
	private final boolean weekendsAsHolidays;
	private final Calendar c;

	/**
	 * Construct a <code>SingleFinancialCalendarProducer</code> for the
	 * specified calendar, enabling or disabling inclusion of weekend days as
	 * holidays.
	 *
	 * @param calendar financial calendar definition from which to produce
	 *                 holidays
	 * @param weekendsAsHolidays flag indicating inclusion/exclusion of weekend
	 *                           days as holidays
	 * @throws IllegalArgumentException if calendar is <code>null</code>
	 */
	public SingleFinancialCalendarProducer(final FinancialCalendar calendar, final boolean weekendsAsHolidays) {
		Validate.notNull(calendar, "FinancialCalendar argument cannot be null");
		this.calendar = calendar;
		this.weekendsAsHolidays = weekendsAsHolidays;
		this.c = Calendar.getInstance();
	}

	/**
	 * Construct a <code>SingleFinancialCalendarProducer</code> for the
	 * specified calendar. Excludes weekend days.
	 *
	 * @param calendar financial calendar definition from which to produce
	 *                 holidays
	 * @throws IllegalArgumentException if calendar is <code>null</code>
	 */
	public SingleFinancialCalendarProducer(final FinancialCalendar calendar) {
		this(calendar, false);
	}

	public boolean includesWeekends() {
		return weekendsAsHolidays;
	}

	public Holiday[] produce(Integer... years) {
		Arrays.sort(years);
		Range yearRange = new IntRange(years[0], years[years.length - 1]); 
		List<Holiday> holidays = new LinkedList<Holiday>();
		for (int year = yearRange.getMinimumInteger(); yearRange.containsInteger(year); year++) {
			for (HolidayDefinition hd : calendar) {
				HolidayExpression expr = CentralBankDecoratorLocator.decorate(hd, calendar.getCentralBank());
				Date date = expr.evaluate(year);
				holidays.add(new Holiday(calendar, date, hd));
			}
			if (weekendsAsHolidays) {
				this.addWeekends(year, holidays);
			}
		}
		return holidays.toArray(EMPTY_ARRAY);
	}

	private void addWeekends(int year, List<Holiday> holidays) {
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
