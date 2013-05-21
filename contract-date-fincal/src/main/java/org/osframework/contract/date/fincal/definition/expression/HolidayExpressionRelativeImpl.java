/*
 * File: HolidayExpressionRelativeImpl.java
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
package org.osframework.contract.date.fincal.definition.expression;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.annotation.XmlTransient;

import org.osframework.contract.date.fincal.definition.HolidayExpression;

/**
 * Produce the date of a holiday occurring on the <i>N</i>th named weekday in a
 * month in a given year.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
@XmlTransient
public class HolidayExpressionRelativeImpl implements HolidayExpression {

	private static final String LAST = "L";

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
		this.calendarMonthConst = nameToConst(parts[0]);
		this.calendarWeekdayConst = nameToConst(parts[1]);
		
		final String weekdayNumIndicator = parts[2].trim().toUpperCase();
		if (LAST.equals(weekdayNumIndicator)) {
			// Set to negative 'flag' value for switching
			// in evaluate() method
			this.weekdayNumInMonth = -1;
		} else {
			int intVal = Integer.parseInt(weekdayNumIndicator);
			if (!(1 <= intVal && intVal <= 4)) {
				throw new IllegalArgumentException("Invalid weekday count value: " + intVal);
			}
			this.weekdayNumInMonth = intVal;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, calendarMonthConst);
		switch (weekdayNumInMonth) {
		case -1:
			// Find last of the specified weekday in the month
			c.set(Calendar.DAY_OF_WEEK, calendarWeekdayConst);
			c.set(Calendar.DAY_OF_WEEK_IN_MONTH, -1);
			break;
		default:
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
			break;
		}
		return c.getTime();
	}

	private int nameToConst(final String parsedName) {
		final String name = parsedName.trim().toUpperCase();
		final Field[] calFields = Calendar.class.getFields();
		int constVal = Integer.MIN_VALUE;
		for (Field f : calFields) {
			if (!(Modifier.isPublic(f.getModifiers()) && Modifier.isStatic(f.getModifiers()))) {
				continue;
			}
			if (f.getName().equals(name)) {
				try {
					constVal = f.getInt(null);
					break;
				} catch (IllegalAccessException iae) {
					throw new IllegalArgumentException("Calendar constant '" + name + "' not accessible", iae);
				}
			}
		}
		if (Integer.MIN_VALUE == constVal) {
			throw new IllegalArgumentException("Calendar constant '" + name + "' does not exist");
		}
		return constVal;
	}

}
