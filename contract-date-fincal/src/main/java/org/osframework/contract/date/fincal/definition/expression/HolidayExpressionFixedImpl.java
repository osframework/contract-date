/*
 * File: HolidayExpressionFixedImpl.java
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

import org.osframework.contract.date.fincal.definition.HolidayExpression;

/**
 * Produce the date of a holiday occurring on a specific date in a month every
 * year.
 *
 * @author <a href="mailto:dave@osframework.org">Dave Joyce</a>
 */
public class HolidayExpressionFixedImpl implements HolidayExpression {

	private final int calendarMonthConst, dayOfMonth;

	/**
	 * Constructor - accepts expression argument of the form:
	 * <pre>
	 * MONTHNAME/DAY_OF_MONTH
	 * </pre>
	 */
	public HolidayExpressionFixedImpl(final String expression) {
		if (null == expression) {
			throw new IllegalArgumentException("argument 'expression' cannot be null");
		}
		String[] parts = expression.split("/");
		if (2 != parts.length) {
			throw new IllegalArgumentException("Invalid argument 'expression'");
		}
		Class<Calendar> calClass = Calendar.class;
		Field[] calFields = calClass.getFields();
		String monthName   = parts[0].trim().toUpperCase(),
			   dayOfMonth  = parts[1].trim();
		int monthConstVal   = -1;
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
		this.dayOfMonth = Integer.parseInt(dayOfMonth);
	}

	/**
	 * {@inheritDoc}
	 */
	public Date evaluate(int year) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, calendarMonthConst);
		c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return c.getTime();
	}

}
