/**
 * File: HolidayExpressionRelativeImpl.java
 * 
 * Copyright 2012 David A. Joyce, OSFramework Project.
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
package org.osframework.contract.date.holiday.expression;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

/**
 * Produce the date of a holiday occurring on the <i>N</i>th named weekday in a
 * month in a given year.
 *
 * @author <a href="mailto:david.joyce13@gmail.com">Dave Joyce</a>
 */
public class HolidayExpressionRelativeImpl implements HolidayExpression {

	private final int calendarMonthConst, calendarWeekdayConst, weekdayNumInMonth;

	/**
	 * Constructor - accepts expression argument of the form:
	 * <pre>
	 * MONTHNAME/WEEKDAYNAME/WEEKDAY_NUM_IN_MONTH
	 * </pre>
	 */
	public HolidayExpressionRelativeImpl(final String expression) {
		if (null == expression) {
			throw new IllegalArgumentException("argument 'expression' cannot be null");
		}
		String[] parts = expression.split("/");
		if (3 != parts.length) {
			throw new IllegalArgumentException("Invalid argument 'expression'");
		}
		Class<Calendar> calClass = Calendar.class;
		Field[] calFields = calClass.getFields();
		String monthName   = parts[0].trim().toUpperCase(),
			   weekdayName = parts[1].trim().toUpperCase();
		int monthConstVal   = -1,
			weekdayConstVal = -1;
		for (Field f : calFields) {
			if (!(Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))) {
				continue;
			}
			if (f.getName().equals(monthName)) {
				try {
					monthConstVal = f.getInt(null);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("Calendar constant '" + monthName + "' not accessible", e);
				}
				break;
			}
		}
		this.calendarMonthConst = monthConstVal;
		for (Field f : calFields) {
			if (!(Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))) {
				continue;
			}
			if (f.getName().equals(weekdayName)) {
				try {
					weekdayConstVal = f.getInt(null);
				} catch (IllegalAccessException e) {
					throw new IllegalArgumentException("Calendar constant '" + weekdayName + "' not accessible", e);
				}
				break;
			}
		}
		this.calendarWeekdayConst = weekdayConstVal;
		int weekdayNumVal = Integer.parseInt(parts[2].trim());
		if (!(1 <= weekdayNumVal && weekdayNumVal <= 4)) {
			throw new IllegalArgumentException("Invalid " + weekdayName + " number");
		}
		this.weekdayNumInMonth = weekdayNumVal;
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, calendarMonthConst);
		c.set(Calendar.DAY_OF_MONTH, 1);
		int i = 0;
		while (i < weekdayNumInMonth) {
			if (calendarWeekdayConst == c.get(Calendar.DAY_OF_WEEK)) {
				i += 1;
			}
			// Prevent adding 1 day beyond target date
			if (i != weekdayNumInMonth) {
				c.add(Calendar.DAY_OF_MONTH, 1);
			}
		}
		return c.getTime();
	}

}
